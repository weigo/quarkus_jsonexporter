package org.arachna.jsonexporter.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.ScrapeType;
import org.arachna.jsonexporter.registry.MetricsRegistry;
import org.arachna.jsonexporter.registry.TextFormat004Writer;

/**
 * Handler for a collection of metrics subsumed under a module configuration.
 * <p>
 * When requesting metrics from a target URL one can also specify a module the acts as a wrapper around the metrics
 * collectors to be applied to the JSON returned from the request to the specified URL.
 */
public class ModuleHandler {
    private final JsonProvider jsonProvider;

    private final List<MetricHandler> metricHandlers;

    /**
     * Create a handler for the metrics specified in the module configuration. Use the supplied JSonPath configuration
     * for executing the queries specified in the module's metrics.
     *
     * @param configuration
     *     JSonPath configuration to use in queries on queried JSON
     * @param module
     *     module specifying the metrics to extract from the queried JSON
     */
    ModuleHandler(Configuration configuration, JSonExporterConfig.Module module) {
        this.jsonProvider = configuration.jsonProvider();
        this.metricHandlers = module.metrics()
            .stream()
            .map(metric -> ScrapeType.VALUE == metric.type() ? new ValueMetricHandler(metric) : new ObjectMetricHandler(metric))
            .collect(Collectors.toList());
    }

    /**
     * Apply metrics queries to the supplied JSON and collect those metrics.
     *
     * @param json
     *     JSON to apply the metrics queries to
     *
     * @return extracted metrics in prometheus metrics format (Open Metrics Format)
     */
    String handle(String json) throws IOException {
        Object document = jsonProvider.parse(json);
        MetricsRegistry registry = new MetricsRegistry();

        for (MetricHandler handler : this.metricHandlers) {
            handler.collectMetrics(registry, document);
        }
        // print collected metrics into string and return
        StringWriter result = new StringWriter();

        try (TextFormat004Writer writer = new TextFormat004Writer(result)) {
            writer.write(registry.samples());
        }

        return result.toString();
    }
}
