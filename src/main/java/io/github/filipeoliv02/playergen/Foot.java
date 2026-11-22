package io.github.filipeoliv02.playergen;

/** A player's preferred foot. */
public enum Foot {
    RIGHT("Right"),
    LEFT("Left"),
    BOTH("Both");

    private final String displayName;

    Foot(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
