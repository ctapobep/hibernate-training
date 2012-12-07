package org.javatalks.training.hibernate.entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;

/** @author stanislav bashkirtsev */
@Embeddable
@Table(name = "chapter")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chapter chapter = (Chapter) o;

        if (pageCount != chapter.pageCount) return false;
        if (!name.equals(chapter.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + pageCount;
        return result;
    }
}
