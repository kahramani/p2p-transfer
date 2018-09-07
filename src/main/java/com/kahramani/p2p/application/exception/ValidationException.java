package com.kahramani.p2p.application.exception;

import com.kahramani.p2p.application.exception.enums.ErrorResponseSet;

public class ValidationException extends RuntimeException {

    private final ErrorResponseSet errorResponseSet;

    public ValidationException(ErrorResponseSet errorResponseSet) {
        this.errorResponseSet = errorResponseSet;
    }

    public ErrorResponseSet getErrorResponseSet() {
        return errorResponseSet;
    }
}