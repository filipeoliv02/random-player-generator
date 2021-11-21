package filipeoliv02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static filipeoliv02.Utils.randomNumberGenerator;
import static filipeoliv02.Utils.randomNumberGeneratorDouble;

public class Player {
    public String name;
    public String country;
    public int age;
    public int bodyType;
    public double height;
    public double weight;
    public String favoritefoot;
    public Stats stats;
    public SpecialStats specialStats;

    public Player(String namesPath, int numberOfNames, String surnamesPath, int numberOfSurnames, String countriesPath, int numberOfCountries) {
        this.name = nameGenerator(namesPath, numberOfNames) + " " + nameGenerator(surnamesPath, numberOfSurnames);
        this.country = nameGenerator(countriesPath, numberOfCountries);
        this.age = randomNumberGenerator(17, 46);
        this.height = randomNumberGeneratorDouble(1.45, 2.1);
        this.bodyType = randomNumberGenerator(1, 5);
        if (bodyType == 1) {
            this.weight = calculateWeightThin(height);
        } else if (bodyType == 2) {
            this.weight = calculateWeightFat(height);
        } else {
            this.weight = calculateWeightNormal(height);
        }
        this.favoritefoot = randomNumberGenerator(0, 2) == 0 ? "Right" : randomNumberGenerator(0, 2) == 1 ? "Left" : "Both";
        this.stats = new Stats();
        this.specialStats = new SpecialStats();
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

    public double calculateWeightThin(double height) {
        return height * height * 22;
    }

    public double calculateWeightNormal(double height) {
        return height * height * 25;
    }

    public double calculateWeightFat(double height) {
        return height * height * 30;
    }

    @Override
    public String toString() {
        String bodyTypeT = "";
        if (bodyType == 1) {
            bodyTypeT = "Thin";
        } else if (bodyType == 2) {
            bodyTypeT = "Fat";
        } else {
            bodyTypeT = "Normal";
        }
        return String.format("""
                        Name : %s
                        Country : %s
                        Age : %d years
                        Body Type : %s
                        Height : %.2f m
                        Weight : %.2f kg
                        Favorite Foot : %s
                        ----------------------Stats-----------------------
                        %s
                        ------------------Special Stats-------------------
                        %s
                        """,
                name, country, age, bodyTypeT, height, weight, favoritefoot, stats, specialStats);
    }
}
