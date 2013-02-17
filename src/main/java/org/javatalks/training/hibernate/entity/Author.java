package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "author")
public class Author extends User {
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
