package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.domain.entity.Currency;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferConversionUnitServiceTest {

    private TransferConversionUnitService transferConversionUnitService;

    @Before
    public void setUp() {
        transferConversionUnitService = new TransferConversionUnitService();
    }

    @Test
    public void should_return_eur_to_gbp() {
        assertThat(transferConversionUnitService.getConversionUnit(Currency.EUR, Currency.GBP)).isEqualTo("EUR_GBP");
    }

    @Test
    public void should_return_eur() {
        assertThat(transferConversionUnitService.getFrom("EUR_TRY")).isEqualTo("EUR");
    }

    @Test
    public void should_return_try() {
        assertThat(transferConversionUnitService.getTo("EUR_TRY")).isEqualTo("TRY");
    }
}