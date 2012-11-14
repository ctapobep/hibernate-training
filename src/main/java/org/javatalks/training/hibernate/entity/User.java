package org.javatalks.training.hibernate.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** @author stanislav bashkirtsev */
public class User {
    private Long id;
    private String username;
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

    public Set<Book> getBooks() {
        return books;
    }

    /**
     * This is a method for pervert guys, but we need it. Only DAO has to use this method because while fetching objects
     * from DB, DAO may decide not to load a full User, but leave some its fields not initialized. If we were just
     * creating and setting an empty collection here, users of the API would not distinguish between 'the collection is
     * really empty' and 'collection is not initialized during the fetch' cases.
     *
     * @param books the collection that may or may not be initialized during the fetching from DB
     * @return this
     */
    public User setBooksByDao(Set<Book> books) {
        this.books = books;
        return this;
    }

    public void setBooks(Collection<Book> books) {
        removeAllBooks();
        for (Book book : books) {
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

    @Override
    public String toString() {
        return "User[" + username + "]";
    }
}
