package org.javatalks.training.hibernate.entity;

import java.util.Date;

/** @author stanislav bashkirtsev */
public class DiscountCard {
    private long id;
    private String number;
    private Date expiry;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
}
