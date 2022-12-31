package org.arachna.jsonexporter.registry;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import io.micrometer.core.instrument.Tag;

/**
 * Writer for the prometheus metrics format.
 */
public class TextFormat004Writer extends Writer {
    private final Writer writer;

    /**
     * Instantiate the prometheus format writer with a {@see Writer} instance used to do the actual writing.
     *
     * @param writer
     *     {@see Writer} instance doing the actual writing
     */
    public TextFormat004Writer(Writer writer) {
        this.writer = writer;
    }

    /**
     * Emit the given metric samples in prometheus metrics format.
     *
     * @param samples
     *     list of samples to write
     *
     * @throws IOException
     *     when writing into the underlying writer fails
     */
    public void write(Collection<MetricsRegistry.MetricSamples> samples) throws IOException {
        for (MetricsRegistry.MetricSamples sampleList : samples) {
            emitHeader(sampleList.head);

            for (AbstractSampleImpl sample : sampleList) {
                emitMetric(sample);
            }
        }
    }

    private void emitMetric(AbstractSampleImpl sample) throws IOException {
        writer.write(sample.getName());

        Iterator<Tag> labels = sample.getTags().iterator();

        if (labels.hasNext()) {
            writer.write('[');

            while (labels.hasNext()) {
                Tag label = labels.next();
                writer.write(label.getKey());
                writer.write("=\"");
                writer.write(label.getValue());
                writer.write("\"");

                if (labels.hasNext()) {
                    writer.write("\"");
                }
            }

            writer.write(']');
        }

        writer.write(' ');
        writer.write(Double.toString(sample.getValue()));
        writer.write('\n');
    }

    private void emitHeader(AbstractSampleImpl sample) throws IOException {
        writer.write("# HELP ");
        writer.write(sample.getName());
        writer.write(' ');
        writer.write(sample.getHelp());
        writer.write('\n');
        writer.write("# TYPE ");
        writer.write(sample.getType().name());
        writer.write('\n');
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
