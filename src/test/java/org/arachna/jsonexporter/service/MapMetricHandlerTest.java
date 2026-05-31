package org.arachna.jsonexporter.service;

import java.io.IOException;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class MapMetricHandlerTest extends AbstractMetricHandlerTest {
    @Inject
    ValueMapperFactory valueMapperFactory;

    @ParameterizedTest
    @CsvSource(value = {
        "map_entries_mapper,health_status,/mapBasedHealthMetrics.json,/mapBasedHealthMetrics.metric" })
    void testMapMetricHandler(String moduleName, String metricName, String jsonResourceName, String metricResourceName)
        throws IOException {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        MapMetricHandler handler = new MapMetricHandler(metric, valueMapperFactory);
        handler.collectMetrics(registry, readDocument(jsonResourceName));
        assertGeneratedMetric(metricResourceName);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "map_entries_mapper_with_additional_label,health_status,/mapBasedHealthMetrics.json,/mapBasedHealthMetricsWithLabel.metric" })
    void testMapMetricHandlerWithAdditionalLabel(String moduleName, String metricName, String jsonResourceName, String metricResourceName)
        throws IOException {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        MapMetricHandler handler = new MapMetricHandler(metric, valueMapperFactory);
        handler.collectMetrics(registry, readDocument(jsonResourceName));
        assertGeneratedMetric(metricResourceName);
    }

    @Test
    void mapMetricHandlerShouldNotInstantiateWithWrongHandlerType() {
        JSonExporterConfig.Module.Metric metric = getMetric("health", "health");

        // Make sure that MapMetricHandler cannot be instantiated with ScrapeType other than MAP
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ObjectMetricHandler(metric, valueMapperFactory);
        });
    }
}
