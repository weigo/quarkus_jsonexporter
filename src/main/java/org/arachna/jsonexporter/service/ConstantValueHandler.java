package org.arachna.jsonexporter.service;

public class ConstantValueHandler implements ValueHandler {
    final Double value;
    final String name;

    public ConstantValueHandler(String name, Double value) {
        assert(name != null);
        assert(!name.isEmpty());
        assert(value != null);
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
