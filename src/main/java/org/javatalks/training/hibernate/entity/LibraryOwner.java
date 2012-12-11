package org.javatalks.training.hibernate.entity;

import java.util.ArrayList;
import java.util.List;

/** @author stanislav bashkirtsev */
public class LibraryOwner {
    private long id;
    private Library library;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Library getLibrary() {
        return library;
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
