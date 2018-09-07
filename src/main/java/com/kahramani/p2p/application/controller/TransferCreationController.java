package com.kahramani.p2p.application.controller;

import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.model.response.TransferCreationResponse;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface TransferCreationController extends Handler<RoutingContext> {

    TransferCreationResponse create(String accountReference, TransferCreationRequest transferCreationRequest);
}