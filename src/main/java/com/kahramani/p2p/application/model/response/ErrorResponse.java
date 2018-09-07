package com.kahramani.p2p.application.model.response;

import com.kahramani.p2p.application.exception.enums.ErrorResponseSet;

public final class ErrorResponse implements Response {

    private final Integer errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorResponseSet errorResponseSet) {
        this.errorCode = errorResponseSet.getErrorCode();
        this.errorMessage = errorResponseSet.getErrorMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{errorCode=%d, errorMessage=%s}",
                this.getClass().getSimpleName(),
                getErrorCode(),
                getErrorMessage());
    }
}