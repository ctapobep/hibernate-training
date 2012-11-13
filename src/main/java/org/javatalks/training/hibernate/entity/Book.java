package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return author == null ? null : author.getId();
    }
}
