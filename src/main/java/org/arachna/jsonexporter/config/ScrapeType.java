package org.arachna.jsonexporter.config;

/**
 * Differentiator for scrape result.
 */
public enum ScrapeType {
    /**
     * Scrape a concrete value from JSON.
     */
    VALUE,

    /**
     * Scraped result represents a JSON object where values or labels can be extracted from.
     */
    OBJECT
}
