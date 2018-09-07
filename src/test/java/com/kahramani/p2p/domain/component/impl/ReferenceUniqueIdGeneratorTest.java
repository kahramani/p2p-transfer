package com.kahramani.p2p.domain.component.impl;

import org.junit.Before;
import org.junit.Test;

import static com.kahramani.p2p.domain.component.UniqueIdGenerator.REFERENCE_ID_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceUniqueIdGeneratorTest {

    private ReferenceUniqueIdGenerator referenceUniqueIdGenerator;

    @Before
    public void setUp() {
        referenceUniqueIdGenerator = new ReferenceUniqueIdGenerator();
    }

    @Test
    public void should_generate_reference_id_with_specific_length() {
        String id = referenceUniqueIdGenerator.generateId();

        assertThat(id)
                .isNotNull()
                .hasSize(REFERENCE_ID_LENGTH);
    }
}