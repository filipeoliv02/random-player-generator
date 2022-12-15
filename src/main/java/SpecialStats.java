package main.java;


public class SpecialStats {
    SpecialStatsEnum[] playerSpecials;

    public SpecialStats(SpecialStatsEnum[] specialStats) {
        this.playerSpecials = specialStats;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        for (SpecialStatsEnum stat : this.playerSpecials) {
            sb.append("⭐ ")
                    .append(stat.getProperName())
                    .append("\n");
        }

        return sb.toString();
    }
}
