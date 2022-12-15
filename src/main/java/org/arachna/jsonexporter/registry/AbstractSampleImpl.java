package org.arachna.jsonexporter.registry;

import org.eclipse.microprofile.metrics.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractSampleImpl {

    protected final String name;
    protected final String help;
    protected double value = 0d;
    protected Map<String, Tag> tags = new HashMap<>();

    public AbstractSampleImpl(String name, String help, Collection<Tag> tags) {
        this.name = name;
        this.help = help;

        tags.forEach(tag -> {
            this.tags.put(tag.getTagName(), tag);
        });
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public Collection<Tag> getTags() {
        return tags.values();
    }

    boolean hasSameTags(AbstractSampleImpl other) {
        Collection<Tag> otherTags = other.getTags();

        if (this.tags.size() != otherTags.size()) {
            return false;
        }

        int matchingTags = 0;

        for (Tag tag : otherTags) {
            Tag t = this.tags.get(tag.getTagName());
            if (t != null && t.getTagValue().equals(tag.getTagValue())) {
                matchingTags++;
            }
        }

        return matchingTags == this.tags.size();
    }

    public abstract SampleType getType();
}
