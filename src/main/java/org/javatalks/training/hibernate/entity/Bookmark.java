package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
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

    public long getId() {
        return id;
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

    public Book getBook() {
        return book;
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
