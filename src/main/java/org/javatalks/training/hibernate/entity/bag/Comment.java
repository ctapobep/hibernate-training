package org.javatalks.training.hibernate.entity.bag;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/** @author stanislav bashkirtsev */
@Entity
@Table(name = "comment")
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

    public void setBody(String body) {
        this.body = body;
    }
}
