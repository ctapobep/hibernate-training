package org.javatalks.training.hibernate.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author stanislav bashkirtsev */
public class Reviewer {
    private long id;
    private String name;
    private Collection<Book> reviewedBooks = new ArrayList<>();//MTM bag

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

    public Collection<Book> getReviewedBooks() {
        return reviewedBooks;
    }

    public void setReviewedBooks(Collection<Book> reviewedBooks) {
        this.reviewedBooks = reviewedBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reviewer reviewer = (Reviewer) o;

        if (name != null ? !name.equals(reviewer.name) : reviewer.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
