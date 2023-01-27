package org.arachna.jsonexporter.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.config.MapperType;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class JSONPathTest {
    ValueMapperFactory valueMapperFactory;

    JSonExporterConfig.Module.MapperSpec mapperSpec = new JSonExporterConfig.Module.MapperSpec() {
        @Override
        public MapperType mapperType() {
            return MapperType.STRING_AS_VALUE;
        }

        @Override
        public List<JSonExporterConfig.Module.KeyValue> mappings() {
            return Arrays.asList(new KeyValueImpl("UP", 1.0d));
        }
    };

    @BeforeEach
    public void init() {
        valueMapperFactory = new ValueMapperFactory();
    }

    @Test
    public void assert1() {
        JSonPathValueHandler valueHandler =
            new JSonPathValueHandler(JsonPath.compile("$.status"), valueMapperFactory.create(Optional.of(mapperSpec)));
        JSONObject json = new JSONObject();
        json.put("status", "UP");
        assertThat(valueHandler.handle(json), equalTo(1.0));
    }
    @Test
    public void assert2() {
        JSonPathValueHandler valueHandler =
            new JSonPathValueHandler(JsonPath.compile("$.status"), valueMapperFactory.create(Optional.of(mapperSpec)));
        JSONObject json = new JSONObject();
        json.put("status", "UP");
        json.put("checks", new JSONArray());
        assertThat(valueHandler.handle(json), equalTo(1.0));
    }

    public record KeyValueImpl(String key, Double value) implements JSonExporterConfig.Module.KeyValue {
    }
}
