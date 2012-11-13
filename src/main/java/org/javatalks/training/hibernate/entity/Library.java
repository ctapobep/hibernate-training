package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class Library {
    private Long id;
    private String name;
    private User owner;

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Returns the ID of the owner or null if the owner is null or she is not persisted yet.
     *
     * @return the ID of the owner or null if the owner is null or she is not persisted yet.
     */
    public Long getOwnerId() {
        return owner == null ? null : owner.getId();
    }
}
