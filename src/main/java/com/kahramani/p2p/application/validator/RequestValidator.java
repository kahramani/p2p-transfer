package com.kahramani.p2p.application.validator;

import com.kahramani.p2p.application.exception.ValidationException;

import java.util.Optional;
import java.util.function.Supplier;

public interface RequestValidator<T> {

    void validate(T t) throws ValidationException;

    default void validateIfPresentOrThrow(Object o, Supplier<ValidationException> exceptionSupplier) {
        Optional.ofNullable(o)
                .orElseThrow(exceptionSupplier);
    }
}