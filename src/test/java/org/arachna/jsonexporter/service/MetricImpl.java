package org.arachna.jsonexporter.service;

import java.util.HashMap;
import java.util.Map;

import org.arachna.jsonexporter.api.JSonExporterConfig;
import org.arachna.jsonexporter.api.ScrapeType;
import org.arachna.jsonexporter.api.ValueType;

public class MetricImpl implements JSonExporterConfig.Module.Metric {
    String name;
    String path;
    ScrapeType scrapeType;
    ValueType valueType;
    String help;
    Map<String, String> labelSpecs = new HashMap<>();
    Map<String, String> valueSpecs = new HashMap<>();

    public MetricImpl(String name, String path, String help, ScrapeType scrapeType, ValueType valueType) {
        this.name = name;
        this.path = path;
        this.help = help;
        this.scrapeType = scrapeType;
        this.valueType = valueType;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public ScrapeType type() {
        return scrapeType;
    }

    @Override
    public ValueType valueType() {
        return valueType;
    }

    @Override
    public String help() {
        return help;
    }

    @Override
    public Map<String, String> labelSpecs() {
        return labelSpecs;
    }

    @Override
    public Map<String, String> valueSpecs() {
        return valueSpecs;
    }

    void addLabelSpec(String label, String valueSpec) {
        this.labelSpecs.put(label, valueSpec);
    }
    void addValueSpec(String label, String valueSpec) {
        this.valueSpecs.put(label, valueSpec);
    }
}
