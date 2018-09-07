package com.kahramani.p2p.infrastructure.config.rest;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class VertxRestManager extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(VertxRestManager.class);
    private static final String HTTP_CONTENT_TYPE_JSON = "application/json";
    private static final String API_V1_PREFIX = "/api/v1";

    private HttpServer server;
    private final int port;
    private final DatabaseSessionManager<ConnectionSource> databaseSessionManager;

    public VertxRestManager(int port, DatabaseSessionManager<ConnectionSource> databaseSessionManager) {
        this.port = port;
        this.databaseSessionManager = databaseSessionManager;
    }

    @Override
    public void start() throws SQLException {
        HttpServerOptions serverOptions = new HttpServerOptions().setPort(this.port);
        this.server = vertx.createHttpServer(serverOptions);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());


        this.server.requestHandler(router::accept).listen(result -> {
            if (result.succeeded()) {
                logger.info("Http server started listening on port: {}", this.port);
            } else {
                logger.error("Http server couldn't be started on port: {}", this.port, result.cause());
            }
        });
    }

    @Override
    public void stop() throws IOException {
        logger.info("Http server is about to stop listening on port: {}", this.port);
        this.databaseSessionManager.stop();
        this.server.close();
    }


}