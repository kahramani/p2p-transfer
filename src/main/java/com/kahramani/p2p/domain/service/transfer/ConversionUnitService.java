package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.domain.entity.Currency;

public interface ConversionUnitService {

    String getConversionUnit(Currency from, Currency to);

    String getFrom(String conversionUnit);

    String getTo(String conversionUnit);
}