package org.arachna.jsonexporter.service.mapper;

/**
 * API for mapping values extracted from JSON metrics to double values.
 *
 * @author weigo
 */
public interface ValueMapper {
    /**
     * Map the given value into a double.
     *
     * @param metric
     *     a metric extracted from a JSON response.
     *
     * @return the double value
     */
    Double map(Object metric);
}
