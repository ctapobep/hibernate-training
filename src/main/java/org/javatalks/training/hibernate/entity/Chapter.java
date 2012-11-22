package org.javatalks.training.hibernate.entity;

/** @author stanislav bashkirtsev */
public class Chapter {
    private String name;
    private int pageCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
