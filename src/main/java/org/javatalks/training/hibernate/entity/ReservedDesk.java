package org.javatalks.training.hibernate.entity;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
public class ReservedDesk {
    private long id;
    private int number;
    private User userReserved;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @OneToOne
    @JoinTable(
            name = "user_reserved_desk",
            joinColumns = @JoinColumn(name = "reserved_desk_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public User getUserReserved() {
        return userReserved;
    }

    public void setUserReserved(User userReserved) {
        this.userReserved = userReserved;
    }
}
