package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "users")
public class User {
    private Long id;
    private String username;
    private String biography;
    private Date birthday;
    private byte[] avatar;
    private int age;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBiography() {
        return biography;
    }

    public Date getBirthday() {
        return birthday;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public int getAge() {
        return age;
    }
}
