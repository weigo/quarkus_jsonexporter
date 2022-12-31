package org.arachna.jsonexporter.service;

/**
 * Handler for labels that are constant (that is known at configuration time).
 */
public class ConstantLabelHandler implements LabelHandler {
    /**
     * The label name.
     */
    private final String label;

    /**
     * The label value.
     */
    private final String value;

    /**
     * Create a handler with known values.
     *
     * @param label
     *     the label name.
     * @param value
     *     the label's value.
     */
    public ConstantLabelHandler(String label, String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Return the label value specified at configuration time.
     *
     * @param metric
     *     Ignored since this will return the configured label value.
     *
     * @return value specified at configuration time.
     */
    @Override
    public String handle(Object metric) {
        return value;
    }

    @Override
    public String getName() {
        return label;
    }
}
