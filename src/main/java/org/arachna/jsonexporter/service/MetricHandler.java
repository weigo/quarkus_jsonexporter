package org.arachna.jsonexporter.service;

import org.arachna.jsonexporter.registry.MetricsRegistry;

/**
 * API for collecting metrics from the given JSON metric into the metrics registry.
 *
 * @author weigo
 */
public interface MetricHandler {
    /**
     * Collect metrics from {code}metric{code} into the given {@link MetricsRegistry}.
     *
     * @param registry
     *     {@link MetricsRegistry} to collect metrics into.
     * @param metric
     *     the JSON object to extract metrics from
     */
    void collectMetrics(MetricsRegistry registry, Object metric);
}
