package main.java;


import java.lang.reflect.Field;
import java.util.Arrays;

import static main.java.Utils.specialStatChooser;

public class SpecialStats {
    public boolean Dribbling;
    public boolean TacticalDrible;
    public boolean Positioning;
    public boolean Reaction;
    public boolean Playmaking;
    public boolean Passing;
    public boolean Scoring;
    public boolean oneon1Score;
    public boolean PostPlayer;
    public boolean Lines;
    public boolean MiddleShooting;
    public boolean Side;
    public boolean Centre;
    public boolean Penalties;
    public boolean oneTouchPass;
    public boolean Outside;
    public boolean Marking;
    public boolean Sliding;
    public boolean Covering;
    public boolean DefensiveLine;
    public boolean PenaltyStopper;
    public boolean oneon1Stopper;
    public boolean LongThrow;

    public SpecialStats() throws IllegalAccessException {
        var attributes = this.getClass().getDeclaredFields();

        var chosenNumbers = specialStatChooser(attributes.length);
        // if there are no special stats
        if (Arrays.equals(chosenNumbers, new int[]{})) {
            return;
        }

        for (int chosenNumber : chosenNumbers) {
            attributes[chosenNumber].setAccessible(true);
            attributes[chosenNumber].set(this, true);
        }
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();

        Field[] fields = getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.getType() == boolean.class && field.getBoolean(this)) {
                    sb.append(field.getName()).append("\n");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return sb.toString();
    }
}
