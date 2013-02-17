package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "personnel")
public class Personnel extends User {
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
