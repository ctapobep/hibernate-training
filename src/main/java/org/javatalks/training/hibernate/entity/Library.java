package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/** @author stanislav bashkirtsev */
@Entity
public class Library {
    private long id;
    private List<Book> books;
    private List<LibraryOwner> owners;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @OneToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    public List<Book> getBooks() {
        return books;
    }

    @OneToMany(mappedBy = "library")
    public List<LibraryOwner> getOwners() {
        return owners;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setOwners(List<LibraryOwner> owners) {
        this.owners = owners;
    }

    public static List<Library> create(int numberOfLibraries) {
        List<Library> libs = new ArrayList<>(numberOfLibraries);
        for (int i = 0; i < numberOfLibraries; i++) {
            Library library = new Library();
            library.setBooks(Book.createBooks(100));
            libs.add(library);
        }
        return libs;
    }

    public static Library createWithBook(int numberOfBooks) {
        Library library = new Library();
        library.setBooks(Book.createBooks(numberOfBooks));
        return library;
    }

    public String toString() {
        return id + "";
    }
}
