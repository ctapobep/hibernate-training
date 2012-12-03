package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class Personnel extends User {
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
