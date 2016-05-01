package TreeDistance;

import java.util.ArrayList;

///#else
//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of 'private' inheritance:
//ORIGINAL LINE: class QuartetDistanceCalculator : private AbstractDistanceCalculator
public class QuartetDistanceCalculator extends AbstractDistanceCalculator {
    private INTTYPE_N4 n = new INTTYPE_N4();
    private INTTYPE_N4 totalNoQuartets = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsAgree = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsAgreeDiag = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsDisagree = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsDisagreeDiag = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsAgreeUpper = new INTTYPE_N4();
    private INTTYPE_N4 resolvedQuartetsDisagreeUpper = new INTTYPE_N4();
    private INTTYPE_N4 unresolvedQuartets = new INTTYPE_N4();


    public QuartetDistanceCalculator() {
        dummyHDTFactory = new HDTFactory(0);
    }

    public void dispose() {
        dummyHDTFactory = null;
    }

    public final INTTYPE_N4 calculateQuartetDistance(String filename1, String filename2) {
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

        INTTYPE_N4 res = calculateQuartetDistance(ut1, ut2);

        if (ut1 != null)
            ut1.dispose();
        if (ut2 != null)
            ut2.dispose();

        return res;
    }

    public final INTTYPE_N4 calculateQuartetDistance(UnrootedTree t1, UnrootedTree t2) {

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
        INTTYPE_N4 a = resolvedQuartetsAgree + resolvedQuartetsAgreeDiag + resolvedQuartetsAgreeUpper;
        INTTYPE_N4 e = unresolvedQuartets;

        INTTYPE_N4 result = totalNoQuartets - (a + e);

        this.t1.factory = null;
        this.t2.factory = null;

        // HDT is deleted in count!
        return result;
    }

    public final ArrayList<ArrayList<INTTYPE_N4>> calculateAllPairsQuartetDistance(String filename) {
        NewickParser parser = new NewickParser();

        ArrayList<UnrootedTree> unrootedTrees = parser.parseMultiFile(filename);
        if (unrootedTrees.size() == 0 || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            System.exit(-1);
        }

        final ArrayList<ArrayList<INTTYPE_N4>> results = calculateAllPairsQuartetDistance(unrootedTrees);

        for (int i = 0; i < unrootedTrees.size(); ++i) {
            UnrootedTree tmp = unrootedTrees.get(i);
            if (tmp != null)
                tmp.dispose();
        }

        return results;
    }

    public final ArrayList<ArrayList<INTTYPE_N4>> calculateAllPairsQuartetDistance(ArrayList<UnrootedTree*>trees) {
        ArrayList<ArrayList<INTTYPE_N4>> results = new ArrayList<ArrayList<INTTYPE_N4>>(trees.size());

        for (int r = 0; r < trees.size(); ++r) {
            for (int c = 0; c < r; ++c) {
                INTTYPE_N4 distance = calculateQuartetDistance(trees.get(r), trees.get(c));
                results.get(r).add(distance);
            }
            results.get(r).add(0);
        }

        return results;
    }

    public final ArrayList<INTTYPE_N4> pairs_quartet_distance(ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        ArrayList<INTTYPE_N4> res = new ArrayList<INTTYPE_N4>();

        for (int i = 0; i < unrootedTrees1.size(); i++) {
            INTTYPE_N4 dist = calculateQuartetDistance(unrootedTrees1.get(i), unrootedTrees2.get(i));

            res.add(dist);
        }

        return res;
    }

    public final void pairs_quartet_distance_verbose(std.ostream out, ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        for (int i = 0; i < unrootedTrees1.size(); i++) {
            INTTYPE_N4 dist = calculateQuartetDistance(unrootedTrees1.get(i), unrootedTrees2.get(i));

            INTTYPE_N4 resolvedQuartetsAgree = get_resolvedQuartetsAgree();
            INTTYPE_N4 resolvedQuartetsAgreeDiag = get_resolvedQuartetsAgreeDiag();
            INTTYPE_N4 resolvedQuartetsDisagree = get_resolvedQuartetsDisagree();
            INTTYPE_N4 resolvedQuartetsDisagreeDiag = get_resolvedQuartetsDisagreeDiag();
            INTTYPE_N4 resolvedQuartetsAgreeUpper = get_resolvedQuartetsAgreeUpper();
            INTTYPE_N4 resolvedQuartetsDisagreeUpper = get_resolvedQuartetsDisagreeUpper();

            INTTYPE_N4 n = get_n();
            INTTYPE_N4 totalNoQuartets = get_totalNoQuartets();
            double dist_norm = (double) dist / (double) totalNoQuartets;
            INTTYPE_N4 resAgree = resolvedQuartetsAgree + resolvedQuartetsAgreeDiag + resolvedQuartetsAgreeUpper;
            double resAgree_norm = (double) resAgree / (double) totalNoQuartets;
            INTTYPE_N4 unresolvedQuartetsAgree = get_unresolvedQuartets();
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

    public final ArrayList<INTTYPE_N4> pairs_quartet_distance(String filename1, String filename2) {
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
    public final INTTYPE_N4 get_n() {
        return n;
    }

    public final INTTYPE_N4 get_totalNoQuartets() {
        return totalNoQuartets;
    }

    public final INTTYPE_N4 get_resolvedQuartetsAgree() {
        return resolvedQuartetsAgree;
    }

    public final INTTYPE_N4 get_resolvedQuartetsAgreeDiag() {
        return resolvedQuartetsAgreeDiag;
    }

    public final INTTYPE_N4 get_resolvedQuartetsDisagree() {
        return resolvedQuartetsDisagree;
    }

    public final INTTYPE_N4 get_resolvedQuartetsDisagreeDiag() {
        return resolvedQuartetsDisagreeDiag;
    }

    public final INTTYPE_N4 get_resolvedQuartetsAgreeUpper() {
        return resolvedQuartetsAgreeUpper;
    }

    public final INTTYPE_N4 get_resolvedQuartetsDisagreeUpper() {
        return resolvedQuartetsDisagreeUpper;
    }

    public final INTTYPE_N4 get_unresolvedQuartets() {
        return unresolvedQuartets;
    }

    private void updateCounters() {
        resolvedQuartetsAgree += hdt.quartResolvedAgree;
        resolvedQuartetsAgreeDiag += hdt.quartResolvedAgreeDiag;
        resolvedQuartetsAgreeUpper += hdt.quartResolvedAgreeUpper;
        unresolvedQuartets += hdt.quartSumE;
    }
}