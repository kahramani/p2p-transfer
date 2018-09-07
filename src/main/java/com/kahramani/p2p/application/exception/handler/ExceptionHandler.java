package com.kahramani.p2p.application.exception.handler;

public interface ExceptionHandler<T> {

    void handle(Runnable runnable, T t);
}