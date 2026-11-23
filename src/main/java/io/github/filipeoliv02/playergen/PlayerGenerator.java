package io.github.filipeoliv02.playergen;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final AliasTable<Integer> STATIC_WEIGHTED_BANDS =
            new AliasTable<>(List.of(50, 60, 70, 80, 90), new int[]{10, 8, 6, 4, 2});

    private final NameData data;
    private final RandomGenerator random;
    private final AliasTable<Integer> weightedBands;
    private final AliasTable<BodyType> bodyTypes;
    private final AliasTable<Foot> feet;

    public PlayerGenerator(NameData data, RandomGenerator random) {
        this.data = Objects.requireNonNull(data, "data");
        this.random = Objects.requireNonNull(random, "random");
        weightedBands = new AliasTable<>(List.of(50, 60, 70, 80, 90), new int[]{10, 8, 6, 4, 2});
        bodyTypes = new AliasTable<>(List.of(BodyType.THIN, BodyType.NORMAL, BodyType.FAT),
                new int[]{1, 2, 1});
        feet = new AliasTable<>(List.of(Foot.RIGHT, Foot.LEFT, Foot.BOTH), new int[]{2, 1, 1});
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

    /** Thin 25%, Normal 50%, Fat 25%. */
    private BodyType bodyType() {
        return bodyTypes.sample(random);
    }

    /** Right 50%, Left 25%, Both 25%. */
    private Foot foot() {
        return feet.sample(random);
    }

    private Stats stats() {
        var values = new EnumMap<Attribute, Integer>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            Attribute.Scale scale = attribute.scale();
            int value = switch (scale) {
                case WEIGHTED -> weightedBands.sample(random) + random.nextInt(10);
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
        return STATIC_WEIGHTED_BANDS.sample(random) + random.nextInt(10);
    }

    /** Zero, one or two distinct special abilities, each count equally likely. */
    private List<SpecialAbility> specialAbilities() {
        int count = random.nextInt(MAX_SPECIAL_ABILITIES + 1);
        var shuffle = new ArrayList<SpecialAbility>(ABILITIES.length);
        Collections.addAll(shuffle, ABILITIES);
        for (int i = 0; i < count; i++) {
            int j = random.nextInt(ABILITIES.length - i) + i;
            Collections.swap(shuffle, i, j);
        }
        return List.copyOf(shuffle.subList(0, count));
    }
}
