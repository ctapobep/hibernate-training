package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "library_owner")
public class LibraryOwner {
    private long id;
    private Library library;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @ManyToOne
    @ForeignKey(name = "owner_library_fk")
    @JoinColumn(name = "library_id")
    public Library getLibrary() {
        return library;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public static List<LibraryOwner> create(int amount){
        List<LibraryOwner> owners = new ArrayList<>();
        for(int i = 0; i< amount; i++){
            owners.add(new LibraryOwner());
        }
        return owners;
    }
}
