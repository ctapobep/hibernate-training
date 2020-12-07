package org.javatalks.training.hibernate.dao

import org.hibernate.HibernateException
import org.javatalks.training.hibernate.entity.bag.Book
import org.javatalks.training.hibernate.entity.bag.Chapter
import org.javatalks.training.hibernate.entity.bag.Comment
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import javax.persistence.PersistenceException

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@Transactional
class ListTest {

    @Test
    void "List demonstration"() {
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book original = new Book(title: "with comments", comments: comments)
        bookDao.save(original).flushAndClearSession()

        Book fromDb = bookDao.get(original.id)
        assertReflectionEquals(fromDb, original)
    }

    @Test
    void 'when merging, if collections differ, elements are removed from managed entity'() {
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book original = new Book(title: "with comments", comments: comments)
        bookDao.save(original).flushAndClearSession()

        //merging a book with the same ID but with different number of comments
        Book merged = bookDao.merge(new Book(id: original.id, title: 'after merge', comments: [comments[0]]))
        assert merged.id == original.id
        assert merged.comments.size() == original.comments.size() - 1
    }


    @Test
    void 'when updating, if orphan collection is dereferenced, exception thrown'() {
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book original = new Book(title: "with comments", comments: comments)
        bookDao.save(original).flushSession()

        try {
            original.comments = [new Comment(body: 'new')]//it's not allowed to replace collection with cascade=orphan
            bookDao.saveOrUpdate(original).flushSession()
            assert false
        } catch (PersistenceException e) {
            assert e.message.startsWith('org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance')
        }
    }

    @Test
    void 'when merging, if new collection is null, exception thrown'() {
        List<Comment> comments = [new Comment(body: "a"), new Comment(body: "b")]
        Book original = new Book(title: "with comments", comments: comments)
        bookDao.save(original).flushAndClearSession()

        try {
            bookDao.merge(new Book(id: original.id, comments: null))//exception only due to null
            bookDao.flushSession()
            assert false
        } catch (PersistenceException e) {
            assert e.message.startsWith('org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance')
        }
    }

    @Autowired
    BookDao bookDao;
}
