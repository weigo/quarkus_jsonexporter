package org.arachna.jsonexporter.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

public interface ScrapeService {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    String scrape();
}
