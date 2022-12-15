package org.arachna.jsonexporter.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

import org.arachna.jsonexporter.api.JSonExporterConfig;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.smallrye.common.constraint.NotNull;

@ApplicationScoped
public class JSonScrapeService {
    @Inject
    JSonExporterConfig config;

    Map<String, ModuleHandler> modules = new HashMap<>();

    Map<String, ScrapeService> scrapeServices = new HashMap<>();

    @PostConstruct
    void init() {
        final Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);

        for (JSonExporterConfig.Module module : config.modules()) {
            modules.put(module.name(), new ModuleHandler(configuration, module));
        }
    }

    public String scrape(@NotNull String target, @NotNull String module) {
        ModuleHandler handler = modules.get(module);

        if (handler == null) {
            throw new IllegalStateException(String.format("No such module: '%s'!", module));
        }

        final ScrapeService scrapeService = scrapeServices.computeIfAbsent(target,
            t -> RestClientBuilder.newBuilder().baseUri(URI.create(target)).build(ScrapeService.class));

        return handler.handle(scrapeService.scrape());
    }
}
