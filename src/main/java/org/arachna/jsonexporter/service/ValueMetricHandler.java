package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.ScrapeType;
import org.arachna.jsonexporter.registry.MetricsRegistry;

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
    public ValueMetricHandler(JSonExporterConfig.Module.Metric metricSpec) {
        super(metricSpec);

        if (!ScrapeType.VALUE.equals(metricSpec.type())) {
            throw new IllegalArgumentException("A value metric handler configuration should specify 'VALUE' as scrape type!");
        }

        if (!metricSpec.valueSpecs().isEmpty()) {
            throw new IllegalArgumentException(
                String.format("The single value metric '%s' must not specify a values section", metricSpec.name()));
        }

        try {
            this.valueHandlers.add(new JSonPathValueHandler(JsonPath.compile(metricSpec.path())));
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
