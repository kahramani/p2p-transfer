package com.kahramani.p2p;

import com.kahramani.p2p.infrastructure.config.rest.VertxRestManager;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.runner.RunWith;

import java.sql.SQLException;

/**
 * Base abstract class for integration tests which hit an endpoint with a http request
 */
@RunWith(VertxUnitRunner.class)
public abstract class AbstractRestIntegrationTest {

    protected static AbstractVerticle deploy(TestContext context, int port, String dbName) throws SQLException {
        // deploy the verticle
        VertxRestManager vertxRestManager = new VertxRestManager(port, new InMemoryTestDbSessionManager(dbName));
        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("port", port));
        Vertx.vertx().deployVerticle(vertxRestManager, options, context.asyncAssertSuccess());

        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
        return vertxRestManager;
    }

    protected static void tearDown(AbstractVerticle verticle) throws Exception {
        verticle.stop();
    }
}