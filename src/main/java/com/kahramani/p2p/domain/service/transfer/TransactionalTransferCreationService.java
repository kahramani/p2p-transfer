package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.application.exception.*;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.model.response.TransferCreationResponse;
import com.kahramani.p2p.domain.component.UniqueIdGenerator;
import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.entity.Currency;
import com.kahramani.p2p.domain.entity.Transfer;
import com.kahramani.p2p.domain.entity.User;
import com.kahramani.p2p.domain.repository.TransferJpaRepository;
import com.kahramani.p2p.domain.service.account.AccountService;
import com.kahramani.p2p.domain.service.user.UserService;
import com.kahramani.p2p.infrastructure.adapter.rate.ExchangeRateClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.Callable;

/**
 * Concurrency-aware transfer creation service
 * @see TransactionalTransferCreationServiceFT
 */
public class TransactionalTransferCreationService implements TransferCreationService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalTransferCreationService.class);

    private final AccountService accountService;
    private final ExchangeRateClient exchangeRateClient;
    private final UserService userService;
    private final TransferJpaRepository transferJpaRepository;
    private final UniqueIdGenerator incrementalIdGenerator;
    private final UniqueIdGenerator referenceUniqueIdGenerator;
    private final ConversionUnitService conversionUnitService;

    public TransactionalTransferCreationService(AccountService accountService, ExchangeRateClient exchangeRateClient,
                                                UserService userService, TransferJpaRepository transferJpaRepository,
                                                UniqueIdGenerator incrementalIdGenerator, UniqueIdGenerator referenceUniqueIdGenerator,
                                                ConversionUnitService conversionUnitService) {
        this.accountService = accountService;
        this.exchangeRateClient = exchangeRateClient;
        this.userService = userService;
        this.transferJpaRepository = transferJpaRepository;
        this.incrementalIdGenerator = incrementalIdGenerator;
        this.referenceUniqueIdGenerator = referenceUniqueIdGenerator;
        this.conversionUnitService = conversionUnitService;
    }

    @Override
    public TransferCreationResponse create(String accountReference, TransferCreationRequest transferCreationRequest) {

        // exchange rate api call will be executed before/outside of the transactional block
        // to avoid holding database connection too long in case of external api latency etc.
        String senderCurrency = accountService.retrieveAccountCurrencyByReference(accountReference).orElseThrow(AccountNotFoundException::new);
        BigDecimal exchangeRate = exchangeRateClient.getRate(Currency.valueOf(senderCurrency), Currency.valueOf(transferCreationRequest.getToCurrency()));
        BigDecimal convertedAmount = transferCreationRequest.getAmount().multiply(exchangeRate);

        logger.debug("Exchange rate applied. Exchange rate: {}, baseAmount: {}{}, convertedAmount: {}{}",
                exchangeRate, transferCreationRequest.getAmount(), senderCurrency, convertedAmount, transferCreationRequest.getToCurrency());

        Callable<Transfer> transactionalCallable = () -> {
            logger.debug("Transfer transaction in db started for {}", transferCreationRequest);

            Account senderAccount = accountService.retrieveAccountByReference(accountReference).orElseThrow(AccountNotFoundException::new);
            logger.debug("Sender account found: {}", senderAccount);

            if (senderAccount.getBalance().compareTo(transferCreationRequest.getAmount()) == -1) {
                throw new InsufficientBalanceException();
            }

            Account receiverAccount = accountService.retrieveAccountByUserIdAndCurrency(getUser(transferCreationRequest).getId(), transferCreationRequest.getToCurrency())
                    .orElseThrow(AccountNotFoundException::new);
            logger.debug("Receiver account found: {}", receiverAccount);

            if (receiverAccount.getReference().equals(accountReference)) {
                // User can transfer money to own account with different reference (e.g. own account but difference currency)
                throw new CircularTransferException();
            }

            BigDecimal receiverBalanceAfterTransfer = receiverAccount.getBalance().add(convertedAmount);
            receiverAccount.setBalance(receiverBalanceAfterTransfer);
            logger.debug("Receiver new balance: {}{}", receiverBalanceAfterTransfer, receiverAccount.getCurrency());
            BigDecimal senderBalanceAfterTransfer = senderAccount.getBalance().subtract(transferCreationRequest.getAmount());
            senderAccount.setBalance(senderBalanceAfterTransfer);
            logger.debug("Sender new balance: {}{}", senderBalanceAfterTransfer, senderAccount.getCurrency());
            accountService.updateAccount(receiverAccount);
            accountService.updateAccount(senderAccount);

            Transfer transfer = new Transfer();
            transfer.setId(Long.parseLong(incrementalIdGenerator.generateId()));
            transfer.setReference(referenceUniqueIdGenerator.generateId());
            transfer.setInsertDate(Timestamp.from(Instant.now()));
            transfer.setFromAmount(transferCreationRequest.getAmount());
            transfer.setToAmount(convertedAmount);
            transfer.setConversionUnit(conversionUnitService.getConversionUnit(Currency.valueOf(senderAccount.getCurrency()), Currency.valueOf(receiverAccount.getCurrency())));
            transfer.setExchangeRate(exchangeRate);
            transfer.setReceiverAccount(receiverAccount);
            transfer.setSenderAccount(senderAccount);
            transferJpaRepository.save(transfer);
            logger.debug("Transfer created: {}", transfer);
            return transfer;
        };

        try {
            Transfer completedTransfer = transferJpaRepository.processInTransaction(transactionalCallable);
            return createResponse(completedTransfer);
        } catch (SQLException e) {
            Throwable cause = e.getCause();
            if (AccountNotFoundException.class.isInstance(cause)) {
                throw new AccountNotFoundException();
            } else if (UserNotFoundException.class.isInstance(cause)) {
                throw new UserNotFoundException();
            } else if (CircularTransferException.class.isInstance(cause)) {
                throw new CircularTransferException();
            } else if (InsufficientBalanceException.class.isInstance(cause)) {
                throw new InsufficientBalanceException();
            }
            throw new DatabaseException();
        }
    }

    private User getUser(TransferCreationRequest transferCreationRequest) {
        if (StringUtils.isNotBlank(transferCreationRequest.getReceiverMobilePhone())) {
            return userService.retrieveUserByMobileNumber(transferCreationRequest.getReceiverMobilePhone()).orElseThrow(UserNotFoundException::new);
        }

        return userService.retrieveUserByUsername(transferCreationRequest.getReceiverUsername()).orElseThrow(UserNotFoundException::new);
    }

    private TransferCreationResponse createResponse(Transfer transfer) {
        TransferCreationResponse transferCreationResponse = new TransferCreationResponse();
        transferCreationResponse.setAmount(transfer.getFromAmount().toString());
        transferCreationResponse.setFromCurrency(transfer.getSenderAccount().getCurrency());
        transferCreationResponse.setConvertedAmount(transfer.getToAmount().toString());
        transferCreationResponse.setToCurrency(transfer.getReceiverAccount().getCurrency());
        transferCreationResponse.setExchangeRate(transfer.getExchangeRate().toString());
        transferCreationResponse.setTransferReference(transfer.getReference());
        transferCreationResponse.setReceiverAccountReference(transfer.getReceiverAccount().getReference());
        transferCreationResponse.setSenderAccountReference(transfer.getSenderAccount().getReference());

        return transferCreationResponse;
    }
}