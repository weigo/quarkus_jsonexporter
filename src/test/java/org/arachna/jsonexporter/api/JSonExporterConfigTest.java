package org.arachna.jsonexporter.api;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class JSonExporterConfigTest {
    @Inject
    JSonExporterConfig config;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseConfigWithModule() {
        assertThat(config, not(nullValue()));
    }
}
