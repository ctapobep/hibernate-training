package org.javatalks.training.hibernate.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author stanislav bashkirtsev */
public class Book {
    private Long id;
    private String title;
    private BookCover cover;
    private List<Chapter> chapters;
    private Map<String, Object> properties = new HashMap<>();
    /**
     * Book is not the main side of the association, but User is. If both sides would be able to store the whole
     * association, this might lead to the chaos, because while saving the Book, we would need for instance to load
     * Author and her collection of Books from DB (because author has a Set of books, and this type of collection
     * enforces only a single instance of the same object to be present in collection which we would need to check),
     * <p/> In order to escape the chaos of 'any entity can save the association', we assume that while storing the
     * Book, its Author should already be present in DB.
     */
    private User author;

    public void putProperty(String name, Object value){
        properties.put(name, value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
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

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void addChapter(Chapter chapter){
        chapters.add(chapter);
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public BookCover getCover() {
        return cover;
    }

    public void setCover(BookCover cover) {
        this.cover = cover;
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
