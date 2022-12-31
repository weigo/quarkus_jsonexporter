package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.JsonPath;

/**
 * Handler for extraction of a single value from a JSON metric.
 *
 * @author weigo
 */
public class JSonPathValueHandler implements ValueHandler {
    /**
     * Value to extract from {@link Boolean#FALSE}.
     */
    private static final Double ZERO = 0d;

    /**
     * Value to extract from {@link Boolean#TRUE}.
     */
    private static final Double ONE = 1d;

    /**
     * JsonPath to use for extracting the value.
     */
    final JsonPath path;

    /**
     * Name of value to extract.
     */
    final String name;

    /**
     * Use this constructor when a value name shall be appended to the respective metric name.
     *
     * @param name
     *     name to append to metric name
     * @param path
     *     JSONPath to use to extract value from a JSON metric
     */
    JSonPathValueHandler(String name, JsonPath path) {
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
     */
    JSonPathValueHandler(JsonPath path) {
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
        Object tmp = path.read(metric);
        Double result = null;

        if (tmp instanceof Number) {
            result = ((Number) tmp).doubleValue();
        } else if (tmp instanceof Boolean) {
            result = Boolean.TRUE.equals(tmp) ? ONE : ZERO;
        }

        if (result == null) {
            throw new IllegalStateException(
                String.format("Could not extract result from metric '%s' using path '%s'. Result was %s", metric, path.getPath(), tmp));
        }

        return result;
    }
}
