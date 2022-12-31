package org.arachna.jsonexporter.service;

/**
 * Handler specification for value extraction from a given metric result.
 */
public interface ValueHandler {
    /**
     * Name of the extracted value.
     *
     * @return the name of the extracted value (probably empty)
     */
    String getName();

    /**
     * Extracts value from given metric.
     *
     * @param metric
     *     the metric to extract a result from.
     *
     * @return extracted value
     */
    Double handle(Object metric);
}
