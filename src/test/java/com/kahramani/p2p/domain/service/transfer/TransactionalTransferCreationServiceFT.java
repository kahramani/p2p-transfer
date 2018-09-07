package com.kahramani.p2p.domain.service.transfer;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.AbstractConcurrentFunctionalTest;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.model.response.TransferCreationResponse;
import com.kahramani.p2p.domain.component.UniqueIdGenerator;
import com.kahramani.p2p.domain.component.impl.IncrementalUniqueIdGenerator;
import com.kahramani.p2p.domain.component.impl.ReferenceUniqueIdGenerator;
import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.entity.User;
import com.kahramani.p2p.domain.repository.TransferJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbAccountJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbTransferJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbUserJpaRepository;
import com.kahramani.p2p.domain.service.account.AccountService;
import com.kahramani.p2p.domain.service.account.ProxyAccountService;
import com.kahramani.p2p.domain.service.user.ProxyUserService;
import com.kahramani.p2p.domain.service.user.UserService;
import com.kahramani.p2p.infrastructure.adapter.rate.ExchangeRateClient;
import com.kahramani.p2p.infrastructure.adapter.rate.GoogleExchangeRateClient;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionalTransferCreationServiceFT extends AbstractConcurrentFunctionalTest<TransferCreationResponse> {

    private static TransactionalTransferCreationService transferCreationService;
    private static TransferJpaRepository transferJpaRepository;
    private static AccountService accountService;
    private static UserService userService;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DatabaseSessionManager<ConnectionSource> databaseSessionManager = initializeDatabaseSession("testTransactionalTransferCreationServiceFT");
        accountService = new ProxyAccountService(new H2DbAccountJpaRepository(databaseSessionManager));
        ExchangeRateClient exchangeRateClient = new GoogleExchangeRateClient();
        userService = new ProxyUserService(new H2DbUserJpaRepository(databaseSessionManager));
        transferJpaRepository = new H2DbTransferJpaRepository(databaseSessionManager);
        UniqueIdGenerator incrementalUniqueIdGenerator = new IncrementalUniqueIdGenerator();
        UniqueIdGenerator referenceUniqueIdGenerator = new ReferenceUniqueIdGenerator();
        ConversionUnitService conversionUnitService = new TransferConversionUnitService();

        transferCreationService = new TransactionalTransferCreationService(accountService, exchangeRateClient,
                userService, transferJpaRepository, incrementalUniqueIdGenerator, referenceUniqueIdGenerator, conversionUnitService);
    }

    @Test
    public void should_concurrently_create_transfer_and_change_balance_without_race_condition_inconvenience() throws InterruptedException {
        String accountReference = "EGHD4TKYCVGFXNO";
        TransferCreationRequest request = new TransferCreationRequest();
        request.setReceiverMobilePhone("+442033228352");
        request.setAmount(new BigDecimal("12"));
        request.setToCurrency("GBP");

        List<TransferCreationResponse> results = super.callAndGetResults(() -> transferCreationService.create(accountReference, request));

        assertThat(results).isNotEmpty().hasSize(EXECUTION_COUNT);

        results.stream()
                .map(TransferCreationResponse::getTransferReference)
                .forEach(reference -> {
                    assertThat(transferJpaRepository.findByReference(reference).isPresent()).isTrue();
                });

        Account senderAccount = accountService.retrieveAccountByReference("EGHD4TKYCVGFXNO").get();
        User user = userService.retrieveUserByMobileNumber("+442033228352").get();
        Account receiverAccount = accountService.retrieveAccountByUserIdAndCurrency(user.getId(), "GBP").get();

        assertThat(senderAccount.getBalance()).isEqualTo("980.00");
        assertThat(receiverAccount.getBalance()).isEqualTo("320.00");
    }
}