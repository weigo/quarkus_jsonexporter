package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.ScrapeType;
import org.arachna.jsonexporter.registry.MetricsRegistry;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;

/**
 * Handler for extracting concrete values from a JSON object.
 *
 * @author weigo
 */
public class ValueMetricHandler extends AbstractMetricHandler {
    /**
     * Instantiate a handler using the supplied metric specification.
     *
     * @param metricSpec
     *     metric specification to build the handler from
     */
    public ValueMetricHandler(JSonExporterConfig.Module.Metric metricSpec, ValueMapperFactory valueMapperFactory) {
        super(metricSpec);

        if (!ScrapeType.VALUE.equals(metricSpec.type())) {
            throw new IllegalArgumentException(
                String.format("A value metric handler configuration should specify '%s' as scrape type!", ScrapeType.VALUE.name()));
        }

        if (!metricSpec.valueSpecs().isEmpty()) {
            throw new IllegalArgumentException(
                String.format("The single value metric '%s' must not specify a values section", metricSpec.name()));
        }

        try {
            this.valueHandlers.add(
                new JSonPathValueHandler(JsonPath.compile(metricSpec.path()), valueMapperFactory.create(metricSpec.mapper())));
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException(
                String.format("Metric '%s' specified an illegal JSonPath: '%s':%n'%s'%n", metricSpec.name(), metricSpec.path(),
                    e.getLocalizedMessage()));
        }
    }

    @Override
    public void collectMetrics(MetricsRegistry registry, Object document) {
        createMetric(registry, document);
    }
}
