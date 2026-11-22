package io.github.filipeoliv02.playergen;

/**
 * The numeric attributes every generated player has, in display order.
 */
public enum Attribute {
    ATTACK("Attack", Scale.FULL),
    DEFENSE("Defense", Scale.FULL),
    BODY_BALANCE("Body Balance", Scale.WEIGHTED),
    STAMINA("Stamina", Scale.WEIGHTED),
    TOP_SPEED("Top Speed", Scale.WEIGHTED),
    ACCELERATION("Acceleration", Scale.WEIGHTED),
    RESPONSE("Response", Scale.WEIGHTED),
    AGILITY("Agility", Scale.WEIGHTED),
    DRIBBLE_ACCURACY("Dribble Accuracy", Scale.WEIGHTED),
    DRIBBLE_SPEED("Dribble Speed", Scale.WEIGHTED),
    SHORT_PASS_ACCURACY("Short Pass Accuracy", Scale.WEIGHTED),
    SHORT_PASS_SPEED("Short Pass Speed", Scale.WEIGHTED),
    LONG_PASS_ACCURACY("Long Pass Accuracy", Scale.WEIGHTED),
    LONG_PASS_SPEED("Long Pass Speed", Scale.WEIGHTED),
    SHOT_ACCURACY("Shot Accuracy", Scale.WEIGHTED),
    SHOT_POWER("Shot Power", Scale.WEIGHTED),
    SHOT_TECHNIQUE("Shot Technique", Scale.WEIGHTED),
    FREE_KICK_ACCURACY("Free Kick Accuracy", Scale.WEIGHTED),
    SWERVE("Swerve", Scale.WEIGHTED),
    HEADER("Header", Scale.WEIGHTED),
    JUMP("Jump", Scale.WEIGHTED),
    TECHNIQUE("Technique", Scale.WEIGHTED),
    MENTALITY("Mentality", Scale.WEIGHTED),
    KEEPER_SKILLS("Keeper Skills", Scale.WEIGHTED),
    TEAMWORK("Teamwork", Scale.WEIGHTED),
    CONDITION("Condition", Scale.WEIGHTED),
    WEAK_FOOT_ACCURACY("Weak Foot Accuracy", Scale.WEAK_FOOT),
    WEAK_FOOT_FREQUENCY("Weak Foot Frequency", Scale.WEAK_FOOT);

    /** How an attribute's value is distributed; bounds are inclusive. */
    public enum Scale {
        /** Uniform over 1..99. */
        FULL(1, 99),
        /** 50..99, skewed towards lower values (see {@link PlayerGenerator#weightedStat}). */
        WEIGHTED(50, 99),
        /** Uniform over 1..8. */
        WEAK_FOOT(1, 8);

        private final int min;
        private final int max;

        Scale(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int min() {
            return min;
        }

        public int max() {
            return max;
        }

        public boolean contains(int value) {
            return value >= min && value <= max;
        }
    }

    private final String displayName;
    private final Scale scale;

    Attribute(String displayName, Scale scale) {
        this.displayName = displayName;
        this.scale = scale;
    }

    public String displayName() {
        return displayName;
    }

    public Scale scale() {
        return scale;
    }
}
