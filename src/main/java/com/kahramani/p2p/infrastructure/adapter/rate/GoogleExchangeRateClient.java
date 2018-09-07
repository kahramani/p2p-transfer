package com.kahramani.p2p.infrastructure.adapter.rate;

import com.kahramani.p2p.domain.entity.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * It is a mock service with hard-coded values
 */
public class GoogleExchangeRateClient implements ExchangeRateClient {

    private static final int SCALE = 2;

    @Override
    public BigDecimal getRate(Currency from, Currency to) {
        if (from == to)
            return BigDecimal.ONE;

        if (Currency.GBP == from && Currency.EUR == to) {
            return new BigDecimal("1.11");
        } else if (Currency.GBP == from && Currency.TRY == to) {
            return new BigDecimal("8.50");
        } else if (Currency.EUR == from && Currency.TRY == to) {
            return new BigDecimal("7.66");
        }

        return BigDecimal.ONE.divide(getRate(to, from), SCALE, RoundingMode.HALF_EVEN);
    }
}