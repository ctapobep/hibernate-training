package org.javatalks.training.hibernate.entity;

import java.util.ArrayList;
import java.util.List;

/** @author stanislav bashkirtsev */
public class Book {
    private long id;
    private String title;
    private List<Comment> comments = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
