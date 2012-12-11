package org.javatalks.training.hibernate.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author stanislav bashkirtsev */
public class User {
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static List<User> create(int amount) {
        List<User> users = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            User user = new User();
            user.setUsername(UUID.randomUUID().toString());
            users.add(user);
        }
        return users;
    }
}
