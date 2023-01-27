package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import org.arachna.jsonexporter.api.JsonExporterException;
import org.arachna.jsonexporter.service.mapper.ValueMapper;

/**
 * Handler for extraction of a single value from a JSON metric.
 *
 * @author weigo
 */
public class JSonPathValueHandler implements ValueHandler {
    /**
     * JsonPath to use for extracting the value.
     */
    final JsonPath path;

    /**
     * Name of value to extract.
     */
    final String name;

    final ValueMapper mapper;

    /**
     * Use this constructor when a value name shall be appended to the respective metric name.
     *
     * @param name
     *     name to append to metric name
     * @param path
     *     JSONPath to use to extract value from a JSON metric
     * @param mapper
     *     value mapper to use on extracted value
     */
    JSonPathValueHandler(String name, JsonPath path, final ValueMapper mapper) {
        this.mapper = mapper;
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty!");
        }

        this.name = name;
        this.path = path;
    }

    /**
     * Create an instance when no name shall be appended to the metric sample.
     *
     * @param path
     *     JSONPath to use to extract value from a JSON metric
     * @param mapper
     *     value mapper to use on extracted value
     */
    JSonPathValueHandler(JsonPath path, final ValueMapper mapper) {
        this.mapper = mapper;
        this.name = "";
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Extract metric value using the configured JSONPath and convert the result to a double value.
     *
     * @param metric
     *     the metric to extract a result from.
     *
     * @return the double value extracted from the JSON metric.
     *
     * @throws IllegalStateException
     *     when no value could be extracted (or converted).
     */
    @Override
    public Double handle(Object metric) {
        Double result;

        try {
            Object tmp = path.read(metric);
            result = mapper.map(tmp);

            if (result == null) {
                throw new JsonExporterException(
                    String.format("Could not extract result from metric '%s' using path '%s'. Result was %s", metric, path.getPath(), tmp));
            }
        } catch (PathNotFoundException p) {
            throw new JsonExporterException(
                String.format("Failed to extract result from metric '%s' using path '%s'!%n%s", metric, path.getPath(),
                    p.getLocalizedMessage()));
        }

        return result;
    }
}
