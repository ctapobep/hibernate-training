package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.BookWithFirstComment
import org.javatalks.training.hibernate.entity.Comment
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class FetchingTest {
    @Test
    void "try changing fetching properties for list"() {
        Book book = new Book(title: "try me out", comments: Comment.randomComments(500))
        dao.save(book).flushAndClearSession()

        book = dao.get(book.id) // now see the SQL logs and experiment lazy & fetch attributes
        println "[HIBERNATE TRAINING] Let's load something!"
        println "[HIBERNATE TRAINING] COLLECTION SIZE: " + book.comments.size()
        println "[HIBERNATE TRAINING] FIRST ELEMENT: " + book.comments.subList(0, 0)
    }

    @Test
    @Ignore("sql is wrong at the moment")
    void "fetch entity by custom sql"(){
        Book book = new Book(title: "i'm very custom 8-)", comments: Comment.randomComments(2))
        dao.save(book).flushAndClearSession()

        BookWithFirstComment bookWithFirstComment = dao.session().get(BookWithFirstComment.class, book.id) as BookWithFirstComment
        assert bookWithFirstComment.title == "i'm very custom 8-)"
        assert bookWithFirstComment.comment.body == book.comments[0].body
    }

    @Autowired BookDao dao;
}
