package com.kahramani.p2p.domain.component;

public interface UniqueIdGenerator {

    int REFERENCE_ID_LENGTH = 15;

    String generateId();
}