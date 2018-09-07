package com.kahramani.p2p.infrastructure.config.db.session;

import java.io.IOException;

public interface DatabaseSessionManager<T> {

    String H2DB_JDBC_URL = "jdbc:h2:mem:transferdb;INIT=RUNSCRIPT FROM 'classpath:scripts/init.sql'";

    T getConnectionSource();

    void stop() throws IOException;
}