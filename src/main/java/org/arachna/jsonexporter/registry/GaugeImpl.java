package org.arachna.jsonexporter.registry;

import java.util.Collection;

import org.arachna.jsonexporter.api.SampleType;

import io.micrometer.core.instrument.Tag;

/**
 * Sample of type gauge.
 */
public class GaugeImpl extends AbstractSampleImpl {

    /**
     * Create a new instance of a gauge.
     *
     * @param metricName
     *     metric name presented by this counter.
     * @param help
     *     help for the metric.
     * @param tags
     *     list of tags (labels) for this counter.
     */
    public GaugeImpl(String metricName, String help, Collection<Tag> tags) {
        super(metricName, help, tags);
    }

    @Override
    public SampleType getType() {
        return SampleType.GAUGE;
    }

    /**
     * Update the value represented by this gauge.
     *
     * @param value
     *     new value.
     */
    public void setValue(double value) {
        this.value = value;
    }
}
