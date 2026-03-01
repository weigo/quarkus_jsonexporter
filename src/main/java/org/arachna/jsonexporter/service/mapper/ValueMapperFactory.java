package org.arachna.jsonexporter.service.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.MapperType;

import static org.arachna.jsonexporter.config.MapperType.STRING_AS_VALUE;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for value mappers based on configuration.
 *
 * @author weigo
 */
@ApplicationScoped
public class ValueMapperFactory {
    /**
     * Default mapper to use.
     */
    private final ValueMapper defaultMapper = new DefaultValueMapper();

    private final JSonExporterConfig.Module.MapperSpec defaultMapperSpec = new JSonExporterConfig.Module.MapperSpec() {
        @Override
        public MapperType mapperType() {
            return MapperType.DEFAULT;
        }

        @Override
        public List<JSonExporterConfig.Module.KeyValue> mappings() {
            return Collections.emptyList();
        }
    };

    /**
     * Create mapper based on given mapper specification.
     *
     * @param mapperSpec
     *     mapper specification (contains mapper type and its configuration)
     *
     * @return the mapper to use.
     */
    public ValueMapper create(Optional<JSonExporterConfig.Module.MapperSpec> mapperSpec) {
        JSonExporterConfig.Module.MapperSpec spec = mapperSpec.orElse(defaultMapperSpec);

        if (STRING_AS_VALUE.equals(spec.mapperType())) {
            return new String2ValueMapper(spec);
        }

        return defaultMapper;
    }
}
