package org.javatalks.training.hibernate.entity.map;

/** @author stanislav bashkirtsev */
public class Shelf {
    private long id;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!Shelf.class.isInstance(o)) return false;

        Shelf shelf = (Shelf) o;

        if (number != null ? !number.equals(shelf.getNumber()) : shelf.getNumber() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}
