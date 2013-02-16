package org.javatalks.training.hibernate.entity.map;

import javax.persistence.*;
import javax.persistence.Table;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "stand")
public class Stand {
    private long id;
    private String number;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setId(long id) {
        this.id = id;
    }
}
