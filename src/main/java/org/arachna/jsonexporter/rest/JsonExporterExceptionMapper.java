package org.arachna.jsonexporter.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.arachna.jsonexporter.api.JsonExporterException;
import org.jboss.logging.Logger;

/**
 * {@link ExceptionMapper} mapping internal errors to an HTTP response.
 */
@Provider
public class JsonExporterExceptionMapper implements ExceptionMapper<JsonExporterException> {
    @Inject
    Logger logger;

    @Override
    public Response toResponse(final JsonExporterException instance) {
        logger.errorf(instance.getLocalizedMessage());
        logger.debugf(instance, instance.getLocalizedMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(instance.getLocalizedMessage()).build();
    }
}
