package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class RentedPc {
    private long id;
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
