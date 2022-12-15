package org.arachna.jsonexporter.service;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

public class JSonPathLabelHandler implements LabelHandler {
    private String label;
    private JsonPath jsonPath;

    public JSonPathLabelHandler(String label, JsonPath jsonPath) {
        this.label = label;
        this.jsonPath = jsonPath;
    }

    @Override
    public String getName() {
        return label;
    }

    /**
     * @param metric
     * @return
     */
    @Override
    public String handle(Object metric) {
        JSONArray results = jsonPath.read(metric);

        if (results.isEmpty()) {
            throw new IllegalStateException(String.format("No label selected from %s with expression '%s'", metric.toString(), jsonPath.getPath()));
        }

        return results.get(0).toString();
    }
}
