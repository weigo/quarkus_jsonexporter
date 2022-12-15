package org.arachna.jsonexporter.registry;

import org.eclipse.microprofile.metrics.Tag;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class TextFormat004Writer extends Writer {
    private final Writer writer;

    public TextFormat004Writer(Writer writer) {
        this.writer = writer;
    }

    public void write(Collection<MetricsRegistryImpl.MetricSamples> samples) throws IOException {
        for (MetricsRegistryImpl.MetricSamples sampleList : samples) {
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
                writer.write(label.getTagName());
                writer.write("=\"");
                writer.write(label.getTagValue());
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

    /**
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws IOException
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    /**
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
