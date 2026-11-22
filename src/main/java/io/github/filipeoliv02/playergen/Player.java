package io.github.filipeoliv02.playergen;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A generated player. Immutable.
 *
 * @param name             first name and surname
 * @param country          nationality
 * @param age              age in years
 * @param bodyType         body type, used to derive the weight
 * @param heightMetres     height in metres
 * @param weightKg         weight in kilograms
 * @param favouriteFoot    preferred foot
 * @param stats            numeric attributes
 * @param specialAbilities zero or more distinct special abilities
 */
public record Player(
        String name,
        String country,
        int age,
        BodyType bodyType,
        double heightMetres,
        double weightKg,
        Foot favouriteFoot,
        Stats stats,
        List<SpecialAbility> specialAbilities) {

    public Player {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(country, "country");
        Objects.requireNonNull(bodyType, "bodyType");
        Objects.requireNonNull(favouriteFoot, "favouriteFoot");
        Objects.requireNonNull(stats, "stats");
        specialAbilities = List.copyOf(specialAbilities);
    }

    /** Human-readable, multi-line player card. Numbers are formatted locale-independently. */
    public String describe() {
        String specials = specialAbilities.isEmpty()
                ? "(none)"
                : specialAbilities.stream()
                        .map(s -> "⭐ " + s.displayName())
                        .collect(Collectors.joining("\n"));
        return String.format(Locale.ROOT, """
                        Name : %s
                        Country : %s
                        Age : %d years
                        Body Type : %s
                        Height : %.2f m
                        Weight : %.2f kg
                        Favorite Foot : %s
                        ----------------------Stats-----------------------
                        %s
                        ------------------Special Stats-------------------
                        %s
                        """,
                name, country, age, bodyType.displayName(), heightMetres, weightKg,
                favouriteFoot.displayName(), stats, specials);
    }
}
