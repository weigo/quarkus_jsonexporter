package org.arachna.jsonexporter.service;

import java.io.IOException;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.quarkus.test.junit.QuarkusTest;

/**
 * @author weigo
 */
@QuarkusTest
public class ValueMetricHandlerTest extends AbstractMetricHandlerTest {
    @ParameterizedTest
    @CsvSource(value = { "default,simple_value,/simpleValue.json,/simpleValue.metric", })
    public void testValueMetricHandler(String moduleName, String metricName, String jsonResourceName, String expectedMetricResource)
        throws IOException {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        ValueMetricHandler handler = new ValueMetricHandler(metric);
        handler.collectMetrics(registry, readDocument(jsonResourceName));
        assertGeneratedMetric(expectedMetricResource);
    }

    @ParameterizedTest
    @CsvSource(value = { "health,health,/simpleHealth.json" })
    public void testValueMetricHandlerReportsFailedValueExtractionError(String moduleName, String metricName, String jsonResourceName) {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        ValueMetricHandler handler = new ValueMetricHandler(metric);

        Assertions.assertThrows(IllegalStateException.class, () -> handler.collectMetrics(registry, readDocument(jsonResourceName)));
    }
}
