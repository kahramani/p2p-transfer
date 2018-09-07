package com.kahramani.p2p;

import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class InMemoryTestDbSessionManager implements DatabaseSessionManager<ConnectionSource> {

    private final ConnectionSource connectionSource;

    InMemoryTestDbSessionManager(String dbName) throws SQLException {
        String h2DB_JDBC_URL = "jdbc:h2:mem:%s;INIT=RUNSCRIPT FROM 'classpath:scripts/test-init.sql'";
        this.connectionSource = new JdbcPooledConnectionSource(String.format(h2DB_JDBC_URL, dbName), new H2DatabaseType());
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