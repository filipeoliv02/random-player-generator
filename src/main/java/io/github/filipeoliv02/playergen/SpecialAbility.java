package io.github.filipeoliv02.playergen;

/** Special abilities ("stars") a player may have. */
public enum SpecialAbility {
    DRIBBLING("Dribbling"),
    TACTICAL_DRIBBLE("Tactical Dribble"),
    POSITIONING("Positioning"),
    REACTION("Reaction"),
    PLAYMAKING("PlayMaking"),
    PASSING("Passing"),
    SCORING("Scoring"),
    ONE_ON_ONE_SCORING("1-on-1 Scoring"),
    POST_PLAYER("Post Player"),
    LINES("Lines"),
    MIDDLE_SHOOTING("Middle Shooting"),
    SIDE("Side"),
    CENTER("Center"),
    PENALTIES("Penalties"),
    ONE_TOUCH_PASS("1-Touch Pass"),
    OUTSIDE("Outside"),
    MARKING("Marking"),
    SLIDING("Sliding"),
    COVERING("Covering"),
    DEFENSIVE_LINE("Defensive Line"),
    PENALTY_STOPPER("Penalty Stopper"),
    ONE_ON_ONE_STOPPER("1-on-1 Stopper"),
    LONG_THROW("Long Throw");

    private final String displayName;

    SpecialAbility(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
