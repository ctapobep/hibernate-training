package org.javatalks.training.hibernate.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author stanislav bashkirtsev */
public class User {
    private Long id;
    private String username;
    private AccessCard accessCard;
    /**
     * Author is the main side of the association, so it's responsible for managing relationship. Main side has to
     * ensure that book has its author. See {@link Book} for more details on this decision.
     */
    private Set<Book> books = new HashSet<>();

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

    public AccessCard getAccessCard() {
        return accessCard;
    }

    public void setAccessCard(AccessCard accessCard) {
        this.accessCard = accessCard;
    }

    public Set<Book> getBooks() {
        return books;
    }

    /**
     * Replaces the current list of books by removing previous ones and setting the new collection of books. For each
     * specified book, the author is set to the current user in this method.
     * @param books books to be set instead of previous ones
     */
    public void setBooks(Collection<Book> books) {
        removeAllBooks();
        for (Book book : books) {
            addBook(book);
        }
    }

    /**
     * Internal hidden getter for ORM-only usage. Now we can freely use {@link #setBooks(java.util.Collection)} method
     * and put some additional logic there without fear that it breaks something inside Hibernate.
     *
     * @return the internal list of books
     */
    Set<Book> getBooksByOrm() {
        return books;
    }

    /**
     * Accessible only by Hibernate, because we don't want any logic being put into an accessor that is used by ORM.
     * @param books the books collection to be just set to the field, without copying the elements or anything
     */
    void setBooksByOrm(Set<Book> books) {
        this.books = books;
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

    @Override
    public String toString() {
        return "User[" + username + "]";
    }
}
