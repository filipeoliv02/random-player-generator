package io.github.filipeoliv02.playergen;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Creates random {@link Player}s. All randomness comes from the supplied {@link RandomGenerator},
 * so a seeded generator (e.g. {@code new java.util.Random(42)}) yields reproducible players.
 * Not thread-safe unless the supplied generator is.
 */
public final class PlayerGenerator {

    static final int MIN_AGE = 17;
    static final int MAX_AGE = 45;
    static final double MIN_HEIGHT = 1.45;
    static final double MAX_HEIGHT = 2.10;
    static final int MAX_SPECIAL_ABILITIES = 2;

    private static final SpecialAbility[] ABILITIES = SpecialAbility.values();

    private final NameData data;
    private final RandomGenerator random;

    public PlayerGenerator(NameData data, RandomGenerator random) {
        this.data = Objects.requireNonNull(data, "data");
        this.random = Objects.requireNonNull(random, "random");
    }

    /** Generates one player. */
    public Player next() {
        String name = pick(data.firstNames()) + " " + pick(data.surnames());
        String country = pick(data.countries());
        int age = between(MIN_AGE, MAX_AGE);
        double height = random.nextDouble(MIN_HEIGHT, MAX_HEIGHT);
        BodyType bodyType = bodyType();
        return new Player(
                name,
                country,
                age,
                bodyType,
                height,
                bodyType.weightFor(height),
                foot(),
                stats(),
                specialAbilities());
    }

    /** Generates {@code count} players. */
    public List<Player> next(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0: " + count);
        }
        var players = new ArrayList<Player>(count);
        for (int i = 0; i < count; i++) {
            players.add(next());
        }
        return players;
    }

    private String pick(List<String> values) {
        return values.get(random.nextInt(values.size()));
    }

    /** Uniform integer in [min, max], both inclusive. */
    private int between(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    /** Thin 25%, Fat 25%, Normal 50% (same split as the original version). */
    private BodyType bodyType() {
        return switch (random.nextInt(4)) {
            case 0 -> BodyType.THIN;
            case 1 -> BodyType.FAT;
            default -> BodyType.NORMAL;
        };
    }

    /** Right 50%, Left 25%, Both 25% (same split as the original version). */
    private Foot foot() {
        return switch (random.nextInt(4)) {
            case 0, 1 -> Foot.RIGHT;
            case 2 -> Foot.LEFT;
            default -> Foot.BOTH;
        };
    }

    private Stats stats() {
        var values = new EnumMap<Attribute, Integer>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            Attribute.Scale scale = attribute.scale();
            int value = switch (scale) {
                case WEIGHTED -> weightedStat(random);
                case FULL, WEAK_FOOT -> between(scale.min(), scale.max());
            };
            values.put(attribute, value);
        }
        return new Stats(values);
    }

    /**
     * A stat in 50..99 where higher bands are progressively rarer:
     * 50-59 (10/30), 60-69 (8/30), 70-79 (6/30), 80-89 (4/30), 90-99 (2/30).
     */
    static int weightedStat(RandomGenerator random) {
        int bandStart = switch (random.nextInt(30)) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> 50;
            case 10, 11, 12, 13, 14, 15, 16, 17 -> 60;
            case 18, 19, 20, 21, 22, 23 -> 70;
            case 24, 25, 26, 27 -> 80;
            default -> 90;
        };
        return bandStart + random.nextInt(10);
    }

    /** Zero, one or two distinct special abilities, each count equally likely. */
    private List<SpecialAbility> specialAbilities() {
        int count = random.nextInt(MAX_SPECIAL_ABILITIES + 1);
        var chosen = new ArrayList<SpecialAbility>(count);
        while (chosen.size() < count) {
            SpecialAbility candidate = ABILITIES[random.nextInt(ABILITIES.length)];
            if (!chosen.contains(candidate)) {
                chosen.add(candidate);
            }
        }
        return chosen;
    }
}
