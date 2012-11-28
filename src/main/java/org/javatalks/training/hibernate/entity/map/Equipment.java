package org.javatalks.training.hibernate.entity.map;

import java.util.HashMap;
import java.util.Map;

/** @author stanislav bashkirtsev */
public class Equipment {
    private long id;
    private Map<String, Table> tables = new HashMap<>();
    private Map<Shelf, Stand> shelves = new HashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Shelf, Stand> getShelves() {
        return shelves;
    }

    public void setShelves(Map<Shelf, Stand> shelves) {
        this.shelves = shelves;
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public void setTables(Map<String, Table> tables) {
        this.tables = tables;
    }
}
