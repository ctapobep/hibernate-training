package org.javatalks.training.hibernate.entity;

import java.util.Set;

/** @author stanislav bashkirtsev */
public class Library {
    private Long id;
    private String name;
    private User owner;
    private Set<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    /**
     * Returns the ID of the owner or null if the owner is null or she is not persisted yet.
     *
     * @return the ID of the owner or null if the owner is null or she is not persisted yet.
     */
    public Long getOwnerId() {
        return owner == null ? null : owner.getId();
    }
}
