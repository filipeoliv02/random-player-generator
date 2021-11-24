package filipeoliv02;

public class Utils {

  public static int randomNumberGenerator(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }

  public static double randomNumberGeneratorDouble(double min, double max) {
    return (Math.random() * (max - min) + min);
  }

  public static int generateStat() {
    switch (randomNumberGenerator(1, 30)) {
      // 50 - 60
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
        return randomNumberGenerator(50, 60);
      // 61 - 70
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
        return randomNumberGenerator(61, 70);
      // 71 - 80
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
        return randomNumberGenerator(71, 80);
      // 81 - 90
      case 25:
      case 26:
      case 27:
      case 28:
        return randomNumberGenerator(81, 90);
      // 91 - 100
      case 29:
      case 30:
        return randomNumberGenerator(91, 99);
      default:
        return 0;
    }
  }
}
