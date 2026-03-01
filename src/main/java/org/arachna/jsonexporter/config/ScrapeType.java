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
    OBJECT,

    /**
     * Scraped result shall be converted to list of metrics: keys are converted to labels, their values will be converted to the
     * respective metrics values.
     */
    MAP
}
