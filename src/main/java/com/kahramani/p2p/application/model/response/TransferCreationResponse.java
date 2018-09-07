package com.kahramani.p2p.application.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferCreationResponse implements Response {

    private String amount;
    private String fromCurrency;
    private String convertedAmount;
    private String toCurrency;
    private String exchangeRate;
    private String transferReference;

    @JsonProperty("receiverAccount")
    private String receiverAccountReference;
    @JsonProperty("senderAccount")
    private String senderAccountReference;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
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

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
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
                "%s{amount=%s, fromCurrency=%s, convertedAmount=%s, toCurrency=%s, exchangeRate=%s, transferReference=%s, " +
                        "receiverAccountReference=%s, senderAccountReference=%s}",
                this.getClass().getSimpleName(),
                getAmount(),
                getFromCurrency(),
                getConvertedAmount(),
                getToCurrency(),
                getExchangeRate(),
                getTransferReference(),
                getReceiverAccountReference(),
                getSenderAccountReference());
    }
}