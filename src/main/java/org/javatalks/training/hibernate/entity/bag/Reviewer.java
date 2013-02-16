package org.javatalks.training.hibernate.entity.bag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "reviewer")
public class Reviewer {
    private long id;
    private String name;
    private Collection<Book> reviewedBooks = new ArrayList<>();//MTM bag

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @ManyToMany(mappedBy = "reviewers")
    public Collection<Book> getReviewedBooks() {
        return reviewedBooks;
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
