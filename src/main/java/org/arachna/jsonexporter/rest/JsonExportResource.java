package org.arachna.jsonexporter.rest;

import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.arachna.jsonexporter.service.JSonScrapeService;

/**
 * REST endpoint for scraping JSON metrics.
 */
@Path("/probe")
public class JsonExportResource {
    /**
     * Scrape service doing the actual work.
     */
    @Inject
    JSonScrapeService scrapeService;

    /**
     * @param targetUrl
     *     URL to use for scraping a remote endpoint for metrics specified in JSON.
     * @param module
     *     the module name to use extracting the metrics
     *
     * @return scraped metrics in prometheus format.
     *
     * @throws IOException
     *     if writing to the underlying writer instance failed
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String probe(@QueryParam("target") Optional<String> targetUrl, @QueryParam("module") Optional<String> module)
        throws IOException {
        return scrapeService.scrape(targetUrl.orElseThrow(() -> new IllegalArgumentException("Missing scrape target!")),
            module.orElse("default"));
    }
}
