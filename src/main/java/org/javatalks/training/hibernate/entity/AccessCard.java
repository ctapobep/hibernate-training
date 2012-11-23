package org.javatalks.training.hibernate.entity;

/**
 * User can access the library if he has an access card which can grant permissions to several libraries at the same
 * time.
 *
 * @author stanislav bashkirtsev
 */
public class AccessCard {
    public enum Type {
        USUAL, GOLDEN
    }

    private long id;
    private String code;
    private Type type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
