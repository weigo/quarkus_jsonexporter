package org.arachna.jsonexporter.service.mapper;

/**
 * @author weigo
 */
public class DefaultValueMapper implements ValueMapper {
    /**
     * Value to extract from {@link Boolean#FALSE}.
     */
    private static final Double ZERO = 0d;

    /**
     * Value to extract from {@link Boolean#TRUE}.
     */
    private static final Double ONE = 1d;

    @Override
    public Double map(final Object metric) {
        Double result = null;

        if (metric instanceof Number) {
            result = ((Number) metric).doubleValue();
        } else if (metric instanceof Boolean) {
            result = Boolean.TRUE.equals(metric) ? ONE : ZERO;
        }

        return result;
    }
}
