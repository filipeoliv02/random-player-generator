package filipeoliv02;

public class Utils {
    public static int randomNumberGenerator(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static double randomNumberGeneratorDouble(double min, double max) {
        return (Math.random() * (max - min) + min);
    }
}
