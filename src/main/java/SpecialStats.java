package main.java;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static main.java.Utils.specialStatChooser;

public class SpecialStats {
    ArrayList<Field> playerSpecials = new ArrayList<>();
    public String Dribbling;
    public String TacticalDribble;
    public String Positioning;
    public String Reaction;
    public String PlayMaking;
    public String Passing;
    public String Scoring;
    public String oneOn1Score;
    public String PostPlayer;
    public String Lines;
    public String MiddleShooting;
    public String Side;
    public String Center;
    public String Penalties;
    public String oneTouchPass;
    public String Outside;
    public String Marking;
    public String Sliding;
    public String Covering;
    public String DefensiveLine;
    public String PenaltyStopper;
    public String oneOn1Stopper;
    public String LongThrow;

    public SpecialStats() {
        var attributes = this.getClass().getDeclaredFields();

        var chosenNumbers = specialStatChooser(attributes.length - 1);
        // if there are no special stats
        if (Arrays.equals(chosenNumbers, new int[]{})) {
            return;
        }

        for (int chosenNumber : chosenNumbers) {
            this.playerSpecials.add(attributes[chosenNumber + 1]);
        }
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();

        for (Field field : this.playerSpecials) {
            if (field.getType() == String.class)
                sb.append("⭐ ").append(SpecialStatsEnum.valueOf(field.getName()).getProperName()).append("\n");
        }

        return sb.toString();
    }
}
