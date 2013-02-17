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

}
