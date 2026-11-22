package io.github.filipeoliv02.playergen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class NameDataTest {

    @Test
    void loadsBundledListsFromClasspath() {
        var data = NameData.loadDefault();
        assertTrue(data.firstNames().size() > 1_000, "first names: " + data.firstNames().size());
        assertTrue(data.surnames().size() > 10_000, "surnames: " + data.surnames().size());
        assertTrue(data.countries().size() > 100, "countries: " + data.countries().size());
    }

    @Test
    void entriesAreTrimmedAndNonBlank() {
        var data = NameData.loadDefault();
        for (List<String> list : List.of(data.firstNames(), data.surnames(), data.countries())) {
            for (String s : list) {
                assertFalse(s.isBlank());
                assertEquals(s.strip(), s);
            }
        }
    }

    @Test
    void readsResourcesAsUtf8() {
        // countries.txt contains non-ASCII names such as "Åland Islands".
        assertTrue(NameData.loadDefault().countries().contains("Åland Islands"));
    }

    @Test
    void missingResourceFailsClearly() {
        var e = assertThrows(IllegalStateException.class, () -> NameData.readLines("/data/does-not-exist.txt"));
        assertTrue(e.getMessage().contains("does-not-exist"));
    }

    @Test
    void rejectsEmptyLists() {
        assertThrows(IllegalArgumentException.class, () -> new NameData(List.of(), List.of("a"), List.of("b")));
    }

    @Test
    void listsAreImmutable() {
        var data = NameData.loadDefault();
        assertThrows(UnsupportedOperationException.class, () -> data.countries().add("Atlantis"));
    }
}
