package org.arachna.jsonexporter.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.arachna.jsonexporter.api.JsonExporterException;

/**
 * Registry for metric samples.
 */
public class MetricsRegistry {
    /**
     * Registered metric samples.
     */
    private final Map<String, MetricSamples> samples = new HashMap<>();

    /**
     * Register a new metric sample.
     *
     * @param sample
     *     metric sample to register.
     *
     * @throws IllegalArgumentException
     *     when
     *     <ul>
     *         <li>another metric with the same name and tag values already exists, or</li>
     *         <li>another metric with the same name and help text already exists.</li>
     *     </ul>
     */
    public void register(final AbstractSampleImpl sample) {
        MetricSamples samples = this.samples.computeIfAbsent(sample.getName(), name -> new MetricSamples(sample));

        // Check for metrics with same name and same tags or different help string
        for (AbstractSampleImpl current : samples) {
            // skip same instance otherwise compare tags
            if (sample != current) {
                if (sample.hasSameTags(current)) {
                    final String sb = '[' + sample.getTags()
                        .stream()
                        .map(tag -> String.format("%s=%s", tag.getKey(), tag.getValue()))
                        .collect(Collectors.joining(", ")) + ']';
                    String message =
                        String.format("A metric with name '%s' and tags '%s' has already been registered!", current.getName(), sb);
                    throw new JsonExporterException(message);
                } else if (!sample.help.equals(current.help)) {
                    String message =
                        String.format("A metric with name '%s' and help '%s' (this instances help: '%s') has already been registered!",
                            current.name, current.help, sample.help);
                    throw new JsonExporterException(message);
                }
            }
        }

        samples.add(sample);
    }

    /**
     * Return the list of collected metric samples.
     *
     * @return list of collected metric samples.
     */
    public Collection<MetricSamples> samples() {
        return samples.values();
    }

    /**
     * Iterable for sampled metrics.
     */
    class MetricSamples implements Iterable<AbstractSampleImpl> {
        AbstractSampleImpl head;

        Collection<AbstractSampleImpl> samples = new ArrayList<>();

        MetricSamples(AbstractSampleImpl head) {
            this.head = head;
        }

        @Override
        public Iterator<AbstractSampleImpl> iterator() {
            return samples.iterator();
        }

        /**
         * @param action
         *     The action to be performed for each element
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
