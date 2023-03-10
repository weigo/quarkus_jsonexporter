package org.arachna.jsonexporter.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.jayway.jsonpath.Configuration;

import org.arachna.jsonexporter.api.JsonExporterException;
import org.arachna.jsonexporter.config.JSonExporterConfig;
import org.arachna.jsonexporter.service.mapper.ValueMapperFactory;

import io.smallrye.common.constraint.NotNull;

/**
 * Service using the module configuration to extract metrics from scrape targets.
 */
@ApplicationScoped
public class JSonScrapeService {
    /**
     * Module configuration for metric extraction from scrape targets.
     */
    @Inject
    JSonExporterConfig config;

    /**
     * Provider for actual scraping of JSON metrics.
     */
    @Inject
    JSonScrapeServiceProvider jsonScrapeServiceProvider;

    @Inject
    ValueMapperFactory valueMapperFactory;

    /**
     * Map of configured Modules.
     */
    Map<String, ModuleHandler> modules = new HashMap<>();

    /**
     * Initialize modules from configuration.
     */
    @PostConstruct
    void init() {
        final Configuration configuration = Configuration.defaultConfiguration(); //.addOptions(Option.ALWAYS_RETURN_LIST);

        config.modules().forEach(module -> modules.put(module.name(), new ModuleHandler(configuration, module, valueMapperFactory)));
    }

    /**
     * Scrape an actual target using the specified module name.
     *
     * @param target
     *     target URL to scrape
     * @param module
     *     name of module to use for extracting the metrics
     *
     * @return the scraped metrics in prometheus metrics format
     *
     * @throws IOException
     *     if writing to the underlying writer failed
     */
    public String scrape(@NotNull String target, @NotNull String module) throws IOException {
        ModuleHandler handler = modules.get(module);

        if (handler == null) {
            throw new JsonExporterException(String.format("Module '%s' not found for handling scrape target '%s'!", module, target));
        }

        final ScrapeService scrapeService = this.jsonScrapeServiceProvider.getService(target);

        return handler.handle(scrapeService.scrape());
    }
}
