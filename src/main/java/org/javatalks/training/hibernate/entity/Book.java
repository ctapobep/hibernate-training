package org.javatalks.training.hibernate.entity;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "book")
public class Book {
    private Long id;
    private String title;
    /**
     * Book is not the main side of the association, but User is. If both sides would be able to store the whole
     * association, this might lead to the chaos, because while saving the Book, we would need for instance to load
     * Author and her collection of Books from DB (because author has a Set of books, and this type of collection
     * enforces only a single instance of the same object to be present in collection which we would need to check),
     * <p/> In order to escape the chaos of 'any entity can save the association', we assume that while storing the
     * Book, its Author should already be present in DB.
     */

    private User author;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Transient
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    public Long getAuthorId() {
        return author == null ? null : author.getId();
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

    @Override
    public String toString() {
        return "Book[" + title + "]";
    }
}
