package org.javatalks.training.hibernate.entity.bag;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "bookmark")
public class Bookmark {
    private long id;
    private Book book;
    private String name;
    private int pageNumber;

    public Bookmark() {
    }

    public Bookmark(String name, int pageNumber) {
        this.name = name;
        this.pageNumber = pageNumber;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "book_bookmark_fk"))
    public Book getBook() {
        return book;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
