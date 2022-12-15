package org.arachna.jsonexporter.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.jayway.jsonpath.Configuration;

import org.arachna.jsonexporter.registry.MetricsRegistryImpl;
import org.arachna.jsonexporter.registry.TextFormat004Writer;
import org.arachna.jsonexporter.api.JSonExporterConfig;

public class ModuleHandler {
    private Configuration configuration;

    private JSonExporterConfig.Module module;

    private List<MetricHandler> metricHandlers;

    ModuleHandler(Configuration configuration, JSonExporterConfig.Module module) {
        this.configuration = configuration;
        this.module = module;
        this.metricHandlers = module.metrics().stream().map(MetricHandler::new).collect(Collectors.toList());
    }

    String handle(String json) {
        Object document = parse(json);
        MetricsRegistryImpl registry = new MetricsRegistryImpl();

        for (MetricHandler handler : this.metricHandlers) {
            handler.createMetrics(registry, document);
        }
        // print collected metrics into string and return
        StringWriter result = new StringWriter();

        try (TextFormat004Writer writer = new TextFormat004Writer(result)) {
            writer.write(registry.samples());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    private Object parse(String json) {
        return configuration.jsonProvider().parse(json);
    }
}
