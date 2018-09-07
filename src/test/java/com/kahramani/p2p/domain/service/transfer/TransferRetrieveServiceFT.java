package com.kahramani.p2p.domain.service.transfer;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.AbstractFunctionalTest;
import com.kahramani.p2p.application.model.response.TransferRetrieveResponse;
import com.kahramani.p2p.domain.repository.h2.H2DbAccountJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbTransferJpaRepository;
import com.kahramani.p2p.domain.service.account.AccountService;
import com.kahramani.p2p.domain.service.account.ProxyAccountService;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferRetrieveServiceFT extends AbstractFunctionalTest {

    private static TransferRetrieveService transferRetrieveService;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DatabaseSessionManager<ConnectionSource> databaseSessionManager = initializeDatabaseSession("testTransferRetrieveServiceFT");
        AccountService accountService = new ProxyAccountService(new H2DbAccountJpaRepository(databaseSessionManager));
        ConversionUnitService conversionUnitService = new TransferConversionUnitService();
        transferRetrieveService = new TransferRetrieveService(new H2DbTransferJpaRepository(databaseSessionManager), accountService, conversionUnitService);
    }

    @Test
    public void should_retrieve_transfer_by_reference() {
        String transferReference = "N8TNO6BMB7KL49K";
        TransferRetrieveResponse transferRetrieveResponse = transferRetrieveService.retrieveByReference(transferReference);

        assertThat(transferRetrieveResponse.getTransferReference()).isEqualTo("N8TNO6BMB7KL49K");
        assertThat(transferRetrieveResponse.getDate()).isNotNull();
        assertThat(transferRetrieveResponse.getFromAmount()).isEqualTo("382.80");
        assertThat(transferRetrieveResponse.getFromCurrency()).isEqualTo("EUR");
        assertThat(transferRetrieveResponse.getToAmount()).isEqualTo("344.52");
        assertThat(transferRetrieveResponse.getToCurrency()).isEqualTo("GBP");
        assertThat(transferRetrieveResponse.getExchangeRate()).isEqualTo("0.90");
        assertThat(transferRetrieveResponse.getReceiverAccountReference()).isEqualTo("CHQ7DAURKL1O8FB");
        assertThat(transferRetrieveResponse.getSenderAccountReference()).isEqualTo("F62QHCMPLHFOHID");
    }
}