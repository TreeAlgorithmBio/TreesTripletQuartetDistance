package TreeDistance;

public class GlobalMembers {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#if _WIN32
//C++ TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
    public static class__declspec(dllexport UnnamedParameter1)TripletDistanceCalculator {
        ///#endif
        public static <T> void assert_equal (T val, T exp)
        {
            assert_equal(val, exp, "Assertion failed!");
        }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above:
//ORIGINAL LINE: void assert_equal(const T &val, const T &exp, const String msg = "Assertion failed!")



    public static void usage(tangible.RefObject<String> programName) {
        System.out.print("Usage: ");
        System.out.print(programName.argValue);
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

        public static int Main(int argc, String[] args) {
        if (argc == 1) {
            usage(args[0]);
        }

        boolean verbose = false;

        if (argc < 3) {
            std.cerr << "Error: Not enough parameters!" << std.endl;
            usage(args[0]);
            return -1;
        }

        if (argc == 4 && strcmp(args[1], "-v") == 0) {
            verbose = true;
        }

        String filename1 = args[argc - 2];
        String filename2 = args[argc - 1];

        TripletDistanceCalculator tripletCalc = new TripletDistanceCalculator();
        INTTYPE_REST dist = tripletCalc.calculateTripletDistance(filename1, filename2);

        if (dist == -1) {
            System.exit(-1);
        }

        if (!verbose) {
            System.out.print(dist);
            System.out.print("\n");
        } else {
            INTTYPE_REST n = tripletCalc.get_n();
            INTTYPE_REST totalNoTriplets = tripletCalc.get_totalNoTriplets();
            INTTYPE_REST resolved = tripletCalc.get_resolvedTriplets();
            INTTYPE_REST unresolved = tripletCalc.get_unresolvedTriplets();
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

    }public static <T> void assert_equal(T val, T exp, String msg) {
        if (val != exp) {
            std.cerr << msg.compareTo() < 0 < <" Found " << val << " but expected " << exp << std.endl;
            System.exit(-1);
        }
    }
}