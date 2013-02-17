package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @Id
    @GeneratedValue
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
