package com.kahramani.p2p.application.validator;

import com.kahramani.p2p.application.exception.ValidationException;
import com.kahramani.p2p.application.exception.enums.ErrorResponseSet;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferCreationRequestValidatorTest {

    private TransferCreationRequestValidator transferCreationRequestValidator;

    @Before
    public void setUp() {
        transferCreationRequestValidator = new TransferCreationRequestValidator();
    }

    @Test
    public void should_not_throw_validationException_when_request_is_valid() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setReceiverMobilePhone("+442038720620");
        request.setAmount(BigDecimal.TEN);
        request.setToCurrency("GBP");

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable).isNull();
    }

    @Test
    public void should_throw_validationException_when_request_is_null() {
        TransferCreationRequest request = null;

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_NULL);
    }

    @Test
    public void should_throw_validationException_when_amount_is_null() {
        TransferCreationRequest request = new TransferCreationRequest();

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_AMOUNT_NULL);
    }

    @Test
    public void should_throw_validationException_when_currency_is_null() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setAmount(BigDecimal.TEN);

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_CURRENCY_NULL);
    }

    @Test
    public void should_throw_validationException_when_amount_is_negative() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setAmount(new BigDecimal("-3.4"));
        request.setToCurrency("GBP");

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_AMOUNT_NEGATIVE);
    }

    @Test
    public void should_throw_validationException_when_currency_not_supported() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setAmount(BigDecimal.TEN);
        request.setToCurrency("USD");

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_CURRENCY_NOT_SUPPORTED);
    }

    @Test
    public void should_throw_validationException_when_receiver_identifier_is_null() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setAmount(BigDecimal.TEN);
        request.setToCurrency("EUR");

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_RECEIVER_IDENTIFIER_NULL);
    }

    @Test
    public void should_throw_validationException_when_multiple_receiver_identifier_exists() {
        TransferCreationRequest request = new TransferCreationRequest();
        request.setReceiverMobilePhone("+442038720620");
        request.setReceiverUsername("nikolay");
        request.setAmount(BigDecimal.TEN);
        request.setAmount(BigDecimal.TEN);
        request.setToCurrency("EUR");

        Throwable throwable = Assertions.catchThrowable(() -> transferCreationRequestValidator.validate(request));

        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ValidationException.class);

        ValidationException exception = (ValidationException) throwable;
        assertThat(exception.getErrorResponseSet()).isEqualTo(ErrorResponseSet.REQUEST_RECEIVER_IDENTIFIER_MULTIPLE);
    }
}