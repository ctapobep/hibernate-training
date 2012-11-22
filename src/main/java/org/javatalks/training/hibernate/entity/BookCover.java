package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class BookCover {
    private String color;
    private boolean hard;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }
}
