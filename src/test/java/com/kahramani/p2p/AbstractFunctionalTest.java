package com.kahramani.p2p;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;

import java.sql.SQLException;

/**
 * Base abstract class for functional tests
 */
public abstract class AbstractFunctionalTest {

    protected static DatabaseSessionManager<ConnectionSource> initializeDatabaseSession(String dbName) throws SQLException {
        return new InMemoryTestDbSessionManager(dbName);
    }
}