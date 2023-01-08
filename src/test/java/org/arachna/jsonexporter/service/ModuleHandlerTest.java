package org.arachna.jsonexporter.service;

import java.io.IOException;
import javax.inject.Inject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

import org.arachna.jsonexporter.AbstractBaseTest;
import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ModuleHandlerTest extends AbstractBaseTest {
    @Inject
    JSonExporterConfig config;

    @Inject
    ValueMapperFactory valueMapperFactory;

    @ParameterizedTest
    @CsvSource(value = { "default,/simpleValue.json,/simpleValue.metric",
        "nested_structured_as_several_metrics_using_value,/structuredHealth.json,/structuredHealth.metric",
        "nested_structured_as_several_metrics_using_object,/structuredHealth.json,/structuredHealth1.metric" })
    void testModuleHandlers(String moduleName, String jsonResource, String expectedResultResource) throws IOException {
        String result = getModuleHandler(moduleName).handle(readClasspathRessource(jsonResource));
        assertThat(result, equalTo(readClasspathRessource(expectedResultResource)));
    }

    private ModuleHandler getModuleHandler(String moduleName) {
        JSonExporterConfig.Module module = config.modules().stream().filter(m -> m.name().equals(moduleName)).findFirst().get();
        Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
        return new ModuleHandler(configuration, module, valueMapperFactory);
    }
}
