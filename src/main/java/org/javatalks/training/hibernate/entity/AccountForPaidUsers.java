package org.javatalks.training.hibernate.entity;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
public class AccountForPaidUsers {
    private long id;
    private String number;
    private double availableMoney;
    private User user;

    @Id
    @GeneratedValue(generator = "userIdGenerator")
    @org.hibernate.annotations.GenericGenerator(
            name = "userIdGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "user")
    )
    public long getId() {
        return id;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    public User getUser() {
        return user;
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

    public double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
