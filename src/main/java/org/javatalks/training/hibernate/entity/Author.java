package org.javatalks.training.hibernate.entity;

import java.util.HashSet;
import java.util.Set;

/** @author stanislav bashkirtsev */
public class Author {
    private long id;
    private String name;
    private Set<Book> books = new HashSet<>();

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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
