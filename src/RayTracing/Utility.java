package RayTracing;

import java.util.concurrent.ThreadLocalRandom;

public class Utility {
    public static final double infinity = Double.POSITIVE_INFINITY;

    public static final double negativeInfinity = Double.NEGATIVE_INFINITY;

    public static double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double low, double high) {
        return ThreadLocalRandom.current().nextDouble(low, high);
    }

    public static int randomInt(int low, int high) {
        return ThreadLocalRandom.current().nextInt(low, high);
    }

    public static double clamp(double x, double low, double high) {
        if(x<low) return low;
        else return Math.min(x, high);
    }

    public static int readInt() {
        return Integer.parseInt(System.console().readLine());
    }
}
