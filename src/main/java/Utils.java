package main.java;

import java.util.Random;

public class Utils {

    public static int randomNumberGenerator(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static double randomNumberGeneratorDouble(double min, double max) {
        return (Math.random() * (max - min) + min);
    }

    public static int generateStat() {
        return switch (randomNumberGenerator(1, 30)) {
            // 50 - 60
            case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> randomNumberGenerator(50, 60);
            // 61 - 70
            case 11, 12, 13, 14, 15, 16, 17, 18 -> randomNumberGenerator(61, 70);
            // 71 - 80
            case 19, 20, 21, 22, 23, 24 -> randomNumberGenerator(71, 80);
            // 81 - 90
            case 25, 26, 27, 28 -> randomNumberGenerator(81, 90);
            // 91 - 100
            case 29, 30 -> randomNumberGenerator(91, 99);
            default -> 0;
        };
    }

    public static int[] specialStatChooser(int numberOfAttributes) {
        Random rand = new Random();
        int numToChoose = rand.nextInt(3);

        if (numToChoose == 0) {

            return new int[]{};
        }else if (numToChoose == 1) {
            int chosenNumber = rand.nextInt(numberOfAttributes);

            return new int[]{chosenNumber};
        }else {
            int chosenNumber = rand.nextInt(numberOfAttributes);
            int chosenNumber1;

            do {
                chosenNumber1 = rand.nextInt(numberOfAttributes);
            } while (chosenNumber == chosenNumber1);

            return new int[]{chosenNumber, chosenNumber1};
        }
    }
}
