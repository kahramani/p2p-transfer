package com.kahramani.p2p.domain.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(columnName = "id", id = true, useGetSet = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = "reference", width = 15, useGetSet = true, canBeNull = false)
    private String reference;

    @DatabaseField(columnName = "idate", dataType = DataType.TIME_STAMP, useGetSet = true, canBeNull = false)
    private Timestamp insertDate;

    @DatabaseField(columnName = "user_name", unique = true, width = 20, useGetSet = true, canBeNull = false)
    private String userName;

    @DatabaseField(columnName = "mobile_number", unique = true, width = 20, useGetSet = true, canBeNull = false)
    private String mobileNumber;

    @DatabaseField(columnName = "email", unique = true, width = 30, useGetSet = true, canBeNull = false)
    private String email;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%s, reference=%s, insertDate=%s, username=%s, mobileNumber=%s, email=%s}",
                this.getClass().getSimpleName(),
                getId(),
                getReference(),
                getInsertDate(),
                getUserName(),
                getMobileNumber(),
                getEmail());
    }
}