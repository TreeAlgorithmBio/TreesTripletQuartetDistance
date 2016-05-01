package TreeDistance;

import java.util.ArrayList;
import java.util.Iterator;

public class UnrootedTree {
    public String name;
    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: unsigned int level;
    public int level;
    public UnrootedTree dontRecurceOnMe;
    public int maxDegree;
    private ArrayList<UnrootedTree> edges = new ArrayList<UnrootedTree>();

    public UnrootedTree() {
        dontRecurceOnMe = null;
        level = maxDegree = 0;
    }

    public UnrootedTree(String name) {
        dontRecurceOnMe = null;
        level = maxDegree = 0;
        this.name = name;
    }

    public void dispose() {
        for (Iterator<UnrootedTree> i = edges.iterator(); i.hasNext(); ) {
            UnrootedTree t = i.next();
            if (dontRecurceOnMe != t) {
                t.dontRecurceOnMe = this;
                if (t != null)
                    t.dispose();
            }
        }
    }

    public final void addEdgeTo(UnrootedTree t) {
        edges.add(t);
        t.edges.add(this);
    }

    public final void toDot() {
        dontRecurceOnMe = null;
        System.out.print("graph g {");
        System.out.print("\n");
        System.out.print("node[shape=circle,label=\"\"];");
        System.out.print("\n");
        toDotImpl();
        System.out.print("}");
        System.out.print("\n");
    }

    public final boolean isLeaf() {
        return edges.size() == 1;
    }

    public final ArrayList<UnrootedTree> getList() {
        dontRecurceOnMe = null;
        ArrayList<UnrootedTree> list = new ArrayList<UnrootedTree * > ();
        getListImpl(list);
        return list;
    }

    public final RootedTree convertToRootedTree(RootedTreeFactory oldFactory) {
        UnrootedTree t = this;

        // Make sure the root is not a leaf
        // (unless of course there's only 2 elements in which case we can't avoid it)
        if (isLeaf()) {
            t = edges.get(0);
        }

        t.dontRecurceOnMe = null;
        RootedTreeFactory factory = new RootedTreeFactory(oldFactory);
        RootedTree rooted = t.convertToRootedTreeImpl(factory);

        // Make sure the root always recurses on everything! (e.g. so that we can cleanup properly!)
        dontRecurceOnMe = null;

        return rooted;
    }

    private void toDotImpl() {
        System.out.print("n");
        System.out.print(this);
        System.out.print("[label=\"");
        System.out.print(name);
        System.out.print("\"];");
        System.out.print("\n");
            /*
			if (isLeaf())
			{
				cout << "n" << this << "[label=\"" << name << "\"];" << endl;
			}
			else
			{
				cout << "n" << this << ";" << endl;
			}
			*/

        for (Iterator<UnrootedTree> i = edges.iterator(); i.hasNext(); ) {
            UnrootedTree t = i.next();
            if (t != dontRecurceOnMe) {
                t.dontRecurceOnMe = this;
                t.toDotImpl();
                System.out.print("n");
                System.out.print(this);
                System.out.print(" -- n");
                System.out.print(t);
                System.out.print(";");
                System.out.print("\n");
            }
        }
    }

    private void getListImpl(ArrayList<UnrootedTree> list) {
        if (isLeaf()) {
            list.add(this);
        }

        for (Iterator<UnrootedTree> i = edges.iterator(); i.hasNext(); ) {
            UnrootedTree t = i.next();
            if (t != dontRecurceOnMe) {
                t.dontRecurceOnMe = this;
                t.level = level + 1;
                t.getListImpl(list);
            }
        }
    }

    private RootedTree convertToRootedTreeImpl(RootedTreeFactory factory) {
        RootedTree result = factory.getRootedTree(this.name);
        int maxDegreeChildren = 0;
        int maxDegreeHere = 0;
        for (Iterator<UnrootedTree> i = edges.iterator(); i.hasNext(); ) {
            UnrootedTree t = i.next();
            if (t != dontRecurceOnMe) {
                maxDegreeHere++;
                t.dontRecurceOnMe = this;
                RootedTree rt = t.convertToRootedTreeImpl(factory);
                result.addChild(rt);
                maxDegreeChildren = Math.max(maxDegreeChildren, rt.maxDegree);
            }
        }
        result.maxDegree = Math.max(maxDegreeHere, maxDegreeChildren);
        return result;
    }
}