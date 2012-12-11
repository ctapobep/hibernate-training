package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author stanislav bashkirtsev */
@Entity
public class Book {
    private long id;
    private String name;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Book> createBooks(int number) {
        List<Book> books = new ArrayList<>(number);
        for(int i = 0; i< number; i++){
            Book book = new Book();
            book.setName(UUID.randomUUID().toString());
            books.add(book);
        }
        return books;
    }
}
