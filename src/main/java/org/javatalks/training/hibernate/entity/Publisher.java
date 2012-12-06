package org.javatalks.training.hibernate.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Can't implement {@link org.javatalks.training.hibernate.dao.Crud} because uses composite key instead of long.
 *
 * @author stanislav bashkirtsev
 */
@Entity
public class Publisher {
    private Id id;

    @EmbeddedId
    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public static class Id implements Serializable {
        private String name;
        private String city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
