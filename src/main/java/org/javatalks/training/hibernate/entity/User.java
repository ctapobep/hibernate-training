package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "discount_card_id", name = "discount_id_uk"),
        @UniqueConstraint(columnNames = "rented_pc_id", name = "user_rented_pc_id_uk")
})
public class User {
    private Long id;
    private String username;
    private AccessCard accessCard;
    private DiscountCard discountCard;
    private AccountForPaidUsers account;
    private Passport passport;
    private RentedPc rentedPc;
    private ReservedDesk reservedDesk;

    @Id
    @GeneratedValue(generator = "accessCardIdGenerator")
    @org.hibernate.annotations.GenericGenerator(
            name = "accessCardIdGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "accessCard")
    )
    public Long getId() {
        return id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public AccountForPaidUsers getAccount() {
        return account;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_reserved_desk",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "reserved_desk_id")
    )
    public ReservedDesk getReservedDesk() {
        return reservedDesk;
    }

    @ManyToOne
    public Passport getPassport() {
        return passport;
    }

    /**
     * User's id is references to access card's ID.
     * @return
     */
    @OneToOne
    @PrimaryKeyJoinColumn
    public AccessCard getAccessCard() {
        return accessCard;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_card_id", unique = true)
    @ForeignKey(name = "discount_card_fk")
    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    @ManyToOne
    @JoinColumn(name = "rented_pc_id", unique = true)
    @ForeignKey(name = "user_rented_pc_fk")
    public RentedPc getRentedPc() {
        return rentedPc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReservedDesk(ReservedDesk reservedDesk) {
        this.reservedDesk = reservedDesk;
    }

    public void setRentedPc(RentedPc rentedPc) {
        this.rentedPc = rentedPc;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
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
