package org.javatalks.training.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author stanislav bashkirtsev */
@Entity
public class Comment {
    private long id;
    private String body;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public static List<Comment> randomComments(int amount){
        List<Comment> comments = new ArrayList<>(amount);
        for(int i = 0; i< amount; i++){
            Comment comment = new Comment();
            comment.setBody(UUID.randomUUID().toString());
            comments.add(comment);
        }
        return comments;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
