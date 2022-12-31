package org.arachna.jsonexporter.registry;

import java.util.Collection;

import org.arachna.jsonexporter.api.SampleType;

import io.micrometer.core.instrument.Tag;

/**
 * Samples of type counter.
 */
public class CounterImpl extends AbstractSampleImpl {
    /**
     * Create a new instance of a counter.
     *
     * @param metricName
     *     metric name presented by this counter.
     * @param help
     *     help for the metric.
     * @param tags
     *     list of tags (labels) for this counter.
     */
    public CounterImpl(String metricName, String help, Collection<Tag> tags) {
        super(metricName, help, tags);
    }

    @Override
    public SampleType getType() {
        return SampleType.COUNTER;
    }

    /**
     * Increase counter by 1.
     */
    public void inc() {
        this.value += 1d;
    }

    /**
     * Increase counter by the given value.
     *
     * @param value
     *     value to add to the current counter value.
     */
    public void inc(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Counters should only increase. Use Gauge when values could decrease!");
        }

        this.value += value;
    }
}
