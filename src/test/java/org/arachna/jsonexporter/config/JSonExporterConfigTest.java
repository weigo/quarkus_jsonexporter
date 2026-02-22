package org.arachna.jsonexporter.config;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class JSonExporterConfigTest {
    @Inject
    JSonExporterConfig config;

    @Test
    void parseConfigWithModule() {
        assertThat(config, not(nullValue()));
    }
}
