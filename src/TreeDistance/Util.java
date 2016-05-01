package TreeDistance;

public class Util {

    public static INTTYPE_REST binom2(INTTYPE_REST n) {
        return ((n - 1) * n) / 2;
    }

    public static INTTYPE_REST binom3(INTTYPE_REST n) {
        return ((n - 2) * (n - 1) * n) / 6;
    }

    public static INTTYPE_N4 binom4(INTTYPE_REST n) {
        return ((INTTYPE_N4) (n - 3) * (n - 2) * (n - 1) * n) / 24;
    }
}