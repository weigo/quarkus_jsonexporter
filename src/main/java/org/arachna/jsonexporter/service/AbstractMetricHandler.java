package org.arachna.jsonexporter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.registry.CounterImpl;
import org.arachna.jsonexporter.registry.GaugeImpl;
import org.arachna.jsonexporter.registry.MetricsRegistry;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;

/**
 * Base class for implementing {@link MetricHandler}s.
 *
 * @author weigo
 */
abstract class AbstractMetricHandler implements MetricHandler {
    /**
     * List of handlers extracting values from a JSON object.
     */
    protected final List<ValueHandler> valueHandlers = new ArrayList<>();

    /**
     * List of handlers extracting labels from a JSON object.
     */
    protected final List<LabelHandler> labelHandlers = new ArrayList<>();

    /**
     * Specification of metric.
     */
    protected final JSonExporterConfig.Module.Metric metricSpec;

    AbstractMetricHandler(JSonExporterConfig.Module.Metric metricSpec) {
        this.metricSpec = metricSpec;
        initLabelHandlers(metricSpec.labelSpecs().entrySet());
    }

    void createMetric(MetricsRegistry registry, Object json) {
        Map<String, Double> values = getValues(json);
        Collection<Tag> labels = getLabels(json);

        for (Map.Entry<String, Double> entry : values.entrySet()) {
            final Double value = entry.getValue();
            String metricName =
                entry.getKey() != null && !entry.getKey().isEmpty() ? String.format("%s_%s", metricSpec.name(), entry.getKey())
                                                                    : metricSpec.name();

            switch (metricSpec.sampleType()) {
                case COUNTER:
                    CounterImpl counter = new CounterImpl(metricName, metricSpec.help(), labels);
                    counter.inc(value);
                    registry.register(counter);
                    break;

                case GAUGE:
                    GaugeImpl gauge = new GaugeImpl(metricName, metricSpec.help(), labels);
                    gauge.setValue(value);

                    registry.register(gauge);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + metricSpec.sampleType());
            }
        }
    }

    Map<String, Double> getValues(Object json) {
        Map<String, Double> values = new HashMap<>();

        for (ValueHandler handler : this.valueHandlers) {
            values.put(handler.getName(), handler.handle(json));
        }

        return values;
    }

    Collection<Tag> getLabels(Object metric) {
        Collection<Tag> labels = new ArrayList<>();

        for (LabelHandler handler : labelHandlers) {
            String label = handler.handle(metric);

            if (label != null) {
                labels.add(new ImmutableTag(handler.getName(), label));
            }
        }

        return labels;
    }

    private void initLabelHandlers(Collection<Map.Entry<String, String>> labelSpecs) {
        for (Map.Entry<String, String> labelSpec : labelSpecs) {
            if (labelSpec.getValue().startsWith("$")) {
                JsonPath path = JsonPath.compile(labelSpec.getValue());
                this.labelHandlers.add(new JSonPathLabelHandler(labelSpec.getKey(), path));
            } else {
                this.labelHandlers.add(new ConstantLabelHandler(labelSpec.getKey(), labelSpec.getValue()));
            }
        }
    }
}
