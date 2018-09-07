package com.kahramani.p2p.infrastructure.config.rest;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.application.exception.handler.ExceptionHandler;
import com.kahramani.p2p.application.exception.handler.RoutingContextExceptionHandler;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.validator.RequestValidator;
import com.kahramani.p2p.application.validator.TransferCreationRequestValidator;
import com.kahramani.p2p.domain.component.UniqueIdGenerator;
import com.kahramani.p2p.domain.component.impl.IncrementalUniqueIdGenerator;
import com.kahramani.p2p.domain.component.impl.ReferenceUniqueIdGenerator;
import com.kahramani.p2p.domain.repository.TransferJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbAccountJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbTransferJpaRepository;
import com.kahramani.p2p.domain.repository.h2.H2DbUserJpaRepository;
import com.kahramani.p2p.domain.service.account.AccountService;
import com.kahramani.p2p.domain.service.account.ProxyAccountService;
import com.kahramani.p2p.domain.service.transfer.*;
import com.kahramani.p2p.domain.service.user.ProxyUserService;
import com.kahramani.p2p.domain.service.user.UserService;
import com.kahramani.p2p.infrastructure.adapter.rate.ExchangeRateClient;
import com.kahramani.p2p.infrastructure.adapter.rate.GoogleExchangeRateClient;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import com.kahramani.p2p.infrastructure.rest.RestTransferCreationController;
import com.kahramani.p2p.infrastructure.rest.RestTransferRetrieveController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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

        // create global beans
        ExceptionHandler<RoutingContext> exceptionHandler = new RoutingContextExceptionHandler();
        ConversionUnitService conversionUnitService = new TransferConversionUnitService();
        TransferJpaRepository transferJpaRepository = new H2DbTransferJpaRepository(databaseSessionManager);
        AccountService proxyAccountService = new ProxyAccountService(new H2DbAccountJpaRepository(databaseSessionManager));
        RequestValidator<TransferCreationRequest> requestValidator = new TransferCreationRequestValidator();
        ExchangeRateClient exchangeRateClient = new GoogleExchangeRateClient();

        UserService proxyUserService = new ProxyUserService(new H2DbUserJpaRepository(databaseSessionManager));
        UniqueIdGenerator incrementalUniqueIdGenerator = new IncrementalUniqueIdGenerator();
        UniqueIdGenerator referenceUniqueIdGenerator = new ReferenceUniqueIdGenerator();

        TransferCreationService transferCreationService = new TransactionalTransferCreationService(proxyAccountService, exchangeRateClient,
                proxyUserService, transferJpaRepository, incrementalUniqueIdGenerator, referenceUniqueIdGenerator, conversionUnitService);
        TransferRetrieveService transferRetrieveService = new TransferRetrieveService(transferJpaRepository, proxyAccountService, conversionUnitService);

        // prepare routes
        routeTransferCreation(router, exceptionHandler, requestValidator, transferCreationService);
        routeTransferRetrieve(router, exceptionHandler, transferRetrieveService);

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

    private void routeTransferCreation(Router router, ExceptionHandler<RoutingContext> exceptionHandler,
                                       RequestValidator<TransferCreationRequest> requestValidator,
                                       TransferCreationService transferCreationService) {

        router.post(API_V1_PREFIX + "/accounts/:accountReference/transfers")
                .consumes(HTTP_CONTENT_TYPE_JSON)
                .produces(HTTP_CONTENT_TYPE_JSON)
                .handler(new RestTransferCreationController(exceptionHandler, requestValidator, transferCreationService));
    }

    private void routeTransferRetrieve(Router router, ExceptionHandler<RoutingContext> exceptionHandler,
                                       TransferRetrieveService transferRetrieveService) {

        router.get(API_V1_PREFIX + "/transfers/:transferReference")
                .consumes(HTTP_CONTENT_TYPE_JSON)
                .produces(HTTP_CONTENT_TYPE_JSON)
                .handler(new RestTransferRetrieveController(exceptionHandler, transferRetrieveService));
    }
}