package org.arachna.jsonexporter.api;

import org.eclipse.microprofile.metrics.MetricType;

public enum ValueType {
    gauge(MetricType.GAUGE),
    counter(MetricType.COUNTER),
    untyped(MetricType.INVALID);

    public final MetricType type;

    ValueType(final MetricType type) {
        this.type = type;
    }
}
