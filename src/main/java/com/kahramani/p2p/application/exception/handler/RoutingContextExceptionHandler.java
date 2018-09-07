package com.kahramani.p2p.application.exception.handler;

import com.kahramani.p2p.application.exception.*;
import com.kahramani.p2p.application.exception.enums.ErrorResponseSet;
import com.kahramani.p2p.application.model.response.ErrorResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

import static com.kahramani.p2p.application.exception.enums.ErrorResponseSet.*;

public class RoutingContextExceptionHandler implements ExceptionHandler<RoutingContext> {

    @Override
    public void handle(Runnable runnable, RoutingContext event) {
        ErrorResponseSet error = null;
        try {
            runnable.run();
        } catch (DecodeException e) {
            error = REQUEST_NOT_DECODED;
        } catch (ValidationException e) {
            error = e.getErrorResponseSet();
        } catch (AccountNotFoundException e) {
            error = ACCOUNT_NOT_FOUND;
        } catch (UserNotFoundException e) {
            error = USER_NOT_FOUND;
        } catch (TransferNotFoundException e) {
            error = TRANSFER_NOT_FOUND;
        } catch (InsufficientBalanceException e) {
            error = INSUFFICIENT_BALANCE;
        } catch (CircularTransferException e) {
            error = CIRCULAR_TRANSFER;
        } catch (DatabaseException e) {
            error = DATABASE_ERROR;
        } catch (Exception e) {
            error = SYSTEM_ERROR;
        }

        // if error present, prepare appropriate response
        Optional.ofNullable(error)
                .ifPresent(errorResponseSet -> event.response()
                        .setStatusCode(errorResponseSet.getHttpResponseCode())
                        .end(Json.encodePrettily(new ErrorResponse(errorResponseSet))));
    }
}
