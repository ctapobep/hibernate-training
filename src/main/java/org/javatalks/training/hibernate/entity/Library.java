package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/** @author stanislav bashkirtsev */
@Entity
public class Library {
    private long id;
    private List<Book> books;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @OneToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    public List<Book> getBooks() {
        return books;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
