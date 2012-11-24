package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Author
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Comment
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
class CollectionsTest {
    @Test
    void "List demonstration"(){
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book book = new Book(comments: comments)
        bookDao.save(book)
    }

    @Test
    void "Bag demonstration"(){
        List<Author> authors = [new Author(name: "a1"), new Author(name: "a2")]
        Book book = new Book(authors: authors)
        bookDao.save(book)
    }

    @Autowired BookDao bookDao;
}
