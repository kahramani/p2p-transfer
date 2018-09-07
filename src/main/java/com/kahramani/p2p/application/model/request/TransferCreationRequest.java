package com.kahramani.p2p.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferCreationRequest implements Request {

    @JsonProperty("mobilePhone")
    private String receiverMobilePhone;
    @JsonProperty("userName")
    private String receiverUsername;
    private BigDecimal amount;
    private String toCurrency;

    public String getReceiverMobilePhone() {
        return receiverMobilePhone;
    }

    public void setReceiverMobilePhone(String receiverMobilePhone) {
        this.receiverMobilePhone = receiverMobilePhone;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{receiverMobilePhone=%s, receiverUsername=%s, amount=%s, toCurrency=%s}",
                this.getClass().getSimpleName(),
                getReceiverMobilePhone(),
                getReceiverUsername(),
                getAmount(),
                getToCurrency());
    }
}