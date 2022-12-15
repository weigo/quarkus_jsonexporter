package org.arachna.jsonexporter.rest;

import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.arachna.jsonexporter.service.JSonScrapeService;

@Path("/probe")
public class JsonExportResource {
    @Inject
    JSonScrapeService scrapeService;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String probe(@QueryParam("target") String targetUrl, @QueryParam("module") Optional<String> module) {
        return scrapeService.scrape(targetUrl, module.orElse("default"));
    }
}
