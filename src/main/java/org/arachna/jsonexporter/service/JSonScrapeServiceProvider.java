package org.arachna.jsonexporter.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

/**
 * Provider for scrape services.
 * <p>
 * Keeps track of created instances and wraps scrape services in order to provide timing metrics.
 *
 * @author weigo
 */
@ApplicationScoped
public class JSonScrapeServiceProvider {
    /**
     * Registry for micrometer metrics. Used to provide timers to meter retrieval timing.
     */
    @Inject
    MeterRegistry registry;

    /**
     * Map of registered scrape services.
     */
    Map<String, ScrapeService> scrapeServices = new HashMap<>();

    /**
     * Provides scrape services for target URLs.
     * <p>
     * Wraps provided instances with a timer to meter execution time of metric retrieval.
     *
     * @param target
     *     target URL is used to identify the scrape service instance to be used.
     *
     * @return {@link ScrapeService} to use for scraping a JSON metric from the target URL
     */
    public ScrapeService getService(String target) {
        return scrapeServices.computeIfAbsent(target, t -> {
            URI uri = URI.create(target);
            Tag tag = new ImmutableTag("target", target);
            Timer timer = registry.timer("scrape_service_duration_seconds", List.of(tag));

            return new TimedScrapeService(RestClientBuilder.newBuilder().baseUri(uri).build(ScrapeService.class), timer);
        });
    }

    /**
     * Wrapper around {@link ScrapeService} to time access to the remote API and collect the associated runtime metric.
     */
    private static final class TimedScrapeService implements ScrapeService {
        private final Timer timer;

        private final ScrapeService service;

        /**
         * Create an instance using the supplied {@link ScrapeService} and {@link Timer}.
         *
         * @param service
         *     the {@link ScrapeService} used to collect the remote metric as JSON
         * @param timer
         *     the {@link Timer} instance used to meter the execution of the {@link ScrapeService}
         */
        TimedScrapeService(ScrapeService service, Timer timer) {
            this.service = service;
            this.timer = timer;
        }

        @Override
        public String scrape() {
            return timer.record(service::scrape);
        }
    }
}
