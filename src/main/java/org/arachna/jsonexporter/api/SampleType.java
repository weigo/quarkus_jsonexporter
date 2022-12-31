package org.arachna.jsonexporter.api;

/**
 * Type of sampled metrics.
 */
public enum SampleType {
    /**
     * Gauge: A value that can increase and decrease.
     */
    GAUGE,
    /**
     * Counter: A value that can only increase.
     */
    COUNTER
}
