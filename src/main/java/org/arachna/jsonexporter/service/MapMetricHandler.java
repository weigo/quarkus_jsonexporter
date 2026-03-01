package org.arachna.jsonexporter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.ScrapeType;
import org.arachna.jsonexporter.registry.MetricsRegistry;
import org.arachna.jsonexporter.service.mapper.ValueMapper;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;

/**
 * @author weigo
 */
public class MapMetricHandler extends AbstractMetricHandler {
    /**
     * JSonPath to use extracting a JSON metrics.
     */
    private final JsonPath pathSpec;

    private final ValueMapper valueMapper;

    MapMetricHandler(final JSonExporterConfig.Module.Metric metricSpec, ValueMapperFactory valueMapperFactory) {
        super(metricSpec);

        if (!ScrapeType.MAP.equals(metricSpec.type())) {
            throw new IllegalArgumentException("A map metric handler configuration should specify 'MAP' as scrape type!");
        }

        if (this.metricSpec.keyLabel().isEmpty()) {
            throw new IllegalArgumentException("A map metric handler configuration should specify a key label!");
        }

        try {
            pathSpec = JsonPath.compile(metricSpec.path());
            valueMapper = valueMapperFactory.create(this.metricSpec.mapper());

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

    @Override
    public void collectMetrics(final MetricsRegistry registry, final Object metric) {
        JSONArray results = this.pathSpec.read(metric);
        Collection<Tag> labels = getLabels(metric);
        String keyLabel = this.metricSpec.keyLabel().get();
        String metricName = this.metricSpec.name();

        results.forEach(map -> {
            ((Map<Object, Object>) map).forEach((key, value) -> {
                ImmutableTag label = new ImmutableTag(keyLabel, (String) key);
                Double result = valueMapper.map(value);
                Collection<Tag> tags = new ArrayList<>(labels);
                tags.add(label);

                createMetric(registry, metricName, tags, result);
            });
        });
    }
}
