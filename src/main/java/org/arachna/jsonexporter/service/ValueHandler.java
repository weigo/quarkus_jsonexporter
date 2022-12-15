package org.arachna.jsonexporter.service;

public interface ValueHandler {
    String getName();
    Double handle(Object metric);
}
