package com.kahramani.p2p.domain.repository.h2;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.application.exception.DatabaseException;
import com.kahramani.p2p.domain.entity.User;
import com.kahramani.p2p.domain.repository.UserJpaRepository;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class H2DbUserJpaRepository implements UserJpaRepository {

    private static final Logger logger = LoggerFactory.getLogger(H2DbUserJpaRepository.class);

    private static Dao<User, Long> userDao;

    public H2DbUserJpaRepository(DatabaseSessionManager<ConnectionSource> databaseSessionManager) throws SQLException {
        userDao = DaoManager.createDao(databaseSessionManager.getConnectionSource(), User.class);
    }

    @Override
    public User save(User user) {
        try {
            userDao.createOrUpdate(user);
            return user;
        } catch (SQLException e) {
            logger.error("Error occured while user on save {}.", user, e);
            throw new DatabaseException();
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        try {
            return Optional.ofNullable(userDao.queryForId(userId));
        } catch (SQLException e) {
            logger.error("Error occured while finding user by id {}.", userId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByReference(String reference) {
        try {
            User user = userDao.queryBuilder()
                    .where().eq("reference", reference)
                    .queryForFirst();
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            logger.error("Error occured while finding user by reference {}.", reference, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) {
        try {
            User user = userDao.queryBuilder()
                    .where().eq("mobile_number", mobileNumber)
                    .queryForFirst();
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            logger.error("Error occured while finding user by mobileNumber {}.", mobileNumber, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = userDao.queryBuilder()
                    .where().eq("user_name", username)
                    .queryForFirst();
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            logger.error("Error occured while finding user by username {}.", username, e);
            return Optional.empty();
        }
    }
}