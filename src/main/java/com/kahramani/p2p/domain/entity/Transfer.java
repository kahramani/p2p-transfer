package com.kahramani.p2p.domain.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.Timestamp;

@DatabaseTable(tableName = "transfers")
public class Transfer {

    @DatabaseField(columnName = "id", id = true, useGetSet = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = "reference", width = 15, useGetSet = true, canBeNull = false)
    private String reference;

    @DatabaseField(columnName = "idate", dataType = DataType.TIME_STAMP, useGetSet = true, canBeNull = false)
    private Timestamp insertDate;

    @DatabaseField(columnName = "from_amount", useGetSet = true, canBeNull = false)
    private BigDecimal fromAmount;

    @DatabaseField(columnName = "to_amount", useGetSet = true, canBeNull = false)
    private BigDecimal toAmount;

    @DatabaseField(columnName = "conversion_unit", width = 7, useGetSet = true, canBeNull = false)
    private String conversionUnit;

    @DatabaseField(columnName = "exchange_rate", useGetSet = true, canBeNull = false)
    private BigDecimal exchangeRate;

    @DatabaseField(columnName = "receiver_account_id", useGetSet = true, canBeNull = false, foreign = true)
    private Account receiverAccount;

    @DatabaseField(columnName = "sender_account_id", useGetSet = true, canBeNull = false, foreign = true)
    private Account senderAccount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public String getConversionUnit() {
        return conversionUnit;
    }

    public void setConversionUnit(String conversionUnit) {
        this.conversionUnit = conversionUnit;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%s, reference=%s, insertDate=%s, fromAmount=%s, toAmount=%s, conversionUnit=%s, exchangeRate=%s, receiverAccount=%s, senderAccount=%s}",
                this.getClass().getSimpleName(),
                getId(),
                getReference(),
                getInsertDate(),
                getFromAmount(),
                getToAmount(),
                getConversionUnit(),
                getExchangeRate(),
                getReceiverAccount(),
                getSenderAccount());
    }
}