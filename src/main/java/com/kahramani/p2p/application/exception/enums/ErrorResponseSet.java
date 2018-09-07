package com.kahramani.p2p.application.exception.enums;

import io.netty.handler.codec.http.HttpResponseStatus;

public enum ErrorResponseSet {

    SYSTEM_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), 1000, "A system error occurred. Please try again later with a backoff policy or contact us, we are working on it!"),
    DATABASE_ERROR(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), 1001, "A storage error occurred. Please try again later with a backoff policy or contact us, we are working on it!"),

    REQUEST_NOT_DECODED(HttpResponseStatus.BAD_REQUEST.code(), 1500, "Your request is invalid!"),
    REQUEST_NULL(HttpResponseStatus.BAD_REQUEST.code(), 1501, "Request cannot be null!"),
    REQUEST_RECEIVER_IDENTIFIER_NULL(HttpResponseStatus.BAD_REQUEST.code(), 1502, "At least one of the receiver identifier must be present!"),
    REQUEST_RECEIVER_IDENTIFIER_MULTIPLE(HttpResponseStatus.BAD_REQUEST.code(), 1503, "You must specify only one identifier for receiver!"),
    REQUEST_AMOUNT_NULL(HttpResponseStatus.BAD_REQUEST.code(), 1504, "Amount cannot be null!"),
    REQUEST_AMOUNT_NEGATIVE(HttpResponseStatus.BAD_REQUEST.code(), 1505, "Amount cannot be negative!"),
    REQUEST_CURRENCY_NULL(HttpResponseStatus.BAD_REQUEST.code(), 1506, "Currency cannot be null!"),
    REQUEST_CURRENCY_NOT_SUPPORTED(HttpResponseStatus.BAD_REQUEST.code(), 1507, "Currency is not supported!"),

    ACCOUNT_NOT_FOUND(HttpResponseStatus.NOT_FOUND.code(), 1550, "Account not found!"),
    USER_NOT_FOUND(HttpResponseStatus.NOT_FOUND.code(), 1551, "User not found!"),
    TRANSFER_NOT_FOUND(HttpResponseStatus.NOT_FOUND.code(), 1552, "Transfer not found!"),

    INSUFFICIENT_BALANCE(HttpResponseStatus.UNPROCESSABLE_ENTITY.code(), 1600, "Balance is not sufficient to execute transfer!"),
    CIRCULAR_TRANSFER(HttpResponseStatus.UNPROCESSABLE_ENTITY.code(), 1601, "You cannot transfer money from account to the same account!");

    private final Integer httpResponseCode;
    private final Integer errorCode;
    private final String errorMessage;

    ErrorResponseSet(Integer httpResponseCode, Integer errorCode, String errorMessage) {
        this.httpResponseCode = httpResponseCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}