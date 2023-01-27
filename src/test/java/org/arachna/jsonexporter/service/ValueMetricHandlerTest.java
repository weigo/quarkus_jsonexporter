package org.arachna.jsonexporter.service;

import java.io.IOException;
import javax.inject.Inject;

import org.arachna.jsonexporter.api.JsonExporterException;
import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.quarkus.test.junit.QuarkusTest;

/**
 * @author weigo
 */
@QuarkusTest
class ValueMetricHandlerTest extends AbstractMetricHandlerTest {
    @Inject
    ValueMapperFactory valueMapperFactory;

    @ParameterizedTest
    @CsvSource(value = { "default,simple_value,/simpleValue.json,/simpleValue.metric",
        "string_value_mapper,health_status,/stringValuedMetric.json,/stringValuedMetricUP.metric",
        "string_value_mapper_1,health_status,/stringValuedMetric.json,/stringValuedMetricDOWN.metric", })
    void testValueMetricHandler(String moduleName, String metricName, String jsonResourceName, String expectedMetricResource)
        throws IOException {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        ValueMetricHandler handler = new ValueMetricHandler(metric, valueMapperFactory);
        handler.collectMetrics(registry, readDocument(jsonResourceName));
        assertGeneratedMetric(expectedMetricResource);
    }

    @ParameterizedTest
    @CsvSource(value = { "health,health,/simpleHealth.json" })
    void testValueMetricHandlerReportsFailedValueExtractionError(String moduleName, String metricName, String jsonResourceName) {
        JSonExporterConfig.Module.Metric metric = getMetric(moduleName, metricName);
        ValueMetricHandler handler = new ValueMetricHandler(metric, valueMapperFactory);

        Assertions.assertThrows(JsonExporterException.class, () -> handler.collectMetrics(registry, readDocument(jsonResourceName)));
    }
}
