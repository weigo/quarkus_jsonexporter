package org.arachna.jsonexporter.service;

import java.util.Map;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.ScrapeType;
import org.arachna.jsonexporter.registry.MetricsRegistry;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;

/**
 * Handler for extracting JSON objects from a JSON metric.
 */
class ObjectMetricHandler extends AbstractMetricHandler implements MetricHandler {
    /**
     * JSonPath to use extracting a JSON metrics.
     */
    private final JsonPath pathSpec;

    /**
     * Instantiate a handler and configure it from the given metric specification.
     *
     * @param metricSpec
     *     metric specification to use for configuration.
     */
    public ObjectMetricHandler(JSonExporterConfig.Module.Metric metricSpec, ValueMapperFactory valueMapperFactory) {
        super(metricSpec);

        if (!ScrapeType.OBJECT.equals(metricSpec.type())) {
            throw new IllegalArgumentException("An object metric handler configuration should specify 'OBJECT' as scrape type!");
        }

        try {
            pathSpec = JsonPath.compile(metricSpec.path());

            for (Map.Entry<String, String> valueSpec : metricSpec.valueSpecs().entrySet()) {
                if (valueSpec.getValue().startsWith("$")) {
                    JsonPath path = JsonPath.compile(valueSpec.getValue());
                    this.valueHandlers.add(
                        new JSonPathValueHandler(valueSpec.getKey(), path, valueMapperFactory.create(metricSpec.mapper())));
                } else {
                    this.valueHandlers.add(new ConstantValueHandler(valueSpec.getKey(), Double.parseDouble(valueSpec.getValue())));
                }
            }
        } catch (InvalidPathException e) {
            throw new IllegalStateException(String.format("Invalid JSonPath '%s'!%n%s", metricSpec.path(), e.getLocalizedMessage()));
        }
    }

    /**
     * Extract metrics from the given JSON using the JSonPath specified at instantiation time.
     *
     * @param json
     *     JSON metric to extract values from
     *
     * @return array of metric values extracted using the configured JSonPath
     */
    Object[] getMetricData(Object json) {
        Object[] result;
        Object tmp = pathSpec.read(json);

        try {
            result = ((JSONArray) tmp).toArray();
        } catch (ClassCastException cce) {
            result = new Object[] { tmp };
        }

        return result;
    }

    @Override
    public void collectMetrics(MetricsRegistry registry, Object document) {
        Object[] metrics = getMetricData(document);

        for (Object metric : metrics) {
            // create Metric object and register with registry.
            createMetric(registry, metric);
        }
    }
}
