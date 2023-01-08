package org.arachna.jsonexporter.service;

import java.io.IOException;
import javax.inject.Inject;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ObjectMetricHandlerTest extends AbstractMetricHandlerTest {
    @Inject
    ValueMapperFactory valueMapperFactory;

    @ParameterizedTest
    @CsvSource(value = {
        "nested_structured_as_several_metrics_using_object,nested_structured,/structuredHealth.json,/structuredHealthSingleMetricResult.metric" })
    void testObjectMetricHandler(String moduleName, String metricName, String jsonResourceName, String metricResourceName)
        throws IOException {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        ObjectMetricHandler handler = new ObjectMetricHandler(metric, valueMapperFactory);
        handler.collectMetrics(registry, readDocument(jsonResourceName));
        assertGeneratedMetric(metricResourceName);
    }

    @Test
    void objectMetricHandlerShouldNotInstantiateWithWrongHandlerType() {
        JSonExporterConfig.Module.Metric metric = getMetric("health", "health");

        // Make sure that ObjectMetricHandler cannot be instantiated with ScrapeType other than OBJECT
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ObjectMetricHandler(metric, valueMapperFactory);
        });
    }
}
