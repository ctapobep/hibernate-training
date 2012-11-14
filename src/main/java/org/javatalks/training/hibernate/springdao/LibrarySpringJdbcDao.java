package org.javatalks.training.hibernate.springdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Library;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author stanislav bashkirtsev */
public class LibrarySpringJdbcDao implements Crud<Library> {
    public LibrarySpringJdbcDao(UserSpringJdbcDao userDao, JdbcTemplate jdbcTemplate) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
        insertTemplate = new SimpleJdbcInsert(jdbcTemplate);
        insertTemplate.setTableName("library");
        insertTemplate.setGeneratedKeyName("id");
    }

    @Override
    public void saveOrUpdate(Library entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    private void insert(Library library) {
        logger.info("Creating library with name [{}]", library.getName());
        cascadeSaveOrUpdateToOwner(library.getOwner());
        Map<String, Object> insertArgs = new HashMap<>();
        insertArgs.put("name", library.getName());
        insertArgs.put("owner_id", library.getOwnerId());
        Number libraryId = insertTemplate.executeAndReturnKey(insertArgs);
        library.setId((Long) libraryId);
    }

    private void update(Library library) {
        logger.info("Updating library [{}]", library.getName());
        cascadeSaveOrUpdateToOwner(library.getOwner());
        int affectedRows = jdbcTemplate.update(UPDATE, library.getName(), library.getOwnerId(), library.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Updating library failed, no rows affected.");
        }
    }


    @Override
    public Library get(long id) {
        List<Library> libs = jdbcTemplate.query(SELECT, new Object[]{id}, RowMappers.libraryMapper(userDao));
        return libs.isEmpty() ? null : libs.get(0);
    }

    @Override
    public void delete(Library entity) {
        logger.info("Deleting library [{}]", entity.getName());
        int affectedRows = jdbcTemplate.update(DELETE, entity.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Deleting library failed, no rows affected.");
        }
    }


    /**
     * If it was a newly crated User, it will be stored in DB, if not - it will be updated. We have to execute this
     * UPDATE statement because we don't know actually whether the object was changed or not. Alternatively we would
     * need to select properties and compare with what we have now. Third and best option would be to introduce
     * versioning and execute update with {@code ... where version= :current_version}, but for the sake of simplicity
     * let's stop on updating entity every time.
     *
     * @param owner an owner of the library to be stored in DB and generate its ID if it's a brand new object or to be
     *              update if it was changed
     */
    private void cascadeSaveOrUpdateToOwner(User owner) {
        if (owner != null) {
            userDao.saveOrUpdate(owner);
        }
    }

    private final UserSpringJdbcDao userDao;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String UPDATE = "update library set name = ?, owner_id = ? where id = ?";
    private static final String DELETE = "delete from library where id = ?";
    private static final String SELECT = "select * from library where id = ?";
}
