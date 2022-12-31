package org.arachna.jsonexporter.service;

/**
 * API for extracting a metric label from a given JSON object.
 */
public interface LabelHandler {
    /**
     * Extract a metric label from the given JSON object.
     *
     * @param metric
     *     JSON object to extract label from.
     *
     * @return extracted label or {code}null{code}
     */
    String handle(Object metric);

    /**
     * Label name.
     *
     * @return name of extracted label.
     */
    String getName();
}
