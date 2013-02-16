package org.javatalks.training.hibernate.entity.map;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
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

    @OneToMany
    @MapKey(name = "number")
    @JoinColumn(name = "equipment_id")
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "table_equipment_fk")
    public Map<String, Table> getTables() {
        return tables;
    }

    @OneToMany
    @MapKeyJoinColumn(name = "shelf_id")
    @JoinTable(name = "shelf_stand",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "stand_id", referencedColumnName = "id"))
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "stand_equipment_fk")
    public Map<Shelf, Stand> getShelves() {
        return shelves;
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
