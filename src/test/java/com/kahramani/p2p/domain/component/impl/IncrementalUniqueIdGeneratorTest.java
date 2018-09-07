package com.kahramani.p2p.domain.component.impl;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncrementalUniqueIdGeneratorTest {

    private IncrementalUniqueIdGenerator incrementalUniqueIdGenerator;

    @Before
    public void setUp() {
        incrementalUniqueIdGenerator = new IncrementalUniqueIdGenerator();
    }

    @Test
    public void should_generate_id_incremental() {
        assertThat(incrementalUniqueIdGenerator.generateId()).isEqualTo("1");
        assertThat(incrementalUniqueIdGenerator.generateId()).isEqualTo("2");
        assertThat(incrementalUniqueIdGenerator.generateId()).isEqualTo("3");
    }
}