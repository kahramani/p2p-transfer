package com.kahramani.p2p.infrastructure.config.db.session;

import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;

public class H2DbSessionManager implements DatabaseSessionManager<ConnectionSource> {

    private static H2DbSessionManager INSTANCE;

    private final ConnectionSource connectionSource;

    private H2DbSessionManager() throws SQLException {
        this.connectionSource = new JdbcPooledConnectionSource(H2DB_JDBC_URL, new H2DatabaseType());
    }

    public static H2DbSessionManager getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new H2DbSessionManager();
        }
        return INSTANCE;
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return this.connectionSource;
    }

    @Override
    public void stop() throws IOException {
        if (getConnectionSource() != null) {
            getConnectionSource().close();
        }
    }
}