package org.arachna.jsonexporter.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;

import io.quarkus.test.Mock;

/**
 * @author weigo
 */
@Mock
@ApplicationScoped
public class JSonClassPathScrapeServiceProvider extends JSonScrapeServiceProvider {
    @Override
    public ScrapeService getService(String target) {
        URI uri = URI.create(target);
        String path = uri.getPath();

        return this.scrapeServices.computeIfAbsent(target, t -> {
            final String ressource = readRessource(path);
            return () -> ressource;
        });
    }

    private String readRessource(String path) {
        StringWriter result = new StringWriter();

        try {
            InputStream resource = this.getClass().getResourceAsStream(path);

            if (resource == null) {
                throw new IOException("Resource '%s' does not exist on classpath!");
            }

            IOUtils.copy(resource, result, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }
}
