package org.javatalks.training.hibernate.entity.bag;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "appendix")
public class Appendix {
    private long id;
    private String name;
    private Book book;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @ManyToOne(optional = false)
    @JoinTable(name = "book_appendix",
            joinColumns = @JoinColumn(name = "appendix_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"),
            uniqueConstraints = @UniqueConstraint(name = "appendix_uk", columnNames = "appendix_id"))
    public Book getBook() {
        return book;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appendix appendix = (Appendix) o;

        if (name != null ? !name.equals(appendix.name) : appendix.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
