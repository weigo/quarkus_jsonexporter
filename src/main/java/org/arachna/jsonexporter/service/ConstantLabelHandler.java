package org.arachna.jsonexporter.service;

public class ConstantLabelHandler implements LabelHandler {
    private String label;
    private String value;

    public ConstantLabelHandler(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String handle(Object metric) {
        return value;
    }

    @Override
    public String getName() {
        return label;
    }
}
