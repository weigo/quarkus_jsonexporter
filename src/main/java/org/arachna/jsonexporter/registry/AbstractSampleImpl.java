package org.arachna.jsonexporter.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.arachna.jsonexporter.api.SampleType;

import io.micrometer.core.instrument.Tag;

/**
 * Base class for a metric sample.
 */
abstract class AbstractSampleImpl {
    /**
     * metric name.
     */
    protected final String name;

    /**
     * help string for metric.
     */
    protected final String help;

    /**
     * metric value, default 0.0.
     */
    protected double value = 0d;

    /**
     * Map of tags/labels.
     */
    protected Map<String, Tag> tags = new HashMap<>();

    /**
     * Create an instance of a metric sample.
     *
     * @param metricName
     *     name of metric to sample.
     * @param help
     *     help string to add to the metric
     * @param tags
     *     tags (labels) to add to the metric
     */
    protected AbstractSampleImpl(String metricName, String help, Collection<Tag> tags) {
        assert metricName != null : "Metric name mus not be null!";
        this.name = metricName;
        this.help = help;

        tags.forEach(tag -> this.tags.put(tag.getKey(), tag));
    }

    /**
     * Get value of this sample.
     *
     * @return value of this sample.
     */
    public double getValue() {
        return value;
    }

    /**
     * Get name of the metric represented by this sample.
     *
     * @return metric name
     */
    public String getName() {
        return name;
    }

    /**
     * Get help for metric represented by this sample.
     *
     * @return metric help
     */
    public String getHelp() {
        return help;
    }

    /**
     * Get the tags/labels for this samples metric.
     *
     * @return tags/labels for metric
     */
    public Collection<Tag> getTags() {
        return tags.values();
    }

    /**
     * Validates whether this metric has the same tags as the other metric instance.
     *
     * @param other
     *     metric instance to compare tags with.
     *
     * @return {code}true{code} when all tags have the same name and value, {code}false{code} otherwise.
     */
    boolean hasSameTags(AbstractSampleImpl other) {
        Collection<Tag> otherTags = other.getTags();

        if (this.tags.size() != otherTags.size()) {
            return false;
        }

        int matchingTags = 0;

        for (Tag tag : otherTags) {
            Tag t = this.tags.get(tag.getKey());

            if (t != null && t.getValue().equals(tag.getValue())) {
                matchingTags++;
            }
        }

        return matchingTags == this.tags.size();
    }

    /**
     * Return the type of sample represented by this concrete implementation.
     *
     * @return sample type of this metric
     */
    public abstract SampleType getType();
}
