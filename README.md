# Random Player Generator

[![CI](https://github.com/filipeoliv02/random-player-generator/actions/workflows/ci.yml/badge.svg)](https://github.com/filipeoliv02/random-player-generator/actions/workflows/ci.yml)

A small Java command-line tool that makes up random football players. Each player gets a name,
nationality, age, body type, height, weight, preferred foot, 28 numeric attributes and up to two
special abilities. The attribute set is modelled on classic mid-2000s football games (PES 2006 era).

Pass a seed and you get the same players every time, which is handy for tests, demos or sharing a
"squad".

## Algorithms & techniques demonstrated

Source files live in `src/main/java/io/github/filipeoliv02/playergen/` (abbreviated `…/` below).

| Technique | What it is | How it is applied here | Where |
|-----------|------------|------------------------|-------|
| Weighted random selection (discrete distribution) | Draw an integer uniformly from a range sized to the total weight and map sub-ranges to outcomes. | `weightedStat` draws from `[0, 30)` and maps 10/8/6/4/2 slots to the 50s…90s bands, then adds a uniform offset 0–9. Body type (1:2:1) and foot (2:1:1) use the same idea with 4 slots. O(1) per draw, exact probabilities, no floating point. | […/PlayerGenerator.java](src/main/java/io/github/filipeoliv02/playergen/PlayerGenerator.java) |
| Two-stage sampling | Pick a bucket by weight, then sample uniformly inside it. | Produces a skewed distribution over 50–99 where every value is still reachable (verified by a test that sees all 50 values). | […/PlayerGenerator.java](src/main/java/io/github/filipeoliv02/playergen/PlayerGenerator.java) |
| Uniform sampling with inclusive bounds | `nextInt(min, max + 1)` so both ends are reachable. | Used for age, attack/defense and weak-foot stats; fixes an off-by-one in the original version (regression tests check the upper bounds are hit). | […/PlayerGenerator.java](src/main/java/io/github/filipeoliv02/playergen/PlayerGenerator.java) |
| Sampling without replacement (rejection) | Draw, discard duplicates, repeat. | Special abilities: 0–2 distinct picks out of 23. With k ≪ n the expected number of retries is negligible; for large k a partial Fisher–Yates shuffle would be the better choice. | […/PlayerGenerator.java](src/main/java/io/github/filipeoliv02/playergen/PlayerGenerator.java) |
| Seeded PRNG via dependency injection | Inject the `RandomGenerator` instead of creating one internally. | `PlayerGenerator` takes any `RandomGenerator`; the CLI passes `new java.util.Random(seed)`, whose LCG algorithm is fully specified, so a seed reproduces the same players on any JDK. Tests use fixed seeds for determinism. | […/PlayerGenerator.java](src/main/java/io/github/filipeoliv02/playergen/PlayerGenerator.java), […/App.java](src/main/java/io/github/filipeoliv02/playergen/App.java) |
| Immutable records with validating compact constructors | Java `record`s that check invariants and take defensive copies. | `Player`, `Stats` and `NameData` reject nulls / empty lists / out-of-range values and wrap collections with `List.copyOf` or an unmodifiable map, so a constructed object is always valid. | […/Player.java](src/main/java/io/github/filipeoliv02/playergen/Player.java), […/Stats.java](src/main/java/io/github/filipeoliv02/playergen/Stats.java), […/NameData.java](src/main/java/io/github/filipeoliv02/playergen/NameData.java) |
| `EnumMap` | Array-backed map keyed by enum ordinal. | Attribute values are stored in an `EnumMap`: O(1) access, compact, and iteration follows declaration order, which gives a stable display order for free. | […/Stats.java](src/main/java/io/github/filipeoliv02/playergen/Stats.java) |
| Data-driven enums | Enums that carry their own parameters and behaviour. | Each `Attribute` holds its `Scale` (range + distribution); `BodyType` holds a target BMI and computes weight = height² × BMI. Generation logic switches on the scale instead of hard-coding 28 cases. | […/Attribute.java](src/main/java/io/github/filipeoliv02/playergen/Attribute.java), […/BodyType.java](src/main/java/io/github/filipeoliv02/playergen/BodyType.java) |
| Bounded argument parsing | Hand-written parser that validates every value and range. | `Options.parse` handles short/long flags, missing values, non-numeric input and `--count` limited to 1–10000; errors map to exit code 2. `run` takes `PrintStream`s so it is testable without spawning a process. | […/App.java](src/main/java/io/github/filipeoliv02/playergen/App.java) |
| Classpath resource loading | Read bundled data with an explicit charset. | Word lists are read as UTF-8 from the jar (works from any directory), stripped and filtered with streams; missing resources fail with a clear message. | […/NameData.java](src/main/java/io/github/filipeoliv02/playergen/NameData.java) |
| Statistical and regression tests | Assert distribution properties over large seeded samples. | Tests check band frequencies within ±1%, full range coverage, inclusive bounds, no duplicate abilities, and seed reproducibility. | [PlayerGeneratorTest.java](src/test/java/io/github/filipeoliv02/playergen/PlayerGeneratorTest.java) |

## Sample output

```text
$ java -jar target/random-player-generator.jar --seed 42
Name : Isidore Cain
Country : North Korea
Age : 32 years
Body Type : Fat
Height : 1.65 m
Weight : 81.74 kg
Favorite Foot : Left
----------------------Stats-----------------------
Attack: 44
Defense: 84
Body Balance: 52
Stamina: 52
Top Speed: 82
...
Weak Foot Accuracy: 3
Weak Foot Frequency: 8
------------------Special Stats-------------------
⭐ Tactical Dribble
```

## Requirements

- Java 21 or newer
- Maven 3.9 or newer (only needed to build)

## Build, test and run

```bash
mvn verify                                   # compile, run the tests, build target/random-player-generator.jar
java -jar target/random-player-generator.jar # one random player
java -jar target/random-player-generator.jar --count 11 --seed 2006   # a reproducible XI
java -jar target/random-player-generator.jar --help
```

The word lists are bundled inside the jar, so you can run it from any directory.

### No local JDK? Use Docker

```bash
docker run --rm -v "$PWD":/src -w /src maven:3-eclipse-temurin-21 mvn -B verify
docker run --rm -v "$PWD/target":/app eclipse-temurin:21-jre java -jar /app/random-player-generator.jar -n 3
```

### Options

| Option | Description |
|---|---|
| `-n`, `--count N` | Number of players to generate (1–10000, default 1) |
| `-s`, `--seed S` | 64-bit seed. The same seed gives the same output on any JDK |
| `-h`, `--help` | Show usage |

Exit code `2` means invalid arguments.

## How players are generated

| Property | Rule |
|---|---|
| Name | Random first name + random surname from `src/main/resources/data/` |
| Country | Random entry from `countries.txt` |
| Age | 17–45 |
| Height | 1.45–2.10 m |
| Body type and weight | Thin (25%, BMI 22), Normal (50%, BMI 25), Fat (25%, BMI 30); weight = height² × BMI |
| Favourite foot | Right 50%, Left 25%, Both 25% |
| Attack, Defense | Uniform 1–99 |
| Other attributes | 50–99, weighted towards lower values: 50s 33%, 60s 27%, 70s 20%, 80s 13%, 90s 7% |
| Weak foot accuracy / frequency | 1–8 |
| Special abilities | 0, 1 or 2 (equally likely), no duplicates |

All ranges include both ends.

## Project layout

```text
src/main/java/io/github/filipeoliv02/playergen/
  App.java              CLI entry point and argument parsing
  PlayerGenerator.java  All the random generation rules
  Player.java           Immutable player record + text card
  Stats.java            Immutable, validated attribute values
  Attribute.java        The 28 attributes and their value scales
  BodyType.java, Foot.java, SpecialAbility.java
  NameData.java         Loads the word lists from the classpath (UTF-8)
src/main/resources/data/  names.txt, surnames.txt, countries.txt
src/test/java/...         JUnit Jupiter tests
```

## Attribute glossary

- **Attack / Defense**: how well the player reads the game going forward or without the ball.
- **Body Balance**: physical strength, staying on the ball under challenge.
- **Stamina / Condition**: endurance over a match, and current fitness.
- **Top Speed / Acceleration / Agility / Response**: running speed, how quickly the player gets there, how fast they turn, and their reflexes.
- **Dribble, Short Pass, Long Pass (Accuracy / Speed)**: control and pace on the ball and in passing.
- **Shot Accuracy / Power / Technique, Free Kick Accuracy, Swerve**: finishing and set pieces.
- **Header / Jump**: aerial ability.
- **Technique**: first touch and ball control.
- **Mentality / Teamwork**: composure under pressure, and movement that supports teammates.
- **Keeper Skills**: goalkeeping ability.
- **Weak Foot Accuracy / Frequency**: how good the weaker foot is, and how often it gets used.

## CI

GitHub Actions (`.github/workflows/ci.yml`) builds and tests every push and pull request on JDK 21.
Dependabot keeps Maven dependencies, Maven plugins and GitHub Actions up to date.
