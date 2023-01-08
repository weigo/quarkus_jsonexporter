package org.arachna.jsonexporter.service.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.MapperType;

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
            return Collections.EMPTY_LIST;
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
        ValueMapper mapper;
        JSonExporterConfig.Module.MapperSpec spec = mapperSpec.orElse(defaultMapperSpec);

        switch (spec.mapperType()) {
            case STRING_AS_VALUE:
                mapper = new String2ValueMapper(spec);
                break;
            case DEFAULT:
            default:
                mapper = this.defaultMapper;
                break;
        }

        return mapper;
    }
}
