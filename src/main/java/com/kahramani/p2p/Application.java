package com.kahramani.p2p;

import com.kahramani.p2p.infrastructure.config.db.session.H2DbSessionManager;
import com.kahramani.p2p.infrastructure.config.rest.VertxRestManager;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final int APPLICATION_DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        try {
            int port;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            } else {
                port = APPLICATION_DEFAULT_PORT;
            }

            // deploy verticle
            VertxRestManager vertxRestManager = new VertxRestManager(port, H2DbSessionManager.getInstance());
            DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("port", port));
            Vertx.vertx().deployVerticle(vertxRestManager, options);
        } catch (Exception e) {
            logger.error("Application couldn't be started.", e);
        }
    }
}