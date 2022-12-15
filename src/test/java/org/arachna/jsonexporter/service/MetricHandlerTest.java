package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.arachna.jsonexporter.api.ScrapeType;
import org.arachna.jsonexporter.api.ValueType;
import org.arachna.jsonexporter.registry.MetricsRegistryImpl;
import org.arachna.jsonexporter.registry.TextFormat004Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

class MetricHandlerTest {
    MetricHandler handler;
    Configuration configuration;
    MetricsRegistryImpl registry;

    @BeforeEach
    public void setUp() {
        registry = new MetricsRegistryImpl();
    }

    @Test
    public void testSimpleMetricHandler() throws IOException {
        MetricImpl metric = new MetricImpl("health", "$[?(@.status==\"UP\")]", "Health Status", ScrapeType.object, ValueType.gauge);
        metric.addLabelSpec("x", "y");
        metric.addValueSpec("value", "1");
        handler = new MetricHandler(metric);
        handler.createMetrics(registry, readDocument("/simpleHealth.json"));
        System.err.println(getOpenMetricsFormat());
    }

    @Test
    public void testNestedHandler() throws IOException {
        MetricImpl metric = new MetricImpl("health", "$[?(@.checks)].checks[?(@.id==\"services\")].checks", "Health Status", ScrapeType.object, ValueType.gauge);
        metric.addLabelSpec("service", "$[?(@.id)][0].id");
        metric.addValueSpec("value", "1");
        handler = new MetricHandler(metric);
        handler.createMetrics(registry, readDocument("/nestedHealth.json"));
        System.err.println(getOpenMetricsFormat());
    }

    private Object parse(String json) {
        return configuration.jsonProvider().parse(json);
    }

    Object readDocument(String resource) throws IOException {
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(resource));
        StringWriter writer = new StringWriter();
        reader.transferTo(writer);
        configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
        return parse(writer.toString());
    }

    String getOpenMetricsFormat() {
        // print collected metrics into string and return
        StringWriter result = new StringWriter();

        try {
            new TextFormat004Writer(result).write(registry.samples());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }
}