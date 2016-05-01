package TreeDistance;

public class Util {
    
    public static long binom2(long n) {
        return ((n - 1) * n) / 2;
    }

    public static long binom3(long n) {
        return ((n - 2) * (n - 1) * n) / 6;
    }

    public static long binom4(long n) { return ((long) (n - 3) * (n - 2) * (n - 1) * n) / 24;
    }
}