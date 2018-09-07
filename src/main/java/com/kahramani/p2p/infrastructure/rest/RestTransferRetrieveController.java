package com.kahramani.p2p.infrastructure.rest;

import com.kahramani.p2p.application.controller.TransferRetrieveController;
import com.kahramani.p2p.application.exception.handler.ExceptionHandler;
import com.kahramani.p2p.application.model.response.TransferRetrieveResponse;
import com.kahramani.p2p.domain.service.transfer.TransferRetrieveService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class RestTransferRetrieveController implements TransferRetrieveController {

    private static final Logger logger = LoggerFactory.getLogger(RestTransferRetrieveController.class);

    private final ExceptionHandler<RoutingContext> exceptionHandler;
    private final TransferRetrieveService transferRetrieveService;

    public RestTransferRetrieveController(ExceptionHandler<RoutingContext> exceptionHandler,
                                          TransferRetrieveService transferRetrieveService) {
        this.exceptionHandler = exceptionHandler;
        this.transferRetrieveService = transferRetrieveService;
    }

    @Override
    public TransferRetrieveResponse retrieve(String transferReference) {
        ZonedDateTime startTime = ZonedDateTime.now();
        logger.info("Transfer retrieve started. Reference: {}", transferReference); // request contains no sensitive data so logging shouldn't be a problem

        TransferRetrieveResponse transferRetrieveResponse = transferRetrieveService.retrieveByReference(transferReference);

        logger.info("Transfer retrieve ended. Duration: {} in millis. Response: {}",
                ChronoUnit.MILLIS.between(startTime, ZonedDateTime.now()), transferRetrieveResponse);
        return transferRetrieveResponse;
    }

    @Override
    public void handle(RoutingContext event) {
        Runnable runnable = () -> {
            String transferReference = event.request().getParam("transferReference");
            TransferRetrieveResponse transferRetrieveResponse = retrieve(transferReference);
            event.response()
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .end(Json.encodePrettily(transferRetrieveResponse));
        };

        exceptionHandler.handle(runnable, event);
    }
}