package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Comment
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author stanislav bashkirtsev
 */
class FetchingTest {
    @Test
    void "try changing fetching properties for list"() {
        Book book = new Book(title: "try me out", comments: Comment.randomComments(500))
        dao.save(book).flushAndClearSession()

        book = dao.get(book.id)
        dao.flushAndClearSession()
        book.comments
    }

    @Autowired BookDao dao;
}
