package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "book")
public class Book {
    private Long id;
    private String title;
    private BookCover cover;
    private List<Chapter> chapters;
    private Map<String, Object> properties = new HashMap<>();

    public void putProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @ElementCollection
    @CollectionTable(name = "chapter", joinColumns = @JoinColumn(name = "book_id"))
    @ForeignKey(name = "chapter_book_fk")
    @OrderColumn(name = "order_in_book")
    public List<Chapter> getChapters() {
        return chapters;
    }

    @Embedded
    public BookCover getCover() {
        return cover;
    }

    @Transient//no way to map dynamic components via annotations
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
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
