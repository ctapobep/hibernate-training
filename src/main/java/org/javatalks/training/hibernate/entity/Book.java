package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;

/** @author stanislav bashkirtsev */
@Entity
public class Book {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
