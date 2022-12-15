package main.java;

public class App {
    public static void main(String[] args) throws IllegalAccessException {
        String namesPath = "lib/names.txt";
        String surnamesPath = "lib/surnames.txt";
        String countriesPath = "lib/countries.txt";
        int numberOfNames = Player.GetNumberOfLines(namesPath);
        int numberOfSurnames = Player.GetNumberOfLines(surnamesPath);
        int numberOfCountries = Player.GetNumberOfLines(countriesPath);
        Player player = new Player(namesPath, numberOfNames, surnamesPath, numberOfSurnames, countriesPath, numberOfCountries);
        System.out.println(player);

    }
}
