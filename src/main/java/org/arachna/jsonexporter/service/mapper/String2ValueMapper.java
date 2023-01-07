package org.arachna.jsonexporter.service.mapper;

import java.util.HashMap;
import java.util.Map;

import org.arachna.jsonexporter.config.JSonExporterConfig;

/**
 * Mapper for converting strings to doubles.
 *
 * @author weigo
 */
public class String2ValueMapper implements ValueMapper {
    private final Map<String, Double> mappings = new HashMap<>();

    /**
     * Create a new instance using the given mapping specification.
     *
     * @param spec
     *     list of mappings from string to double values to be used for metric mapping.
     */
    String2ValueMapper(JSonExporterConfig.Module.MapperSpec spec) {
        spec.mappings().forEach(mapping -> mappings.put(mapping.key(), mapping.value()));
    }

    @Override
    public Double map(final Object metric) {
        return mappings.get((String) metric);
    }
}
