package org.arachna.jsonexporter.registry;

import org.eclipse.microprofile.metrics.Tag;

import java.util.Collection;

public class CounterImpl extends AbstractSampleImpl {
    public CounterImpl(String name, String help, Collection<Tag> tags) {
        super(name, help, tags);
    }

    /**
     * @return 
     */
    @Override
    public SampleType getType() {
        return SampleType.COUNTER;
    }

    public void inc() {
        this.value += + 1d;
    }

    public void inc(double value) {
        this.value += value;
    }
}
