package org.javatalks.training.hibernate.mybatisdao;

import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.User;
import org.javatalks.training.hibernate.mybatisdao.mapper.UserMapper;

/**
 * MyBatis is a cool framework, but it has a limited set of features, so we would need to roll up our sleeves and write
 * some custom code. In some simple cases this DAO will simply delegate its work to the MyBatis' proxy.
 *
 * @author stanislav bashkirtsev
 */
public class UserMybatisDao implements UserMapper {
    public UserMybatisDao(UserMapper userMapper, BookMybatisDao bookDao) {
        this.bookDao = bookDao;
        this.userMapper = userMapper;
    }

    @Override
    public void insert(User entity) {
        userMapper.insert(entity);
        for (Book book : entity.getBooks()) { //might be a good candidate for batching
            bookDao.insert(book); //we don't use MyBatis foreach facility because it won't return book ID
        }
    }

    @Override
    public void update(User entity) {
        userMapper.update(entity);
        bookDao.insertOrUpdateBooks(entity.getBooks());
    }

    @Override
    public User getWithBooks(long id) {
        return userMapper.getWithBooks(id);
    }

    @Override
    public User get(long id) {
        return userMapper.get(id);
    }

    @Override
    public void delete(User entity) {
        userMapper.delete(entity);
    }

    private final BookMybatisDao bookDao;
    private final UserMapper userMapper;
}
