package org.arachna.jsonexporter.registry;

import org.jboss.logging.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MetricsRegistryImpl {
    private final Logger logger = Logger.getLogger(getClass());
    private Map<String, MetricSamples> samples = new HashMap<>();

    public void register(final AbstractSampleImpl sample) {
        MetricSamples samples = this.samples.computeIfAbsent(sample.getName(), name -> new MetricSamples(sample));

        for (AbstractSampleImpl current : samples) {
            // skip same instance otherwise compare tags
            if (sample != current && sample.hasSameTags(current)) {
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                sb.append(sample.getTags().stream().map(tag -> String.format("%s=%s", tag.getTagName(), tag.getTagValue())).collect(Collectors.joining(", ")));
                sb.append(']');
                String message = String.format("A metric with name '%s' and tags '%s' has already been registered!", sample.getName());
                logger.errorf(message);
                throw new IllegalArgumentException(message);
            }
        }

        samples.add(sample);
    }

    public Collection<MetricSamples> samples() {
        return samples.values();
    }

    class MetricSamples implements Iterable<AbstractSampleImpl> {
        AbstractSampleImpl head;
        Collection<AbstractSampleImpl> samples = new ArrayList<>();

        MetricSamples(AbstractSampleImpl head) {
            this.head = head;
        }

        /**
         * @return
         */
        @Override
        public Iterator<AbstractSampleImpl> iterator() {
            return samples.iterator();
        }

        /**
         * @param action The action to be performed for each element
         */
        @Override
        public void forEach(Consumer<? super AbstractSampleImpl> action) {
            Iterable.super.forEach(action);
        }

        /**
         * @return
         */
        @Override
        public Spliterator<AbstractSampleImpl> spliterator() {
            return Iterable.super.spliterator();
        }

        public void add(AbstractSampleImpl sample) {
            this.samples.add(sample);
        }
    }
}
