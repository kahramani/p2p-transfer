package com.kahramani.p2p.domain.repository.h2;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.application.exception.DatabaseException;
import com.kahramani.p2p.domain.entity.Transfer;
import com.kahramani.p2p.domain.repository.TransferJpaRepository;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Callable;

public class H2DbTransferJpaRepository implements TransferJpaRepository {

    private static final Logger logger = LoggerFactory.getLogger(H2DbTransferJpaRepository.class);

    private static Dao<Transfer, Long> transferDao;
    private static TransactionManager transactionManager;

    public H2DbTransferJpaRepository(DatabaseSessionManager<ConnectionSource> databaseSessionManager) throws SQLException {
        ConnectionSource connectionSource = databaseSessionManager.getConnectionSource();
        transferDao = DaoManager.createDao(connectionSource, Transfer.class);
        transactionManager = new TransactionManager(connectionSource);
    }

    @Override
    public Transfer save(Transfer transfer) {
        try {
            transferDao.createOrUpdate(transfer);
            return transfer;
        } catch (SQLException e) {
            logger.error("Error occured while transfer on save {}.", transfer, e);
            throw new DatabaseException();
        }
    }

    @Override
    public Optional<Transfer> findById(Long transferId) {
        try {
            return Optional.ofNullable(transferDao.queryForId(transferId));
        } catch (SQLException e) {
            logger.error("Error occured while finding transfer by id {}.", transferId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Transfer> findByReference(String reference) {
        try {
            Transfer transfer = transferDao.queryBuilder()
                    .where().eq("reference", reference)
                    .queryForFirst();
            return Optional.ofNullable(transfer);
        } catch (SQLException e) {
            logger.error("Error occured while finding transfer by reference {}.", reference, e);
            return Optional.empty();
        }
    }

    @Override
    public synchronized Transfer processInTransaction(Callable<Transfer> callable) throws SQLException {
        return transactionManager.callInTransaction(callable);
    }
}