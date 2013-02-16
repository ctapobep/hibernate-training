package org.javatalks.training.hibernate.entity.map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/** @author stanislav bashkirtsev */
@Entity
@javax.persistence.Table(name = "equipment")
public class Equipment {
    private long id;
    private String libraryName;
    private Map<String, Table> tables = new HashMap<>();
    private Map<Shelf, Stand> shelves = new HashMap<>();

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public Map<Shelf, Stand> getShelves() {
        return shelves;
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void setShelves(Map<Shelf, Stand> shelves) {
        this.shelves = shelves;
    }

    public void setTables(Map<String, Table> tables) {
        this.tables = tables;
    }
}
