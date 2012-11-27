package org.javatalks.training.hibernate.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author stanislav bashkirtsev */
public class Book {
    private long id;
    private String title;
    private List<Comment> comments = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();//uni-bag
    private Collection<Bookmark> bookmarks = new ArrayList<>();//bidi-bag
    private Collection<Appendix> appendixes = new ArrayList<>();//bidi-bag: MTO with joined table
    private Collection<Reviewer> reviewers = new ArrayList<>();//bidi-bag: MTM
    private List<Chapter> chapters = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(Collection<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public Collection<Appendix> getAppendixes() {
        return appendixes;
    }

    public void setAppendixes(Collection<Appendix> appendixes) {
        this.appendixes = appendixes;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Collection<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Collection<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
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

    public Book addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
        bookmark.setBook(this);
        return this;
    }

    public Book addChapter(Chapter chapter){
        chapters.add(chapter);
        chapter.setBook(this);
        return this;
    }
}
