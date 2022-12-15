package main.java;


import java.util.ArrayList;
import java.util.Arrays;

import static main.java.Utils.specialStatChooser;

public class SpecialStats {
    ArrayList<SpecialStatsEnum> playerSpecials = new ArrayList<>();


    public SpecialStats() {
        var attributes = SpecialStatsEnum.values();

        var chosenNumbers = specialStatChooser(attributes.length);
        // if there are no special stats
        if (Arrays.equals(chosenNumbers, new int[]{})) {
            return;
        }

        for (int chosenNumber : chosenNumbers) {
            this.playerSpecials.add(SpecialStatsEnum.values()[chosenNumber]);
        }
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();

        for (SpecialStatsEnum stat : this.playerSpecials) {
                sb.append("⭐ ").append(stat.getProperName()).append("\n");
        }

        return sb.toString();
    }
}
