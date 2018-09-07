package com.kahramani.p2p.application.controller;

import com.kahramani.p2p.application.model.response.TransferRetrieveResponse;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface TransferRetrieveController extends Handler<RoutingContext> {

    TransferRetrieveResponse retrieve(String transferReference);
}