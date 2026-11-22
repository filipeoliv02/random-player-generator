package io.github.filipeoliv02.playergen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Word lists used to build a player's name and nationality.
 * The bundled lists are loaded from the classpath, so the application runs from any working directory.
 */
public record NameData(List<String> firstNames, List<String> surnames, List<String> countries) {

    static final String FIRST_NAMES_RESOURCE = "/data/names.txt";
    static final String SURNAMES_RESOURCE = "/data/surnames.txt";
    static final String COUNTRIES_RESOURCE = "/data/countries.txt";

    public NameData {
        firstNames = requireNonEmpty(firstNames, "firstNames");
        surnames = requireNonEmpty(surnames, "surnames");
        countries = requireNonEmpty(countries, "countries");
    }

    /** Loads the word lists bundled with the application. */
    public static NameData loadDefault() {
        return new NameData(
                readLines(FIRST_NAMES_RESOURCE),
                readLines(SURNAMES_RESOURCE),
                readLines(COUNTRIES_RESOURCE));
    }

    /**
     * Reads a UTF-8 classpath resource, returning its stripped, non-blank lines.
     *
     * @throws IllegalStateException if the resource does not exist
     * @throws UncheckedIOException  if it cannot be read
     */
    static List<String> readLines(String resource) {
        InputStream in = NameData.class.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalStateException("Resource not found on classpath: " + resource);
        }
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .map(String::strip)
                    .filter(line -> !line.isEmpty())
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + resource, e);
        }
    }

    private static List<String> requireNonEmpty(List<String> list, String what) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(what + " must not be empty");
        }
        return List.copyOf(list);
    }
}
