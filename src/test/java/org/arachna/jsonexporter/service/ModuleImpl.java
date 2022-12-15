package org.arachna.jsonexporter.service;

import java.util.ArrayList;
import java.util.List;

import org.arachna.jsonexporter.api.JSonExporterConfig;

public class ModuleImpl implements JSonExporterConfig.Module {
    private String name;
    private List<Metric> metrics = new ArrayList<>();

    public ModuleImpl(String name, List<Metric> metrics) {
        this.name = name;
        this.metrics.addAll(metrics);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<Metric> metrics() {
        return metrics;
    }
}
