package org.javatalks.training.hibernate.entity.bag;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "book")
public class Book {
    private long id;
    private String title;
    private List<Comment> comments = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();//uni-bag
    private Collection<Bookmark> bookmarks = new ArrayList<>();//bidi-bag
    private Collection<Appendix> appendixes = new ArrayList<>();//bidi-bag: MTO with joined table
    private Collection<Reviewer> reviewers = new ArrayList<>();//bidi-bag: MTM
    private List<Chapter> chapters = new ArrayList<>();

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @OneToMany(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "book_id", nullable = false)
    @ForeignKey(name = "book_author_fk")
    public List<Author> getAuthors() {
        return authors;
    }

    @OneToMany(orphanRemoval = true, mappedBy = "book",
            cascade = javax.persistence.CascadeType.ALL, targetEntity = Bookmark.class)
    @ForeignKey(name = "book_bookmark_fk")
    public Collection<Bookmark> getBookmarks() {
        return bookmarks;
    }

    @OneToMany(mappedBy = "book")
    @ForeignKey(name = "appendix_book_fk")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public Collection<Appendix> getAppendixes() {
        return appendixes;
    }

    @ManyToMany
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "book_reviewer",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "reviewer_id"),
            uniqueConstraints = @UniqueConstraint(name = "book_reviewer_uk", columnNames = {"book_id", "reviewer_id"}))
    public Collection<Reviewer> getReviewers() {
        return reviewers;
    }

    @Transient
    public List<Chapter> getChapters() {
        return chapters;
    }

    @Transient
    public List<Comment> getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setReviewers(Collection<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public void setAppendixes(Collection<Appendix> appendixes) {
        this.appendixes = appendixes;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void setBookmarks(Collection<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Book addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
        bookmark.setBook(this);
        return this;
    }

    public Book addChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setBook(this);
        return this;
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
}
