package TreeDistance;

import java.util.ArrayList;
import java.util.Iterator;

///#else
//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of 'private' inheritance:
//ORIGINAL LINE: class TripletDistanceCalculator : private AbstractDistanceCalculator
public class TripletDistanceCalculator extends AbstractDistanceCalculator {
    private long n ;
    private long totalNoTriplets ;
    private long resolvedTriplets ;
    private long unresolvedTriplets ;

    public TripletDistanceCalculator() {
        dummyHDTFactory = new HDTFactory();
    }

    public void dispose() {
        dummyHDTFactory = null;
    }

    public final long calculateTripletDistance(String filename1, String filename2) {
        UnrootedTree ut1 = null;
        UnrootedTree ut2 = null;
        RootedTree rt1 = null;
        RootedTree rt2 = null;

        NewickParser parser = new NewickParser();

        ut1 = parser.parseFile(filename1);
        if (ut1 == null || parser.isError()) {

            System.out.println("Error: Parsing of "+filename1+"failed.");
            System.out.println("Aborting");
            return -1;
        }

        ut2 = parser.parseFile(filename2);

        if (ut2 == null || parser.isError()) {

            System.out.println("Error: Parsing of "+filename1+"failed.");
            System.out.println("Aborting");
            return -1;
        }

        rt1 = ut1.convertToRootedTree(null);
        rt2 = ut2.convertToRootedTree(rt1.factory);

        long result = calculateTripletDistance(rt1, rt2);

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

    public final long calculateTripletDistance(RootedTree t1, RootedTree t2) {
        this.t1 = t1;
        t1.pairAltWorld(t2);

        if (t1.isError()) {

            System.out.println("The two trees do not have the same set of leaves");
            System.out.println("Aborting");
            return -1;
        }

        // Section 3 : Counting unresolved triplets and quartets in a single tree
        countChildren(t1);

        hdt = HDT.constructHDT(t2, t1.maxDegree, dummyHDTFactory);

        resolvedTriplets = unresolvedTriplets = 0;
        n = t1.n;
        totalNoTriplets = Util.binom3(n);
        count(t1);
        // HDT is deleted in count if extracting and contracting!

        hdt.factory = null;


        return totalNoTriplets - resolvedTriplets - unresolvedTriplets;
    }

    public final ArrayList<ArrayList<long>> calculateAllPairsTripletDistance(String filename) {

        NewickParser parser = new NewickParser();

        ArrayList<UnrootedTree> unrootedTrees = parser.parseMultiFile(filename);
        if (unrootedTrees.size() == 0 || parser.isError()) {

            System.out.println("Error: Parsing of \"+filename1+\"failed.");
            System.out.println("Aborting");
            System.exit(-1);
        }

        ArrayList<ArrayList<Long>> results = calculateAllPairsTripletDistance(unrootedTrees);

        for (Iterator<UnrootedTree> it = unrootedTrees.iterator(); it.hasNext(); ) {
            delete(it.next());
        }

        return results;
    }

    public final ArrayList<ArrayList<long>> calculateAllPairsTripletDistance(ArrayList<UnrootedTree*>trees) {
        ArrayList<ArrayList<long>> results = new ArrayList<ArrayList<long>>(trees.size());

        RootedTree rt1;
        RootedTree rt2;

        for (int r = 0; r < trees.size(); ++r) {
            for (int c = 0; c < r; ++c) {
                rt1 = trees.get(r).convertToRootedTree(null);
                rt2 = trees.get(c).convertToRootedTree(rt1.factory);

                long distance = calculateTripletDistance(rt1, rt2);
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

            long dist = calculateTripletDistance(rt1, rt2);

            long n = get_n();
            long totalNoTriplets = get_totalNoTriplets();
            long resolved = get_resolvedTriplets();
            long unresolved = get_unresolvedTriplets();
            double dist_norm = (double) dist / (double) totalNoTriplets;
            double resolved_norm = (double) resolved / (double) totalNoTriplets;
            double unresolved_norm = (double) unresolved / (double) totalNoTriplets;

            out << n << "\t" << totalNoTriplets << "\t" << dist << "\t" << dist_norm << "\t" << resolved << "\t" << resolved_norm << "\t" << unresolved << "\t" << unresolved_norm << std.endl;
        }
    }

    public final ArrayList<long> pairs_triplet_distance(ArrayList<UnrootedTree> unrootedTrees1, ArrayList<UnrootedTree> unrootedTrees2) {
        ArrayList<long> res = new ArrayList<long>();

        RootedTree rt1;
        RootedTree rt2;

        for (int i = 0; i < unrootedTrees1.size(); i++) {
            rt1 = unrootedTrees1.get(i).convertToRootedTree(null);
            rt2 = unrootedTrees2.get(i).convertToRootedTree(rt1.factory);

            long dist = calculateTripletDistance(rt1, rt2);
            res.add(dist);
        }

        return res;
    }

    public final ArrayList<long> pairs_triplet_distance(String filename1, String filename2) {
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
    public final long get_n() {
        return n;
    }

    public final long get_resolvedTriplets() {
        return resolvedTriplets;
    }

    public final long get_unresolvedTriplets() {
        return unresolvedTriplets;
    }

    public final long get_totalNoTriplets() {
        return totalNoTriplets;
    }

    private void updateCounters() {
        resolvedTriplets += hdt.getResolvedTriplets();
        unresolvedTriplets += hdt.getUnresolvedTriplets();
    }
}