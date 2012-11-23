package org.javatalks.training.hibernate.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author stanislav bashkirtsev */
public class User {
    private Long id;
    private String username;
    private AccessCard accessCard;
    private DiscountCard discountCard;
    private AccountForPaidUsers account;
    private Passport passport;
    private RentedPc rentedPc;
    private ReservedDesk reservedDesk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReservedDesk getReservedDesk() {
        return reservedDesk;
    }

    public void setReservedDesk(ReservedDesk reservedDesk) {
        this.reservedDesk = reservedDesk;
    }

    public RentedPc getRentedPc() {
        return rentedPc;
    }

    public void setRentedPc(RentedPc rentedPc) {
        this.rentedPc = rentedPc;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public AccountForPaidUsers getAccount() {
        return account;
    }

    public void setAccount(AccountForPaidUsers account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccessCard getAccessCard() {
        return accessCard;
    }

    public void setAccessCard(AccessCard accessCard) {
        this.accessCard = accessCard;
    }

    @Override
    public String toString() {
        return "User[" + username + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!username.equals(user.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
