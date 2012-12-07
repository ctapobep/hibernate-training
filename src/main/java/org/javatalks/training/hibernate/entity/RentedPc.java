package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

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

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    @ForeignKey(name = "rented_pc_user_fk")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
