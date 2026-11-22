package io.github.filipeoliv02.playergen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class AppTest {

    private record Result(int exitCode, String out, String err) {
    }

    private static Result run(String... args) {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();
        int code;
        try (var o = new PrintStream(out, true, StandardCharsets.UTF_8);
             var e = new PrintStream(err, true, StandardCharsets.UTF_8)) {
            code = App.run(args, o, e);
        }
        return new Result(code, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void defaultsToOnePlayer() {
        var options = App.Options.parse();
        assertEquals(1, options.count());
        assertNull(options.seed());
    }

    @Test
    void parsesCountAndSeed() {
        var options = App.Options.parse("--count", "3", "-s", "-42");
        assertEquals(3, options.count());
        assertEquals(-42L, options.seed());
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("-n", "0"));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("-n", "abc"));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("-n", String.valueOf(App.MAX_COUNT + 1)));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("-s"));
        assertThrows(IllegalArgumentException.class, () -> App.Options.parse("--bogus"));
    }

    @Test
    void printsRequestedNumberOfPlayers() {
        var result = run("-n", "3", "-s", "5");
        assertEquals(0, result.exitCode());
        assertEquals(3, result.out().lines().filter(l -> l.startsWith("Name : ")).count());
        assertTrue(result.out().contains("Weak Foot Frequency: "));
        assertEquals("", result.err());
    }

    @Test
    void seededOutputIsReproducible() {
        assertEquals(run("-n", "2", "-s", "2024").out(), run("-n", "2", "-s", "2024").out());
    }

    @Test
    void usageErrorReturnsExitCode2() {
        var result = run("--nope");
        assertEquals(2, result.exitCode());
        assertTrue(result.err().contains("Unknown option"));
        assertTrue(result.err().contains("Usage:"));
    }

    @Test
    void helpPrintsUsage() {
        var result = run("-h");
        assertEquals(0, result.exitCode());
        assertTrue(result.out().startsWith("Usage:"));
    }
}
