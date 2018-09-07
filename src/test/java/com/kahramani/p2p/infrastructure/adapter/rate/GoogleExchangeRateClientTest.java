package com.kahramani.p2p.infrastructure.adapter.rate;

import com.kahramani.p2p.domain.entity.Currency;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleExchangeRateClientTest {

    private GoogleExchangeRateClient googleExchangeRateClient;

    @Before
    public void setUp() {
        googleExchangeRateClient = new GoogleExchangeRateClient();
    }

    @Test
    public void should_return_one_for_same_currencies() {
        assertThat(googleExchangeRateClient.getRate(Currency.EUR, Currency.EUR)).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void should_return_rounded_with_scale_of_two() {
        assertThat(googleExchangeRateClient.getRate(Currency.TRY, Currency.EUR).toString()).isEqualTo("0.13");
    }
}