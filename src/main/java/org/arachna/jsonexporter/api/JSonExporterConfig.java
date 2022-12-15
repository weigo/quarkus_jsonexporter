package org.arachna.jsonexporter.api;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix="exporter")
public interface JSonExporterConfig {
    List<Module> modules();

    interface Module {
        String name();

        public List<Metric> metrics();

        interface Metric {
            String name();

            String path();

            @ConfigProperty(defaultValue = "value")
            ScrapeType type();

            @WithName("valuetype")
            @ConfigProperty(defaultValue = "untyped")
            ValueType valueType();
            String help();

            @WithName("labels")
            Map<String, String> labelSpecs();

            @WithName("values")
            Map<String, String> valueSpecs();

        }
    }
}
