package org.javatalks.training.hibernate.entity.bag;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "chapter")
public class Chapter {
    private long id;
    private Book book;
    private String name;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "book_id_fk"))
    public Book getBook() {
        return book;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setName(String name) {
        this.name = name;
    }
}
