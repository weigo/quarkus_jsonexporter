package org.arachna.jsonexporter.service;

/**
 * Handler for constant values.
 */
class ConstantValueHandler implements ValueHandler {
    /**
     * Constant to supply to consuming metrics.
     */
    final Double value;

    /**
     * Name to append to metric consumers metric name.
     */
    final String name;

    /**
     * Create handler instance for a constant metric value.
     *
     * @param name
     *     name to append to metric name.
     * @param value
     *     value to supply to consumers.
     */
    public ConstantValueHandler(String name, Double value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Handler name must not be null or empty!");
        }

        if (value == null) {
            throw new IllegalArgumentException("Handler value must not be null!");
        }

        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double handle(Object metric) {
        return value;
    }
}
