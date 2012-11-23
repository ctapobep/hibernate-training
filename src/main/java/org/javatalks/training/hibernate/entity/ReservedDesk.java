package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class ReservedDesk {
    private long id;
    private int number;
    private User userReserved;

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

    public User getUserReserved() {
        return userReserved;
    }

    public void setUserReserved(User userReserved) {
        this.userReserved = userReserved;
    }
}
