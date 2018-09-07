package com.kahramani.p2p.domain.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.Timestamp;

@DatabaseTable(tableName = "accounts")
public class Account {

    @DatabaseField(columnName = "id", id = true, useGetSet = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = "reference", width = 15, useGetSet = true, canBeNull = false)
    private String reference;

    @DatabaseField(columnName = "idate", dataType = DataType.TIME_STAMP, useGetSet = true, canBeNull = false)
    private Timestamp insertDate;

    @DatabaseField(columnName = "balance", useGetSet = true, canBeNull = false)
    private BigDecimal balance;

    @DatabaseField(columnName = "currency", useGetSet = true, width = 3, canBeNull = false)
    private String currency;

    @DatabaseField(columnName = "user_id", useGetSet = true, canBeNull = false, foreign = true)
    private User user;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%s, reference=%s, insertDate=%s, balance=%s, currency=%s, user=%s}",
                this.getClass().getSimpleName(),
                getId(),
                getReference(),
                getInsertDate(),
                getBalance(),
                getCurrency(),
                getUser());
    }
}