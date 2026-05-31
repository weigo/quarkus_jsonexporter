package org.arachna.jsonexporter.rest;

import java.io.IOException;
import java.util.Optional;

import org.arachna.jsonexporter.api.JsonExporterNotFoundException;
import org.arachna.jsonexporter.service.JSonScrapeService;

import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for scraping JSON metrics.
 */
@Path("/probe")
public class JsonExportResource {
    /**
     * Scrape service doing the actual work.
     */
    JSonScrapeService scrapeService;

    public JsonExportResource(JSonScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

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
    public Response probe(@QueryParam("target") Optional<String> targetUrl, @QueryParam("module") Optional<String> module)
        throws IOException {
        Response response;

        try {
            if (targetUrl.isPresent()) {
                response = Response.ok(scrapeService.scrape(targetUrl.get(), module.orElse("default"))).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("Missing target URL!").build();
            }
        } catch (JsonExporterNotFoundException _) {
            String message = String.format("Scrape target '%s' not found!", targetUrl.get());
            response = Response.status(Response.Status.NOT_FOUND).entity(message).build();

            Log.errorf(message);
        }

        return response;
    }
}
