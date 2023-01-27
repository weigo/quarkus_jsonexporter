package org.arachna.jsonexporter.service;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;

import org.arachna.jsonexporter.api.JsonExporterNotFoundException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Priority(1)
public class JsonExporterResponseExceptionMapper implements ResponseExceptionMapper<JsonExporterNotFoundException> {
    @Override
    public JsonExporterNotFoundException toThrowable(final Response response) {
        if (response.getStatus() == 404) {
            return new JsonExporterNotFoundException();
        }

        return null;
    }
}
