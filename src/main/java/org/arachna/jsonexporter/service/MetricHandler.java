package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.arachna.jsonexporter.api.JSonExporterConfig;
import org.arachna.jsonexporter.registry.CounterImpl;
import org.arachna.jsonexporter.registry.GaugeImpl;
import org.arachna.jsonexporter.registry.MetricsRegistryImpl;
import net.minidev.json.JSONArray;
import org.eclipse.microprofile.metrics.Tag;
import org.jboss.logging.Logger;

import java.util.*;

class MetricHandler {
    private JsonPath pathSpec;

    private List<ValueHandler> valueHandlers = new ArrayList<>();

    private List<LabelHandler> labelHandlers = new ArrayList<>();
    private JSonExporterConfig.Module.Metric metricSpec;

    Logger logger = Logger.getLogger(getClass());

    public MetricHandler(JSonExporterConfig.Module.Metric metricSpec) {
        try {
            pathSpec = JsonPath.compile(metricSpec.path());
            this.metricSpec = metricSpec;

            for (Map.Entry<String, String> valueSpec : metricSpec.valueSpecs().entrySet()) {
                // TODO: differentiate between
                //  - constant (@configuration time) and
                //  - dynamic values (evaluate paths from the returned json @runtime)
                this.valueHandlers.add(new ConstantValueHandler(valueSpec.getKey(), Double.parseDouble(valueSpec.getValue())));
            }

            for (Map.Entry<String, String> labelSpec : metricSpec.labelSpecs().entrySet()) {
                // TODO: differentiate between
                //  - constant (@configuration time) and
                //  - dynamic values (evaluate paths from the returned json @runtime)

                if (labelSpec.getValue().startsWith("$")) {
                    JsonPath path = JsonPath.compile(labelSpec.getValue());
                    this.labelHandlers.add(new JSonPathLabelHandler(labelSpec.getKey(), path));
                } else {
                    this.labelHandlers.add(new ConstantLabelHandler(labelSpec.getKey(), labelSpec.getValue()));
                }
            }
        } catch (InvalidPathException e) {
            logger.fatalf(e, "JsonPath: %s", metricSpec.path());
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }

    Object[] getMetricData(Object json) {
        return ((JSONArray) pathSpec.read(json)).toArray();
    }

    void createMetric(MetricsRegistryImpl registry, Object metric) {
        Map<String, Double> values = getValues(metric);
        Collection<Tag> labels = getLabels(metric);

        for (Map.Entry<String, Double> entry : values.entrySet()) {
            final Double value = entry.getValue();

            switch (metricSpec.valueType()) {
                case untyped:
                case counter:
                    CounterImpl counter = new CounterImpl(String.format("%s_%s", metricSpec.name(), entry.getKey()), metricSpec.help(), labels);
                    counter.inc(value);
                    registry.register(counter);
                    break;

                case gauge:
                    GaugeImpl gauge = new GaugeImpl(String.format("%s_%s", metricSpec.name(), entry.getKey()), metricSpec.help(), labels);

                    registry.register(gauge);
                    gauge.setValue(value.doubleValue());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + metricSpec.valueType());
            }
        }
    }

    public void createMetrics(MetricsRegistryImpl registry, Object document) {
        Object[] metrics = getMetricData(document);

        for (Object metric : metrics) {
            // create Metric object and register with registry.
            createMetric(registry, metric);
        }
    }

    Map<String, Double> getValues(Object metric) {
        Map<String, Double> values = new HashMap<>();

        for (ValueHandler handler : this.valueHandlers) {
            values.put(handler.getName(), handler.handle(metric));
        }

        return values;
    }

    Collection<Tag> getLabels(Object metric) {
        Collection<Tag> labels = new ArrayList<>();

        for (LabelHandler handler : labelHandlers) {
            labels.add(new Tag(handler.getName(), handler.handle(metric)));
        }

        return labels;
    }
}
