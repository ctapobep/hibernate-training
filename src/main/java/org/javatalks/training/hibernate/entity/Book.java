package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/** @author stanislav bashkirtsev */
@Entity
public class Book {
    private long id;
    private String title;
    private List<Comment> comments = new ArrayList<>();

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    public List<Comment> getComments() {
        return comments;
    }


    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (title != null ? !title.equals(book.title) : book.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
