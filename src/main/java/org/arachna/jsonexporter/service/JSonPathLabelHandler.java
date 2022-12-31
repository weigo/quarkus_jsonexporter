package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

import org.jboss.logging.Logger;

/**
 * Handler extracting label values using JsonPath.
 */
public class JSonPathLabelHandler implements LabelHandler {
    private static final Logger LOGGER = Logger.getLogger(JSonPathLabelHandler.class);

    /**
     * Label name.
     */
    private final String label;

    /**
     * JsonPath to use for extracting the label.
     */
    private final JsonPath jsonPath;

    /**
     * Create handler instance for the given label name and JsonPath.
     *
     * @param label
     *     label name
     * @param jsonPath
     *     JsonPath to extract the label value with
     */
    public JSonPathLabelHandler(String label, JsonPath jsonPath) {
        this.label = label;
        this.jsonPath = jsonPath;
    }

    /**
     * Extract label value using the JsonPath given at instantiation time.
     *
     * @param metric
     *     JSON document to extract label value from.
     *
     * @return extracted label value or {code}null{code}
     */
    @Override
    public String handle(Object metric) {
        JSONArray results = jsonPath.read(metric);

        if (results.isEmpty()) {
            LOGGER.warnf("No label selected from %s with expression '%s'", metric, jsonPath.getPath());
            return null;
        }

        return results.get(0).toString();
    }

    @Override
    public String getName() {
        return label;
    }
}
