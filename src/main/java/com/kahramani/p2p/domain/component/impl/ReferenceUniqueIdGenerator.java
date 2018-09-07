package com.kahramani.p2p.domain.component.impl;

import com.kahramani.p2p.domain.component.UniqueIdGenerator;

import java.util.Random;
import java.util.stream.IntStream;

public class ReferenceUniqueIdGenerator implements UniqueIdGenerator {

    private static final char[] ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    @Override
    public String generateId() {
        Random random = new Random(System.nanoTime());
        return IntStream.range(0, REFERENCE_ID_LENGTH)
                .map(i -> ALPHANUMERIC_CHARS[random.nextInt(ALPHANUMERIC_CHARS.length)])
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}