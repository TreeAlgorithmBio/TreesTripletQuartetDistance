package TreeDistance;

public class GlobalMembers {


    /*public static class__declspec(dllexport UnnamedParameter1)TripletDistanceCalculator {

        public static <T> void assert_equal (T val, T exp)
        {
            assert_equal(val, exp, "Assertion failed!");
        }*/
//ORIGINAL LINE: void assert_equal(const T &val, const T &exp, const String msg = "Assertion failed!")



    public static void usage(String programName) {
        System.out.print("Usage: ");
        System.out.print(programName);
        System.out.print(" [-v] <filename1> <filename2>");
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("Where <filename1> and <filename2> point to two files each containing one");
        System.out.print("\n");
        System.out.print("tree in Newick format. In both trees all leaves should be labeled and the");
        System.out.print("\n");
        System.out.print("two trees should have the same set of leaf labels.");
        System.out.print("\n");
        System.out.print("The triplet distance between the two trees will be printed to stdout.");
        System.out.print("\n");
        System.out.print("If the -v option is used, the following numbers will be reported (in this");
        System.out.print("\n");
        System.out.print("order):");
        System.out.print("\n");
        System.out.print("\t - The number of leaves in the trees (should be the same for both).");
        System.out.print("\n");
        System.out.print("\t - The number of triplets in the two trees (n choose 3).");
        System.out.print("\n");
        System.out.print("\t - The triplet distance between the two trees.");
        System.out.print("\n");
        System.out.print("\t - The normalized triplet distance between the two trees.");
        System.out.print("\n");
        System.out.print("\t - The number of resolved triplets that agree in the two trees.");
        System.out.print("\n");
        System.out.print("\t - The normalized number of resolved triplets that agree in the two trees.");
        System.out.print("\n");
        System.out.print("\t - The number triplets that are unresolved in both trees.");
        System.out.print("\n");
        System.out.print("\t - The normalized number triplets that are unresolved in both trees.");
        System.out.print("\n");
        System.out.print("\n");
    }

        public static void main(int argc, String[] args) {

        if (argc == 1) usage(args[0]);

        boolean verbose = false;

        if (argc < 3) {
            System.out.println("Error: Not enough parameters!");
            usage(args[0]);

        }

        if (argc == 4 && !args[1].equals("-v")) {
            verbose = true;
        }

        String filename1 = args[argc - 2];
        String filename2 = args[argc - 1];

        TripletDistanceCalculator tripletCalc = new TripletDistanceCalculator();
        long dist = tripletCalc.calculateTripletDistance(filename1, filename2);

        if (dist == -1) {
            System.exit(-1);
        }

        if (!verbose) {
            System.out.print(dist);
            System.out.print("\n");
        } else {
            long n = tripletCalc.get_n();
            long totalNoTriplets = tripletCalc.get_totalNoTriplets();
            long resolved = tripletCalc.get_resolvedTriplets();
            long unresolved = tripletCalc.get_unresolvedTriplets();
            double dist_norm = (double) dist / (double) totalNoTriplets;
            double resolved_norm = (double) resolved / (double) totalNoTriplets;
            double unresolved_norm = (double) unresolved / (double) totalNoTriplets;

            System.out.print(n);
            System.out.print("\t");
            System.out.print(totalNoTriplets);
            System.out.print("\t");
            System.out.print(dist);
            System.out.print("\t");
            System.out.print(dist_norm);
            System.out.print("\t");
            System.out.print(resolved);
            System.out.print("\t");
            System.out.print(resolved_norm);
            System.out.print("\t");
            System.out.print(unresolved);
            System.out.print("\t");
            System.out.print(unresolved_norm);
            System.out.print("\n");
        }

    }

    public static <T> void assert_equal(T val, T exp, String msg) {
        if (val != exp) {

            System.out.println("msg.compareTo() < 0 < Found << val <<  but expected << exp ");
            System.exit(-1);
        }
    }
}