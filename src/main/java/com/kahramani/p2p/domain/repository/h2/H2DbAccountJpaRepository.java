package com.kahramani.p2p.domain.repository.h2;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.application.exception.DatabaseException;
import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.repository.AccountJpaRepository;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class H2DbAccountJpaRepository implements AccountJpaRepository {

    private static final Logger logger = LoggerFactory.getLogger(H2DbAccountJpaRepository.class);

    private static Dao<Account, Long> accountDao;

    public H2DbAccountJpaRepository(DatabaseSessionManager<ConnectionSource> databaseSessionManager) throws SQLException {
        accountDao = DaoManager.createDao(databaseSessionManager.getConnectionSource(), Account.class);
    }

    @Override
    public Account save(Account account) {
        try {
            accountDao.createOrUpdate(account);
            return account;
        } catch (SQLException e) {
            logger.error("Error occured while account on save {}.", account, e);
            throw new DatabaseException();
        }
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        try {
            return Optional.ofNullable(accountDao.queryForId(accountId));
        } catch (SQLException e) {
            logger.error("Error occured while finding account by id {}.", accountId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> findByReference(String reference) {
        try {
            Account account = accountDao.queryBuilder()
                    .where().eq("reference", reference)
                    .queryForFirst();
            return Optional.ofNullable(account);
        } catch (SQLException e) {
            logger.error("Error occured while finding account by reference {}.", reference, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> findByUserIdAndCurrency(Long userId, String currency) {
        try {
            Account account = accountDao.queryBuilder()
                    .where()
                    .eq("user_id", userId)
                    .and()
                    .eq("currency", currency)
                    .queryForFirst();
            return Optional.ofNullable(account);
        } catch (SQLException e) {
            logger.error("Error occured while finding account by userId {} and currency {}.", userId, currency, e);
            return Optional.empty();
        }
    }
}