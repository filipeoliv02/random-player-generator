package io.github.filipeoliv02.playergen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PlayerGeneratorTest {

    private static final int SAMPLE_SIZE = 5_000;

    private static NameData data;
    private static List<Player> sample;

    @BeforeAll
    static void generateSample() {
        data = NameData.loadDefault();
        sample = new PlayerGenerator(data, new Random(1234)).next(SAMPLE_SIZE);
    }

    @Test
    void sameSeedProducesSamePlayers() {
        var first = new PlayerGenerator(data, new Random(42)).next(20);
        var second = new PlayerGenerator(data, new Random(42)).next(20);
        assertEquals(first, second);
    }

    @Test
    void differentSeedsProduceDifferentPlayers() {
        var first = new PlayerGenerator(data, new Random(1)).next(5);
        var second = new PlayerGenerator(data, new Random(2)).next(5);
        assertNotEquals(first, second);
    }

    @Test
    void ageAndHeightWithinBounds() {
        for (Player p : sample) {
            assertTrue(p.age() >= PlayerGenerator.MIN_AGE && p.age() <= PlayerGenerator.MAX_AGE, "age " + p.age());
            assertTrue(p.heightMetres() >= PlayerGenerator.MIN_HEIGHT && p.heightMetres() < PlayerGenerator.MAX_HEIGHT,
                    "height " + p.heightMetres());
        }
    }

    @Test
    void inclusiveBoundsAreReachable() {
        // Regression: the original implementation never produced the upper bound of any range.
        assertTrue(sample.stream().anyMatch(p -> p.age() == PlayerGenerator.MIN_AGE));
        assertTrue(sample.stream().anyMatch(p -> p.age() == PlayerGenerator.MAX_AGE));
        assertTrue(sample.stream().anyMatch(p -> p.stats().get(Attribute.WEAK_FOOT_ACCURACY) == 8));
        assertTrue(sample.stream().anyMatch(p -> p.stats().get(Attribute.BODY_BALANCE) == 99));
    }

    @Test
    void weightMatchesBodyType() {
        for (Player p : sample) {
            assertEquals(p.bodyType().weightFor(p.heightMetres()), p.weightKg(), 1e-9);
        }
        assertEquals(EnumSet.allOf(BodyType.class), collect(sample, Player::bodyType, BodyType.class));
        assertEquals(EnumSet.allOf(Foot.class), collect(sample, Player::favouriteFoot, Foot.class));
    }

    @Test
    void everyStatWithinItsScale() {
        for (Player p : sample) {
            for (Attribute a : Attribute.values()) {
                int value = p.stats().get(a);
                assertTrue(a.scale().contains(value), a + "=" + value);
            }
        }
    }

    @Test
    void weightedStatCoversWholeRangeWithoutGaps() {
        // Regression: the original produced no 60, 70, 80, 90 or 99.
        var random = new Random(7);
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < 50_000; i++) {
            int v = PlayerGenerator.weightedStat(random);
            assertTrue(v >= 50 && v <= 99, "stat " + v);
            seen.add(v);
        }
        assertEquals(50, seen.size(), "every value 50..99 should occur");
    }

    @Test
    void weightedStatFavoursLowerBands() {
        var random = new Random(99);
        int[] bands = new int[5];
        int n = 60_000;
        for (int i = 0; i < n; i++) {
            bands[(PlayerGenerator.weightedStat(random) - 50) / 10]++;
        }
        double[] expected = {10 / 30.0, 8 / 30.0, 6 / 30.0, 4 / 30.0, 2 / 30.0};
        for (int b = 0; b < bands.length; b++) {
            assertEquals(expected[b], bands[b] / (double) n, 0.01, "band " + b);
        }
    }

    @Test
    void weightedAliasTablesPreserveExactProbabilities() {
        var bands = new AliasTable<>(List.of(50, 60, 70, 80, 90), new int[]{10, 8, 6, 4, 2});
        long denominator = bands.massDenominator();
        assertEquals(10L * 5, bands.massNumerator(0));
        assertEquals(8L * 5, bands.massNumerator(1));
        assertEquals(6L * 5, bands.massNumerator(2));
        assertEquals(4L * 5, bands.massNumerator(3));
        assertEquals(2L * 5, bands.massNumerator(4));
        assertEquals(30L * 5, denominator);

        var bodyTypes = new AliasTable<>(List.of(BodyType.THIN, BodyType.NORMAL, BodyType.FAT),
                new int[]{1, 2, 1});
        assertEquals(List.of(3L, 6L, 3L),
                bodyTypes.outcomes().stream().map(type -> bodyTypes.massNumerator(bodyTypes.outcomes().indexOf(type))).toList());

        var feet = new AliasTable<>(List.of(Foot.RIGHT, Foot.LEFT, Foot.BOTH), new int[]{2, 1, 1});
        assertEquals(List.of(6L, 3L, 3L),
                feet.outcomes().stream().map(type -> feet.massNumerator(feet.outcomes().indexOf(type))).toList());
    }

    @Test
    void specialAbilitiesAreDistinctAndAtMostTwo() {
        Set<Integer> counts = new HashSet<>();
        for (Player p : sample) {
            var abilities = p.specialAbilities();
            assertTrue(abilities.size() <= PlayerGenerator.MAX_SPECIAL_ABILITIES);
            assertEquals(abilities.size(), Set.copyOf(abilities).size(), "duplicates in " + abilities);
            counts.add(abilities.size());
        }
        assertEquals(Set.of(0, 1, 2), counts);
    }

    @Test
    void namesAndCountriesComeFromTheData() {
        Set<String> firstNames = Set.copyOf(data.firstNames());
        Set<String> surnames = Set.copyOf(data.surnames());
        Set<String> countries = Set.copyOf(data.countries());
        for (Player p : sample) {
            String[] parts = p.name().split(" ", 2);
            assertTrue(firstNames.contains(parts[0]), p.name());
            assertTrue(surnames.contains(parts[1]), p.name());
            assertTrue(countries.contains(p.country()), p.country());
        }
    }

    @Test
    void canPickFirstAndLastEntryOfEachList() {
        // Regression: the original never selected the last line of a file.
        var tiny = new NameData(List.of("A", "B"), List.of("X", "Y"), List.of("P", "Q"));
        var players = new PlayerGenerator(tiny, new Random(3)).next(200);
        assertEquals(Set.of("A X", "A Y", "B X", "B Y"), collectStrings(players.stream().map(Player::name).toList()));
        assertEquals(Set.of("P", "Q"), collectStrings(players.stream().map(Player::country).toList()));
    }

    @Test
    void rejectsNegativeCount() {
        var generator = new PlayerGenerator(data, new Random());
        assertThrows(IllegalArgumentException.class, () -> generator.next(-1));
    }

    private static <E extends Enum<E>> Set<E> collect(List<Player> players,
                                                      java.util.function.Function<Player, E> f, Class<E> type) {
        var set = EnumSet.noneOf(type);
        players.forEach(p -> set.add(f.apply(p)));
        return set;
    }

    private static Set<String> collectStrings(List<String> values) {
        return new HashSet<>(values);
    }
}
