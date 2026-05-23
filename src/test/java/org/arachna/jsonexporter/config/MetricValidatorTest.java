package org.arachna.jsonexporter.config;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;

/**
 * @author weigo
 */
public class MetricValidatorTest {
    MetricValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MetricValidator();
    }

    @Test
    void testKeyLabelMustNotBePresentInLabelSpecsForMapTypeMetrics() {
        JSonExporterConfig.Module.Metric metric = createMetric("testMetric", ScrapeType.MAP, Map.of("label1", "value1"), "label1");
        verifyValidator(metric, MetricValidator.KEY_LABEL_MUST_NOT_BE_PRESENT_IN_LABEL_SPECS_FOR_MAP_TYPE_METRICS.formatted("testMetric"));
    }

    @Test
    void testKeyLabelIsRequiredForMapTypeMetrics() {
        JSonExporterConfig.Module.Metric metric = createMetric("testMetric", ScrapeType.MAP, Map.of("label1", "value1"), null);
        verifyValidator(metric, MetricValidator.KEY_LABEL_IS_REQUIRED_FOR_MAP_TYPE_METRICS.formatted("testMetric"));
    }
    @Test
    void testKeyLabelIsNotAllowedForNonMapTypeMetrics() {
        JSonExporterConfig.Module.Metric metric = createMetric("testMetric", ScrapeType.VALUE, Map.of("label1", "value1"), "label1");
        verifyValidator(metric, MetricValidator.KEY_LABEL_IS_NOT_ALLOWED_FOR_NON_MAP_TYPE_METRICS.formatted("testMetric"));
    }
    private void verifyValidator(final JSonExporterConfig.Module.Metric metric, String expectedMessage) {
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
        assertThat(validator.isValid(metric, context), equalTo(false));
        verify(context).buildConstraintViolationWithTemplate(templateCaptor.capture());
        assertThat(templateCaptor.getValue(), equalTo(expectedMessage));

    }

    JSonExporterConfig.Module.Metric createMetric(String name, ScrapeType type, Map<String, String> labelSpecs, String keyLabel) {
        JSonExporterConfig.Module.Metric metric = Mockito.mock(JSonExporterConfig.Module.Metric.class);
        when(metric.name()).thenReturn(name);
        when(metric.type()).thenReturn(type);
        when(metric.labelSpecs()).thenReturn(labelSpecs);
        when(metric.keyLabel()).thenReturn(keyLabel == null ? Optional.empty() : Optional.of(keyLabel));

        return metric;
    }
}
