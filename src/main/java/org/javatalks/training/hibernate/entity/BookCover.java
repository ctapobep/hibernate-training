package org.javatalks.training.hibernate.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/** @author stanislav bashkirtsev */
public class BookCover {
    private String color;
    private Boolean hard;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isHard() {
        return hard;
    }

    public void setHard(Boolean hard) {
        this.hard = hard;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
