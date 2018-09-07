package com.kahramani.p2p.domain.service.account;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.AbstractFunctionalTest;
import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.repository.h2.H2DbAccountJpaRepository;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyAccountServiceFT extends AbstractFunctionalTest {

    private static ProxyAccountService proxyAccountService;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DatabaseSessionManager<ConnectionSource> databaseSessionManager = initializeDatabaseSession("testProxyAccountServiceFT");
        proxyAccountService = new ProxyAccountService(new H2DbAccountJpaRepository(databaseSessionManager));
    }

    @Test
    public void should_return_account_for_reference() {
        String accountReference = "X2QCL6BNB7ZL49K";
        Optional<Account> optionalAccount = proxyAccountService.retrieveAccountByReference(accountReference);

        assertThat(optionalAccount.isPresent()).isTrue();
        Account account = optionalAccount.get();
        assertThat(account.getId()).isEqualTo(1);
        assertThat(account.getReference()).isEqualTo("X2QCL6BNB7ZL49K");
        assertThat(account.getInsertDate()).isNotNull();
        assertThat(account.getBalance()).isEqualTo("300.00");
        assertThat(account.getCurrency()).isEqualTo("EUR");
        assertThat(account.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void should_return_account_for_id() {
        Long id = 1L;
        Optional<Account> optionalAccount = proxyAccountService.retrieveAccountById(id);

        assertThat(optionalAccount.isPresent()).isTrue();
        Account account = optionalAccount.get();
        assertThat(account.getId()).isEqualTo(1);
        assertThat(account.getReference()).isEqualTo("X2QCL6BNB7ZL49K");
        assertThat(account.getInsertDate()).isNotNull();
        assertThat(account.getBalance()).isEqualTo("300.00");
        assertThat(account.getCurrency()).isEqualTo("EUR");
        assertThat(account.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void should_return_account_for_userId_and_currency() {
        Long userId = 1L;
        String currency = "EUR";
        Optional<Account> optionalAccount = proxyAccountService.retrieveAccountByUserIdAndCurrency(userId, currency);

        assertThat(optionalAccount.isPresent()).isTrue();
        Account account = optionalAccount.get();
        assertThat(account.getId()).isEqualTo(1);
        assertThat(account.getReference()).isEqualTo("X2QCL6BNB7ZL49K");
        assertThat(account.getInsertDate()).isNotNull();
        assertThat(account.getBalance()).isEqualTo("300.00");
        assertThat(account.getCurrency()).isEqualTo("EUR");
        assertThat(account.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void should_return_account_currency_for_reference() {
        String accountReference = "EGHD4TKYCVGFXNO";
        Optional<String> optionalCurrency = proxyAccountService.retrieveAccountCurrencyByReference(accountReference);

        assertThat(optionalCurrency.isPresent()).isTrue();
        assertThat(optionalCurrency.get()).isEqualTo("GBP");
    }

    @Test
    public void should_update() {
        String accountReference = "8K3PK6VU8D232PE";
        Optional<Account> optionalAccount = proxyAccountService.retrieveAccountByReference(accountReference);

        Account account = optionalAccount.get();
        account.setInsertDate(Timestamp.from(Instant.now()));

        Throwable throwable = Assertions.catchThrowable(() -> proxyAccountService.updateAccount(account));

        assertThat(throwable).isNull();
    }
}