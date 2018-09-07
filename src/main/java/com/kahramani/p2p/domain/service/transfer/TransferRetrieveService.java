package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.application.exception.AccountNotFoundException;
import com.kahramani.p2p.application.exception.TransferNotFoundException;
import com.kahramani.p2p.application.model.response.TransferRetrieveResponse;
import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.entity.Transfer;
import com.kahramani.p2p.domain.repository.TransferJpaRepository;
import com.kahramani.p2p.domain.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRetrieveService {

    private static final Logger logger = LoggerFactory.getLogger(TransferRetrieveService.class);

    private final TransferJpaRepository transferJpaRepository;
    private final AccountService accountService;
    private final ConversionUnitService conversionUnitService;

    public TransferRetrieveService(TransferJpaRepository transferJpaRepository,
                                   AccountService accountService,
                                   ConversionUnitService conversionUnitService) {
        this.transferJpaRepository = transferJpaRepository;
        this.accountService = accountService;
        this.conversionUnitService = conversionUnitService;
    }

    public TransferRetrieveResponse retrieveByReference(String transferReference) {
        Transfer transfer = transferJpaRepository.findByReference(transferReference).orElseThrow(TransferNotFoundException::new);
        logger.debug("Transfer found: {}", transfer);

        Account receiverAccount = accountService.retrieveAccountById(transfer.getReceiverAccount().getId()).orElseThrow(AccountNotFoundException::new);
        Account senderAccount = accountService.retrieveAccountById(transfer.getSenderAccount().getId()).orElseThrow(AccountNotFoundException::new);

        TransferRetrieveResponse transferRetrieveResponse = new TransferRetrieveResponse();
        transferRetrieveResponse.setTransferReference(transferReference);
        transferRetrieveResponse.setDate(transfer.getInsertDate().toString());
        transferRetrieveResponse.setFromAmount(transfer.getFromAmount().toString());
        transferRetrieveResponse.setFromCurrency(conversionUnitService.getFrom(transfer.getConversionUnit()));
        transferRetrieveResponse.setToAmount(transfer.getToAmount().toString());
        transferRetrieveResponse.setToCurrency(conversionUnitService.getTo(transfer.getConversionUnit()));
        transferRetrieveResponse.setExchangeRate(transfer.getExchangeRate().toString());
        transferRetrieveResponse.setReceiverAccountReference(receiverAccount.getReference());
        transferRetrieveResponse.setSenderAccountReference(senderAccount.getReference());
        return transferRetrieveResponse;
    }
}
