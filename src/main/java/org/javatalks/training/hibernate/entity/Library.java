package org.javatalks.training.hibernate.entity;

import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/** @author stanislav bashkirtsev */
@Entity
public class Library {
    private Long id;
    private String name;
    private User owner;

    @Id
    @GeneratedValue(generator = "libraryIdGenerator")
    @org.hibernate.annotations.GenericGenerator(
            name = "libraryIdGenerator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "owner")
    )
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}
