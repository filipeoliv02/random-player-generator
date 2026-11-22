package io.github.filipeoliv02.playergen;

/**
 * Body type of a player. Weight is derived from height using a target BMI.
 */
public enum BodyType {
    THIN("Thin", 22),
    NORMAL("Normal", 25),
    FAT("Fat", 30);

    private final String displayName;
    private final double bmi;

    BodyType(String displayName, double bmi) {
        this.displayName = displayName;
        this.bmi = bmi;
    }

    public String displayName() {
        return displayName;
    }

    /** Weight in kg for the given height in metres. */
    public double weightFor(double heightMetres) {
        return heightMetres * heightMetres * bmi;
    }
}
