package TreeDistance;

import java.util.ArrayList;

///#else
//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of 'private' inheritance:
//ORIGINAL LINE: class QuartetDistanceCalculator : private AbstractDistanceCalculator
public class QuartetDistanceCalculator extends AbstractDistanceCalculator {
    private long n;
    private long totalNoQuartets;
    private long resolvedQuartetsAgree;
    private long resolvedQuartetsAgreeDiag;
    private long resolvedQuartetsDisagree;
    private long resolvedQuartetsDisagreeDiag;
    private long resolvedQuartetsAgreeUpper;
    private long resolvedQuartetsDisagreeUpper;
    private long unresolvedQuartets;


    public QuartetDistanceCalculator() {
        dummyHDTFactory = new HDTFactory();
    }

    public void dispose() {
        dummyHDTFactory = null;
    }

    public final long calculateQuartetDistance(String filename1, String filename2) {
        UnrootedTree ut1 = null;
        UnrootedTree ut2 = null;
        NewickParser parser = new NewickParser();

        ut1 = parser.parseFile(filename1);
        if (ut1 == null || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename1 << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            return -1;
        }

        ut2 = parser.parseFile(filename2);
        if (ut2 == null || parser.isError()) {
            cerr << "Parsing of file \"" << filename2 << "\" failed." << "\n";
            cerr << "Aborting!" << "\n";
            return -1;
        }

        long res = calculateQuartetDistance(ut1, ut2);

        if (ut1 != null)
            ut1.dispose();
        if (ut2 != null)
            ut2.dispose();

        return res;
    }

    public final long calculateQuartetDistance(UnrootedTree t1, UnrootedTree t2) {

        UnrootedTree tmp;
        if (t1.maxDegree > t2.maxDegree) { // Smallest degree tree as t1
            tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        this.t1 = t1.convertToRootedTree(null);
        this.t2 = t2.convertToRootedTree(this.t1.factory);

        this.t1.pairAltWorld(this.t2);
        if (this.t1.isError()) {
            std.cerr << "The two trees do not have the same set of leaves." << std.endl;
            std.cerr << "Aborting." << std.endl;
            this.t1.factory = null;
            this.t2.factory = null;
            return -1;
        }

        // Section 3 of Soda13: Counting unresolved triplets and quartets in a single tree
        countChildren(this.t1);
        hdt = HDT.constructHDT(this.t2, this.t1.maxDegree, dummyHDTFactory);

        resolvedQuartetsAgree = resolvedQuartetsAgreeDiag = 0;
        resolvedQuartetsDisagree = resolvedQuartetsDisagreeDiag = 0;
        resolvedQuartetsAgreeUpper = resolvedQuartetsDisagreeUpper = 0;
        unresolvedQuartets = 0;

        count(this.t1);

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if ! doExtractAndContract
        hdt.factory = null;
///#endif

        n = this.t1.n;
        totalNoQuartets = Util.binom4(n);
        long a = resolvedQuartetsAgree + resolvedQuartetsAgreeDiag + resolvedQuartetsAgreeUpper;
        long e = unresolvedQuartets;

        long result = totalNoQuartets - (a + e);

        this.t1.factory = null;
        this.t2.factory = null;

        // HDT is deleted in count!
        return result;
    }

    public final ArrayList<ArrayList<long>> calculateAllPairsQuartetDistance(String filename) {
        NewickParser parser = new NewickParser();

        ArrayList<UnrootedTree> unrootedTrees = parser.parseMultiFile(filename);
        if (unrootedTrees.size() == 0 || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            System.exit(-1);
        }

        final ArrayList<ArrayList<long>> results = calculateAllPairsQuartetDistance(unrootedTrees);

        for (int i = 0; i < unrootedTrees.size(); ++i) {
            UnrootedTree tmp = unrootedTrees.get(i);
            if (tmp != null)
                tmp.dispose();
        }

        return results;
    }

    public final ArrayList<ArrayList<long>> calculateAllPairsQuartetDistance(ArrayList<UnrootedTree>trees) {
        ArrayList<ArrayList<long>> results = new ArrayList<ArrayList<long>>(trees.size());

        for (int r = 0; r < trees.size(); ++r) {
            for (int c = 0; c < r; ++c) {
                long distance = calculateQuartetDistance(trees.get(r), trees.get(c));
                results.get(r).add(distance);
            }
            results.get(r).add(0);
        }

        return results;
    }

    public final ArrayList<long> pairs_quartet_distance(ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        ArrayList<long> res = new ArrayList<long>();

        for (int i = 0; i < unrootedTrees1.size(); i++) {
            long dist = calculateQuartetDistance(unrootedTrees1.get(i), unrootedTrees2.get(i));

            res.add(dist);
        }

        return res;
    }

    public final void pairs_quartet_distance_verbose(std.ostream out, ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        for (int i = 0; i < unrootedTrees1.size(); i++) {
            long dist = calculateQuartetDistance(unrootedTrees1.get(i), unrootedTrees2.get(i));

            long resolvedQuartetsAgree = get_resolvedQuartetsAgree();
            long resolvedQuartetsAgreeDiag = get_resolvedQuartetsAgreeDiag();
            long resolvedQuartetsDisagree = get_resolvedQuartetsDisagree();
            long resolvedQuartetsDisagreeDiag = get_resolvedQuartetsDisagreeDiag();
            long resolvedQuartetsAgreeUpper = get_resolvedQuartetsAgreeUpper();
            long resolvedQuartetsDisagreeUpper = get_resolvedQuartetsDisagreeUpper();

            long n = get_n();
            long totalNoQuartets = get_totalNoQuartets();
            double dist_norm = (double) dist / (double) totalNoQuartets;
            long resAgree = resolvedQuartetsAgree + resolvedQuartetsAgreeDiag + resolvedQuartetsAgreeUpper;
            double resAgree_norm = (double) resAgree / (double) totalNoQuartets;
            long unresolvedQuartetsAgree;
            double unresolvedQuartetsAgree_norm = (double) unresolvedQuartetsAgree / (double) totalNoQuartets;

            System.out.print(n);
            System.out.print("\t");
            System.out.print(totalNoQuartets);
            System.out.print("\t");
            System.out.print(dist);
            System.out.print("\t");
            System.out.print(dist_norm);
            System.out.print("\t");
            System.out.print(resAgree);
            System.out.print("\t");
            System.out.print(resAgree_norm);
            System.out.print("\t");
            System.out.print(unresolvedQuartetsAgree);
            System.out.print("\t");
            System.out.print(unresolvedQuartetsAgree_norm);
            System.out.print("\n");

        }
    }

    public final ArrayList<long> pairs_quartet_distance(String filename1, String filename2) {
        NewickParser parser = new NewickParser();

        ArrayList<UnrootedTree> unrootedTrees1 = parser.parseMultiFile(filename1);
        if (unrootedTrees1.size() == 0 || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename1 << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            System.exit(-1);
        }

        ArrayList<UnrootedTree> unrootedTrees2 = parser.parseMultiFile(filename2);
        if (unrootedTrees2.size() == 0 || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename2 << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            System.exit(-1);
        }

        return pairs_quartet_distance(unrootedTrees1, unrootedTrees2);
    }

    // accessors
    public final long get_n() {
        return n;
    }

    public final long get_totalNoQuartets() {
        return totalNoQuartets;
    }

    public final long get_resolvedQuartetsAgree() {
        return resolvedQuartetsAgree;
    }

    public final long get_resolvedQuartetsAgreeDiag() {
        return resolvedQuartetsAgreeDiag;
    }

    public final long get_resolvedQuartetsDisagree() {
        return resolvedQuartetsDisagree;
    }

    public final long get_resolvedQuartetsDisagreeDiag() {
        return resolvedQuartetsDisagreeDiag;
    }

    public final long get_resolvedQuartetsAgreeUpper() {
        return resolvedQuartetsAgreeUpper;
    }

    public final long get_resolvedQuartetsDisagreeUpper() {
        return resolvedQuartetsDisagreeUpper;
    }

    public final long get_unresolvedQuartets() {
        return unresolvedQuartets;
    }

    private void updateCounters() {
        resolvedQuartetsAgree += hdt.quartResolvedAgree;
        resolvedQuartetsAgreeDiag += hdt.quartResolvedAgreeDiag;
        resolvedQuartetsAgreeUpper += hdt.quartResolvedAgreeUpper;
        unresolvedQuartets += hdt.quartSumE;
    }
}