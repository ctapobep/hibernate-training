package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class Author extends User{
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
