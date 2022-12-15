package main.java;

public enum SpecialStatsEnum {
    Dribbling("Dribbling"),
    TacticalDribble("Tactical Dribble"),
    Positioning("Positioning"),
    Reaction("Reaction"),
    PlayMaking("PlayMaking"),
    Passing("Passing"),
    Scoring("Scoring"),
    oneOn1Score("1-on-1 Scoring"),
    PostPlayer("Post Player"),
    Lines("Lines"),
    MiddleShooting("Middle Shooting"),
    Side("Side"),
    Center("Center"),
    Penalties("Penalties"),
    oneTouchPass("1-Touch Pass"),
    Outside("Outside"),
    Marking("Marking"),
    Sliding("Sliding"),
    Covering("Covering"),
    DefensiveLine("Defensive Line"),
    PenaltyStopper("Penalty Stopper"),
    oneOn1Stopper("1-on-1 Stopper"),
    LongThrow("Long Throw");

    private final String properName;

    SpecialStatsEnum(String name) {
        this.properName = name;
    }

    public String getProperName() {
        return properName;
    }
}
