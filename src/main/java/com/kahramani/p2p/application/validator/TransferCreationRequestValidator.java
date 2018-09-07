package com.kahramani.p2p.application.validator;

import com.kahramani.p2p.application.exception.ValidationException;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.domain.entity.Currency;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.kahramani.p2p.application.exception.enums.ErrorResponseSet.*;

public class TransferCreationRequestValidator implements RequestValidator<TransferCreationRequest> {

    @Override
    public void validate(TransferCreationRequest transferCreationRequest) throws ValidationException {
        validateIfPresentOrThrow(transferCreationRequest, () -> new ValidationException(REQUEST_NULL));
        validateIfPresentOrThrow(transferCreationRequest.getAmount(), () -> new ValidationException(REQUEST_AMOUNT_NULL));
        validateIfPresentOrThrow(transferCreationRequest.getToCurrency(), () -> new ValidationException(REQUEST_CURRENCY_NULL));

        validateAmount(transferCreationRequest.getAmount());
        validateCurrency(transferCreationRequest.getToCurrency());
        validateReceiverIdentifier(transferCreationRequest);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException(REQUEST_AMOUNT_NEGATIVE);
        }
    }

    private void validateCurrency(String currency) {
        Arrays.stream(Currency.values())
                .filter(c -> c.name().equals(currency))
                .findFirst()
                .orElseThrow(() -> new ValidationException(REQUEST_CURRENCY_NOT_SUPPORTED));
    }

    private void validateReceiverIdentifier(TransferCreationRequest transferCreationRequest) {
        long count = Stream.of(transferCreationRequest.getReceiverMobilePhone(),
                transferCreationRequest.getReceiverUsername())
                .filter(StringUtils::isNotBlank)
                .count();

        if (count == 0L) {
            throw new ValidationException(REQUEST_RECEIVER_IDENTIFIER_NULL);
        } else if (count != 1L) {
            throw new ValidationException(REQUEST_RECEIVER_IDENTIFIER_MULTIPLE);
        }
    }
}