package com.kahramani.p2p.application.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRetrieveResponse implements Response {

    private String transferReference;
    private String date;
    private String fromAmount;
    private String fromCurrency;
    private String toAmount;
    private String toCurrency;
    private String exchangeRate;

    @JsonProperty("receiverAccount")
    private String receiverAccountReference;
    @JsonProperty("senderAccount")
    private String senderAccountReference;

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(String fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToAmount() {
        return toAmount;
    }

    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getReceiverAccountReference() {
        return receiverAccountReference;
    }

    public void setReceiverAccountReference(String receiverAccountReference) {
        this.receiverAccountReference = receiverAccountReference;
    }

    public String getSenderAccountReference() {
        return senderAccountReference;
    }

    public void setSenderAccountReference(String senderAccountReference) {
        this.senderAccountReference = senderAccountReference;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{transferReference=%s, date=%s, fromAmount=%s, fromCurrency=%s, toAmount=%s, toCurrency=%s, " +
                        "exchangeRate=%s, receiverAccountReference=%s, senderAccountReference=%s }",
                this.getClass().getSimpleName(),
                getTransferReference(),
                getDate(),
                getFromAmount(),
                getFromCurrency(),
                getToAmount(),
                getToCurrency(),
                getExchangeRate(),
                getReceiverAccountReference(),
                getSenderAccountReference());
    }
}