package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
public class RentedPc {
    private long id;
    private User user;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", unique = true)
    @ForeignKey(name = "rented_pc_user_fk")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
