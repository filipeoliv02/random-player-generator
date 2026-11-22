package io.github.filipeoliv02.playergen;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Immutable set of attribute values. Every {@link Attribute} must be present and within its scale.
 */
public record Stats(Map<Attribute, Integer> values) {

    public Stats {
        Objects.requireNonNull(values, "values");
        var copy = new EnumMap<Attribute, Integer>(Attribute.class);
        copy.putAll(values);
        for (Attribute attribute : Attribute.values()) {
            Integer value = copy.get(attribute);
            if (value == null) {
                throw new IllegalArgumentException("Missing value for " + attribute);
            }
            Attribute.Scale scale = attribute.scale();
            if (!scale.contains(value)) {
                throw new IllegalArgumentException(
                        "%s=%d outside %d..%d".formatted(attribute, value, scale.min(), scale.max()));
            }
        }
        values = Collections.unmodifiableMap(copy);
    }

    public int get(Attribute attribute) {
        return values.get(attribute);
    }

    /** One {@code "Name: value"} line per attribute, in {@link Attribute} order. */
    @Override
    public String toString() {
        return values.entrySet().stream()
                .map(e -> e.getKey().displayName() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
