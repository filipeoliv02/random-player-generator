package filipeoliv02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class Player {
    public String name;
    public String country;
    public int age;
    public double height;
    public double weight;
    public String favoritefoot;
    public Stats stats;
    public SpecialStats specialStats;

    public Player(String namesPath, int numberOfNames, String surnamesPath, int numberOfSurnames, String countriesPath, int numberOfCountries) {
        this.name = nameGenerator(namesPath, numberOfNames) + " " + nameGenerator(surnamesPath, numberOfSurnames);
        this.country = nameGenerator(countriesPath, numberOfCountries);
        this.age = randomNumberGenerator(15, 80);
        this.height = randomNumberGeneratorDouble(1.3, 2.4);
        this.weight = randomNumberGeneratorDouble(40, 200);
        // favorite foot can vary from left or right or both
        this.favoritefoot = randomNumberGenerator(0, 2) == 0 ? "Right" : randomNumberGenerator(0, 2) == 1 ? "Left" : "Both";
    }

    public static int GetNumberOfLines(String filePath) {
        int numberOfLines = 0;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while (br.readLine() != null) {
                numberOfLines++;
            }
            br.close();
            return numberOfLines;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return -1;
    }

    private int randomNumberGenerator(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    private double randomNumberGeneratorDouble(double min, double max) {
        return (Math.random() * (max - min) + min);
    }

    private String nameGenerator(String filePath, int numberOfLines) {
        int randomNumber = randomNumberGenerator(1, numberOfLines);
        String name = "";
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            for (int i = 0; i < randomNumber; i++) {
                name = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public String toString() {
        return String.format("""
                        Name : %s
                        Country : %s
                        Age : %d years
                        Height : %.2f m
                        Weight : %.2f kg
                        Favorite Foot : %s
                        Stats : %s
                        Special Stats : %s
                        """,
                name, country, age, height, weight, favoritefoot, stats, specialStats);
    }
}
