package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class User {
    private long id;
    private String name;
    private String base64avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64avatar() {
        return base64avatar;
    }

    public void setBase64avatar(String base64avatar) {
        this.base64avatar = base64avatar;
    }
}
