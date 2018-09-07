package com.kahramani.p2p.infrastructure.rest;

import com.kahramani.p2p.application.controller.TransferCreationController;
import com.kahramani.p2p.application.exception.handler.ExceptionHandler;
import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.model.response.TransferCreationResponse;
import com.kahramani.p2p.application.validator.RequestValidator;
import com.kahramani.p2p.domain.service.transfer.TransferCreationService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class RestTransferCreationController implements TransferCreationController {

    private static final Logger logger = LoggerFactory.getLogger(RestTransferCreationController.class);

    private final ExceptionHandler<RoutingContext> exceptionHandler;
    private final RequestValidator<TransferCreationRequest> requestValidator;
    private final TransferCreationService transferCreationService;

    public RestTransferCreationController(ExceptionHandler<RoutingContext> exceptionHandler,
                                          RequestValidator<TransferCreationRequest> requestValidator,
                                          TransferCreationService transferCreationService) {
        this.exceptionHandler = exceptionHandler;
        this.requestValidator = requestValidator;
        this.transferCreationService = transferCreationService;
    }

    @Override
    public TransferCreationResponse create(String accountReference, TransferCreationRequest transferCreationRequest) {
        ZonedDateTime startTime = ZonedDateTime.now();
        logger.info("Transfer creation started. Account: {}, TransferCreationRequest: {}", accountReference, transferCreationRequest); // request contains no sensitive data so logging shouldn't be a problem

        requestValidator.validate(transferCreationRequest);
        TransferCreationResponse transferCreationResponse = transferCreationService.create(accountReference, transferCreationRequest);

        logger.info("Transfer creation ended. Duration: {} in millis. Response: {}",
                ChronoUnit.MILLIS.between(startTime, ZonedDateTime.now()), transferCreationResponse);
        return transferCreationResponse;
    }

    @Override
    public void handle(RoutingContext event) {
        Runnable runnable = () -> {
            String accountReference = event.request().getParam("accountReference");
            TransferCreationRequest transferCreationRequest = Json.decodeValue(event.getBodyAsString(), TransferCreationRequest.class);
            TransferCreationResponse transferCreationResponse = create(accountReference, transferCreationRequest);
            event.response()
                    .setStatusCode(HttpResponseStatus.CREATED.code())
                    .end(Json.encodePrettily(transferCreationResponse));
        };

        exceptionHandler.handle(runnable, event);
    }
}