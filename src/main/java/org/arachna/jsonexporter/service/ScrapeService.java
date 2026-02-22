package org.arachna.jsonexporter.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

/**
 * Interface for building REST clients for scraping JSON metrics.
 */
@RegisterProvider(value = JsonExporterResponseExceptionMapper.class, priority = 1)
public interface ScrapeService {
    /**
     * Scrape a target URL for JSON metrics.
     *
     * @return scraped JSON document.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String scrape();
}
