package filipeoliv02;

public class App {
    public static void main(String[] args) {
        String namesPath = "lib/names.txt";
        String surnamesPath = "lib/surnames.txt";
        String countriesPath = "lib/countries.txt";
        int numberOfNames = Player.GetNumberOfLines(namesPath);
        int numberOfSurnames = Player.GetNumberOfLines(surnamesPath);
        int numberOfCountries = Player.GetNumberOfLines(countriesPath);
        for (int i = 0; i < 100; i++) {
            System.out.println(new Player(namesPath, numberOfNames, surnamesPath, numberOfSurnames, countriesPath, numberOfCountries));
            System.out.println("----------------------------------------");
        }

    }
}
