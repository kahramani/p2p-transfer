package com.kahramani.p2p.domain.component.impl;

import com.kahramani.p2p.domain.component.UniqueIdGenerator;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementalUniqueIdGenerator implements UniqueIdGenerator {

    private final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public String generateId() {
        return Integer.toString(COUNTER.incrementAndGet());
    }
}