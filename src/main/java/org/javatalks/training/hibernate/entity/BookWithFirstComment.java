package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class BookWithFirstComment {
    private long id;
    private String title;
    private Comment comment;

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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
