package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.domain.entity.Currency;

public class TransferConversionUnitService implements ConversionUnitService {

    private static final String DELIMITER = "_";

    @Override
    public String getConversionUnit(Currency from, Currency to) {
        return String.format("%s" + DELIMITER + "%s", from.name(), to.name());
    }

    @Override
    public String getFrom(String conversionUnit) {
        return conversionUnit.split(DELIMITER)[0];
    }

    @Override
    public String getTo(String conversionUnit) {
        return conversionUnit.split(DELIMITER)[1];
    }
}