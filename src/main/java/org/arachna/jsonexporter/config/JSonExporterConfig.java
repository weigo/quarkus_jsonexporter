package org.arachna.jsonexporter.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.arachna.jsonexporter.api.SampleType;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

/**
 * JSON exporter Configuration API.
 * <p>
 * Defines the internal API for specifying modules and metrics to apply to JSON metrics.
 */
@ConfigMapping(prefix = "exporter")
public interface JSonExporterConfig {
    /**
     * List of modules to group metrics into.
     * <p>
     * Modules specify a list of metrics to apply to a metric endpoints JSON response.
     *
     * @return list of configured modules
     */
    List<Module> modules();

    /**
     * A module represents a list of metrics specifications that should be used to extract metrics from JSON.
     */
    interface Module {
        /**
         * Name of module. Used to identify the module to use to extract a collection of metrics.
         *
         * @return the modules name
         */
        String name();

        /**
         * List of metric specifications contained in this module.
         *
         * @return metric specifications of this module
         */
        List<Metric> metrics();

        /**
         * A Metric specification.
         */
        interface Metric {
            /**
             * Name of the metric.
             *
             * @return name of metric
             */
            String name();

            /**
             * JSON path to use to extract the metrics from a JSON document.
             *
             * @return JSON path used when extracting metrics from a JSON document.
             */
            String path();

            /**
             * The type of scrape to use when extracting metric information from a JSON document.
             * Default is {@link ScrapeType#VALUE}.
             *
             * @return the type of scrape to use for metric extraction
             */
            @WithDefault("VALUE")
            ScrapeType type();

            /**
             * The type of sample the scraped metric shall represent.
             *
             * @return the type of metric the scraped value should be represented as.
             */
            @WithName("valuetype")
            @WithDefault("GAUGE")
            SampleType sampleType();

            /**
             * Help for the metric.
             *
             * @return the help for the scraped metric.
             */
            String help();

            /**
             * Map of labels to use for the scraped metric.
             * <p>
             * Values are either constant or a JsonPath compliant expression used to extract the actual label value from the scraped JSON
             * document.
             *
             * @return specification of how to extract a given labels value.
             */
            @WithName("labels")
            Map<String, String> labelSpecs();

            /**
             * Map of value specifications to use for extracting metrics from a scraped JSON document.
             * <p>
             * Values are either constant or a JsonPath compliant expression used to extract the actual label value from the scraped JSON
             * document.
             *
             * @return specification of how to extract a given value.
             */
            @WithName("values")
            Map<String, String> valueSpecs();

            /**
             * Specification of mapper to use for scraped values
             *
             * @return mapper specification
             */
            Optional<MapperSpec> mapper();
        }

        /**
         * Map keys to double values.
         */
        interface KeyValue {
            String key();

            Double value();
        }

        interface MapperSpec {
            /**
             * Specify the mapper type.
             *
             * @return type of mapper to use.
             */
            @WithName("type")
            @WithDefault("DEFAULT")
            MapperType mapperType();

            /**
             * Return a list of string to double values to use when mapping JSON values to metrics.
             *
             * @return list of key value pairs
             */
            List<KeyValue> mappings();
        }
    }
}
