package org.arachna.jsonexporter.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

/**
 * Interface for building REST clients for scraping JSON metrics.
 */
public interface ScrapeService {
    /**
     * Scrape a target URL for JSON metrics.
     *
     * @return scraped JSON document.
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    String scrape();
}
