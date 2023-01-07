package org.arachna.jsonexporter.config;

/**
 * Mapper type enumeration.
 * <p>
 * Enumerates the known type one can use to map JSON objects/values into values usable by prometheus.
 *
 * @author weigo
 */
public enum MapperType {
    /**
     * the default mapper: convert numbers and booleans to double.
     */
    DEFAULT,

    /**
     * Map a collection of strings to a collection of values (bijective)
     */
    STRING_AS_VALUE
}
