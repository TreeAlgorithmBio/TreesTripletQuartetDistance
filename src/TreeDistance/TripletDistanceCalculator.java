package TreeDistance;

import java.util.ArrayList;
import java.util.Iterator;

///#else
//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of 'private' inheritance:
//ORIGINAL LINE: class TripletDistanceCalculator : private AbstractDistanceCalculator
public class TripletDistanceCalculator extends AbstractDistanceCalculator {
    private INTTYPE_REST n = new INTTYPE_REST();
    private INTTYPE_REST totalNoTriplets = new INTTYPE_REST();
    private INTTYPE_REST resolvedTriplets = new INTTYPE_REST();
    private INTTYPE_REST unresolvedTriplets = new INTTYPE_REST();

    public TripletDistanceCalculator() {
        dummyHDTFactory = new HDTFactory(0);
    }

    public void dispose() {
        dummyHDTFactory = null;
    }

    public final INTTYPE_REST calculateTripletDistance(String filename1, String filename2) {
        UnrootedTree ut1 = null;
        UnrootedTree ut2 = null;
        RootedTree rt1 = null;
        RootedTree rt2 = null;

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

        rt1 = ut1.convertToRootedTree(null);
        rt2 = ut2.convertToRootedTree(rt1.factory);

        INTTYPE_REST result = calculateTripletDistance(rt1, rt2);

        if (ut1 != null) {
            if (ut1 != null)
                ut1.dispose();
        }
        if (ut2 != null) {
            if (ut2 != null)
                ut2.dispose();
        }
        if (rt1 != null) {
            if (rt1.factory != null)
                rt1.factory.dispose();
        }
        if (rt2 != null) {
            if (rt2.factory != null)
                rt2.factory.dispose();
        }

        return result;
    }

    public final INTTYPE_REST calculateTripletDistance(RootedTree t1, RootedTree t2) {
        this.t1 = t1;
        t1.pairAltWorld(t2);
        if (t1.isError()) {
            std.cerr << "The two trees do not have the same set of leaves." << std.endl;
            std.cerr << "Aborting." << std.endl;
            return -1;
        }

        // Section 3 of Soda13: Counting unresolved triplets and quartets in a single tree
        countChildren(t1);

        hdt = HDT.constructHDT(t2, t1.maxDegree, dummyHDTFactory);

        resolvedTriplets = unresolvedTriplets = 0;
        n = t1.n;
        totalNoTriplets = Util.binom3(n);

        count(t1);
        // HDT is deleted in count if extracting and contracting!
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if ! doExtractAndContract
        hdt.factory = null;
///#endif

        return totalNoTriplets - resolvedTriplets - unresolvedTriplets;
    }

    public final ArrayList<ArrayList<INTTYPE_REST>> calculateAllPairsTripletDistance(String filename) {
        NewickParser parser = new NewickParser();

        ArrayList<UnrootedTree> unrootedTrees = parser.parseMultiFile(filename);
        if (unrootedTrees.size() == 0 || parser.isError()) {
            std.cerr << "Error: Parsing of \"" << filename << "\" failed." << "\n";
            std.cerr << "Aborting!" << "\n";
            System.exit(-1);
        }

        ArrayList<ArrayList<INTTYPE_REST>> results = calculateAllPairsTripletDistance(unrootedTrees);

        for (Iterator<UnrootedTree> it = unrootedTrees.iterator(); it.hasNext(); ) {
            delete(it.next());
        }

        return results;
    }

    public final ArrayList<ArrayList<INTTYPE_REST>> calculateAllPairsTripletDistance(ArrayList<UnrootedTree*>trees) {
        ArrayList<ArrayList<INTTYPE_REST>> results = new ArrayList<ArrayList<INTTYPE_REST>>(trees.size());

        RootedTree rt1;
        RootedTree rt2;

        for (int r = 0; r < trees.size(); ++r) {
            for (int c = 0; c < r; ++c) {
                rt1 = trees.get(r).convertToRootedTree(null);
                rt2 = trees.get(c).convertToRootedTree(rt1.factory);

                INTTYPE_REST distance = calculateTripletDistance(rt1, rt2);
                results.get(r).add(distance);

                if (rt1.factory != null)
                    rt1.factory.dispose();
                if (rt2.factory != null)
                    rt2.factory.dispose();
            }
            results.get(r).add(0);
        }

        return results;
    }

    public final void pairs_triplet_distance_verbose(std.ostream out, ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        RootedTree rt1;
        RootedTree rt2;

        for (int i = 0; i < unrootedTrees1.size(); i++) {

            rt1 = unrootedTrees1.get(i).convertToRootedTree(null);
            rt2 = unrootedTrees2.get(i).convertToRootedTree(rt1.factory);

            INTTYPE_REST dist = calculateTripletDistance(rt1, rt2);

            INTTYPE_REST n = get_n();
            INTTYPE_REST totalNoTriplets = get_totalNoTriplets();
            INTTYPE_REST resolved = get_resolvedTriplets();
            INTTYPE_REST unresolved = get_unresolvedTriplets();
            double dist_norm = (double) dist / (double) totalNoTriplets;
            double resolved_norm = (double) resolved / (double) totalNoTriplets;
            double unresolved_norm = (double) unresolved / (double) totalNoTriplets;

            out << n << "\t" << totalNoTriplets << "\t" << dist << "\t" << dist_norm << "\t" << resolved << "\t" << resolved_norm << "\t" << unresolved << "\t" << unresolved_norm << std.endl;
        }
    }

    public final ArrayList<INTTYPE_REST> pairs_triplet_distance(ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        ArrayList<INTTYPE_REST> res = new ArrayList<INTTYPE_REST>();

        RootedTree rt1;
        RootedTree rt2;

        for (int i = 0; i < unrootedTrees1.size(); i++) {
            rt1 = unrootedTrees1.get(i).convertToRootedTree(null);
            rt2 = unrootedTrees2.get(i).convertToRootedTree(rt1.factory);

            INTTYPE_REST dist = calculateTripletDistance(rt1, rt2);
            res.add(dist);
        }

        return res;
    }

    public final ArrayList<INTTYPE_REST> pairs_triplet_distance(String filename1, String filename2) {
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

        return pairs_triplet_distance(unrootedTrees1, unrootedTrees2);
    }

    // accessors
    public final INTTYPE_REST get_n() {
        return n;
    }

    public final INTTYPE_REST get_resolvedTriplets() {
        return resolvedTriplets;
    }

    public final INTTYPE_REST get_unresolvedTriplets() {
        return unresolvedTriplets;
    }

    public final INTTYPE_REST get_totalNoTriplets() {
        return totalNoTriplets;
    }

    private void updateCounters() {
        resolvedTriplets += hdt.getResolvedTriplets();
        unresolvedTriplets += hdt.getUnresolvedTriplets();
    }
}