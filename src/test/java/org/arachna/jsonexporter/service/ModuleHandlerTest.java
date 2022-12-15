package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.arachna.jsonexporter.api.JSonExporterConfig;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ModuleHandlerTest {
    ModuleHandler handler;
    @Inject
    JSonExporterConfig config;

    @BeforeEach
    void setUp() {
        final Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
        JSonExporterConfig.Module module = config.modules().stream().filter(m -> m.name().equals("rss")).findFirst().get();
        handler = new ModuleHandler(configuration, module);
    }

    @Test
    public void testSinglePropertyResponse() {
        String result = handler.handle("{status: \"UP\"}");
        System.err.println(result);
    }
}
