package com.kahramani.p2p.infrastructure.adapter.rate;

import com.kahramani.p2p.domain.entity.Currency;

import java.math.BigDecimal;

public interface ExchangeRateClient {

    BigDecimal getRate(Currency from, Currency to);
}