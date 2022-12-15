package org.arachna.jsonexporter.registry;

import org.eclipse.microprofile.metrics.Tag;

import java.util.Collection;

public class GaugeImpl extends AbstractSampleImpl {

    public GaugeImpl(String name, String help, Collection<Tag> tags) {
        super(name, help, tags);
    }

    /**
     * @return 
     */
    @Override
    public SampleType getType() {
        return SampleType.GAUGE;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
