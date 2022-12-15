package org.arachna.jsonexporter.service;

import org.eclipse.microprofile.metrics.Tag;

public interface LabelHandler {
    String handle(Object metric);
    String getName();
}
