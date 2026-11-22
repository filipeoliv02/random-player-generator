package io.github.filipeoliv02.playergen;

import java.io.PrintStream;
import java.util.Random;

/**
 * Command-line entry point.
 *
 * <pre>
 * java -jar random-player-generator.jar [-n COUNT] [-s SEED]
 * </pre>
 */
public final class App {

    static final int MAX_COUNT = 10_000;

    static final String USAGE = """
            Usage: java -jar random-player-generator.jar [options]

              -n, --count N   number of players to generate (1-%d, default 1)
              -s, --seed S    seed (64-bit integer) for reproducible output
              -h, --help      show this help
            """.formatted(MAX_COUNT);

    /** Parsed command-line options. {@code seed} is null when no seed was given. */
    record Options(int count, Long seed, boolean help) {

        static Options parse(String... args) {
            int count = 1;
            Long seed = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-h", "--help" -> {
                        return new Options(count, seed, true);
                    }
                    case "-n", "--count" -> count = parseCount(valueAfter(args, ++i, arg));
                    case "-s", "--seed" -> seed = parseSeed(valueAfter(args, ++i, arg));
                    default -> throw new IllegalArgumentException("Unknown option: " + arg);
                }
            }
            return new Options(count, seed, false);
        }

        private static String valueAfter(String[] args, int index, String option) {
            if (index >= args.length) {
                throw new IllegalArgumentException("Missing value for " + option);
            }
            return args[index];
        }

        private static int parseCount(String value) {
            int count;
            try {
                count = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Count must be an integer: " + value, e);
            }
            if (count < 1 || count > MAX_COUNT) {
                throw new IllegalArgumentException("Count must be between 1 and " + MAX_COUNT + ": " + value);
            }
            return count;
        }

        private static long parseSeed(String value) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Seed must be a 64-bit integer: " + value, e);
            }
        }
    }

    private App() {
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    /** Runs the CLI and returns the process exit code. */
    static int run(String[] args, PrintStream out, PrintStream err) {
        Options options;
        try {
            options = Options.parse(args);
        } catch (IllegalArgumentException e) {
            err.println("Error: " + e.getMessage());
            err.print(USAGE);
            return 2;
        }
        if (options.help()) {
            out.print(USAGE);
            return 0;
        }

        // java.util.Random's algorithm is fully specified, so a given seed gives the same players on any JDK.
        Random random = options.seed() == null ? new Random() : new Random(options.seed());
        var generator = new PlayerGenerator(NameData.loadDefault(), random);
        for (int i = 0; i < options.count(); i++) {
            if (i > 0) {
                out.println();
            }
            out.print(generator.next().describe());
        }
        out.flush();
        return 0;
    }
}
