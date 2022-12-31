package org.arachna.jsonexporter.service;

import java.io.IOException;
import java.io.StringWriter;
import javax.inject.Inject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

import org.arachna.jsonexporter.AbstractBaseTest;
import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.registry.MetricsRegistry;
import org.arachna.jsonexporter.registry.TextFormat004Writer;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author weigo
 */
abstract class AbstractMetricHandlerTest extends AbstractBaseTest {
    MetricsRegistry registry;

    Configuration configuration;

    @Inject
    JSonExporterConfig config;

    @BeforeEach
    public void setUp() {
        registry = new MetricsRegistry();
        configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
    }

    protected void assertGeneratedMetric(String expectedMetricResource) throws IOException {
        String result = getOpenMetricsFormat();
        assertThat(result, equalTo(readClasspathRessource(expectedMetricResource)));
    }

    protected Object parse(String json) {
        return configuration.jsonProvider().parse(json);
    }

    protected Object readDocument(String resource) throws IOException {
        return parse(readClasspathRessource(resource));
    }

    private String getOpenMetricsFormat() {
        // print collected metrics into string and return
        StringWriter result = new StringWriter();

        try {
            new TextFormat004Writer(result).write(registry.samples());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    protected JSonExporterConfig.Module.Metric getMetric(String moduleName, String metricName) {
        JSonExporterConfig.Module healthModule =
            config.modules().stream().filter(module -> module.name().equals(moduleName)).findFirst().get();
        JSonExporterConfig.Module.Metric metric =
            healthModule.metrics().stream().filter(m -> m.name().equals(metricName)).findFirst().get();

        return metric;
    }
}
