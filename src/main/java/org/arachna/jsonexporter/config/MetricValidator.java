package org.arachna.jsonexporter.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;

/**
 * @author weigo
 */
public class MetricValidator implements ConstraintValidator<Valid, JSonExporterConfig.Module.Metric> {

    public static final String KEY_LABEL_IS_REQUIRED_FOR_MAP_TYPE_METRICS = "In metric '%s': keyLabel is required for MAP type metrics";

    public static final String KEY_LABEL_MUST_NOT_BE_PRESENT_IN_LABEL_SPECS_FOR_MAP_TYPE_METRICS =
        "In metric '%s': keyLabel must not be present in labelSpecs for MAP type metrics";

    public static final String KEY_LABEL_IS_NOT_ALLOWED_FOR_NON_MAP_TYPE_METRICS =
        "In metric '%s': keyLabel is not allowed for non-MAP type metrics";

    @Override
    public boolean isValid(final JSonExporterConfig.Module.Metric metric, final ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

        if (ScrapeType.MAP.equals(metric.type())) {
            if (metric.keyLabel().isEmpty()) {
                addConstraintViolation(constraintValidatorContext, KEY_LABEL_IS_REQUIRED_FOR_MAP_TYPE_METRICS, metric);
                valid = false;
            } else if (metric.labelSpecs().containsKey(metric.keyLabel().get())) {
                addConstraintViolation(constraintValidatorContext, KEY_LABEL_MUST_NOT_BE_PRESENT_IN_LABEL_SPECS_FOR_MAP_TYPE_METRICS,
                    metric);
                valid = false;
            }
        } else {
            if (metric.keyLabel().isPresent()) {
                addConstraintViolation(constraintValidatorContext, KEY_LABEL_IS_NOT_ALLOWED_FOR_NON_MAP_TYPE_METRICS, metric);
                valid = false;
            }
        }

        return valid;
    }

    private void addConstraintViolation(final ConstraintValidatorContext constraintValidatorContext, final String template,
        JSonExporterConfig.Module.Metric metric) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(template.formatted(metric.name())).addConstraintViolation();
    }
}
