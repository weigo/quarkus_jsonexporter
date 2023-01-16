package org.arachna.jsonexporter.registry;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.micrometer.core.instrument.ImmutableTag;

/**
 * Unit tests for prometheus metrics format writer.
 */
class TextFormat004WriterTest {
    private TextFormat004Writer writer;

    private StringWriter result;

    private MetricsRegistry registry;

    @BeforeEach
    void setUp() {
        result = new StringWriter();
        writer = new TextFormat004Writer(result);
        registry = new MetricsRegistry();
    }

    @Test
    void writeCounterWithoutTags() throws IOException {
        CounterImpl counter = new CounterImpl("request_count", "Request count", Collections.emptyList());
        registry.register(counter);
        writer.write(registry.samples());

        assertThat(result.toString(), equalTo("""
            # HELP request_count Request count
            # TYPE request_count counter
            request_count 0.0
            """));
    }

    @Test
    void writeGaugeWithoutTags() throws IOException {
        GaugeImpl gauge = new GaugeImpl("health_status", "Health status", Collections.emptyList());
        registry.register(gauge);
        writer.write(registry.samples());

        assertThat(result.toString(), equalTo("""
            # HELP health_status Health status
            # TYPE health_status gauge
            health_status 0.0
            """));
    }

    @Test
    void writeSampleWithTags() throws IOException {
        CounterImpl counter = new CounterImpl("request_count", "Request count", List.of(new ImmutableTag("tag", "x")));
        registry.register(counter);
        writer.write(registry.samples());

        assertThat(result.toString(), equalTo("""
            # HELP request_count Request count
            # TYPE request_count counter
            request_count[tag="x"] 0.0
            """));
    }
}
