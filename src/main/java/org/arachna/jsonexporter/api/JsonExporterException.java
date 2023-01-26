package org.arachna.jsonexporter.api;

/**
 * Exception that should be thrown to the top level and handled by the exception mapper
 */
public class JsonExporterException extends RuntimeException {
    public JsonExporterException(String message) {
        super(message);
    }
}
