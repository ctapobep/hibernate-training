package org.javatalks.training.hibernate.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author stanislav bashkirtsev */
public class User {
    private Long id;
    private String username;
    private Set<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        removeAllBooks();
        this.books = new HashSet<>(books);
        for (Book book : this.books) {
            addBook(book);
        }
    }

    public void addBook(Book book) {
        if (books.add(book)) {
            book.setAuthor(this);
        }
    }

    public void removeAllBooks() {
        for (Book book : books) {
            removeBook(book);
        }
    }

    public boolean removeBook(Book book) {
        if (books.remove(book)) {
            book.setAuthor(null);
            return true;
        }
        return false;
    }
}
