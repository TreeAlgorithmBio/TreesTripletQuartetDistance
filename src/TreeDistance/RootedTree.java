package TreeDistance;

import java.util.ArrayList;
import java.util.TreeMap;

public class RootedTree {

    public int level;
    public RootedTree parent;
    public TemplatedLinkedList<RootedTree> children;
    public String name;
    public int numChildren;
    public int maxDegree;
    public RootedTree altWorldSelf;
    public HDT hdtLink;
    public int color;
    public int numZeroes;
    public RootedTreeFactory factory;
    public int n;
    private boolean error;

    public void initialize(String name) {

        parent = altWorldSelf = null;
        children = null;
        level = 0;
        maxDegree = 0;
        numZeroes = 0;
        numChildren = 0;
        n = -1;
        color = 1;
        this.name = name;
    }

    public boolean isLeaf() {
        return numChildren == 0;
    }

    public void addChild(RootedTree t) {

        numChildren++;
        t.parent = this;
        TemplatedLinkedList<RootedTree> newItem = factory.getTemplatedLinkedList();

        //ORIGINAL LINE: newItem->data = t;
        newItem.data=t.level;
        newItem.next = children;
        children = newItem;
    }

    public RootedTree getParent() {
        return parent;
    }

    //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		INTTYPE_REST getUnresolvedTriplets();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		INTTYPE_N4 getUnresolvedQuartets();
    public void toDot() {
        System.out.print("digraph g {");
        System.out.print("\n");
        System.out.print("node[shape=circle];");
        System.out.print("\n");
        toDotImpl();
        System.out.print("}");
        System.out.print("\n");
    }

    public ArrayList<RootedTree> getList() {
        ArrayList<RootedTree> list = new ArrayList<RootedTree>();
        getListImpl(list);
        return list;
    }

    public void pairAltWorld(RootedTree t) {
        error = false;
        ArrayList<RootedTree> l = t.getList();
        TreeMap<String, RootedTree> altWorldLeaves = new TreeMap<String, RootedTree>();

        //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
        for (Iterator<RootedTree> i = l.iterator(); i != l.end(); i++) {
            //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
            RootedTree leaf = i;
            altWorldLeaves.put(leaf.name, leaf);
        }

        if (l != null)
            l.dispose();
        l = getList();
        Iterator<String, RootedTree> altWorldEnd = altWorldLeaves.end();
        //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
        for (Iterator<RootedTree> i = l.iterator(); i != l.end(); i++) {
            //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
            RootedTree leaf = i;
            Iterator<String, RootedTree> j = altWorldLeaves.find(leaf.name);
            if (j == altWorldEnd) {
                // This leaf wasn't found in the input tree!
                cerr << "Leaves doesn't agree! Aborting! (" << leaf.name.compareTo() < 0 < <
                " didn't exist in second tree)" << "\n";
                error = true;
                if (l != null)
                    l.dispose();
                return;
            }

            // If we got this far, we found the match! Setup bidirectional pointers!
            //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
            leaf.altWorldSelf = j.second;
            //C++ TO JAVA CONVERTER TODO TASK: Iterators are only converted within the context of 'while' and 'for' loops:
            j.second.altWorldSelf = leaf;

            // Delete result
            altWorldLeaves.remove(j);
        }

        // Is there results left in altWorldLeaves? If so it had more leaves than we do...
        if (altWorldLeaves.size() > 0) {
            cerr << "Leaves doesn't agree! Aborting! (" << altWorldLeaves.iterator().first.compareTo() < 0 < <
            " didn't exist in first tree)";
            if (altWorldLeaves.size() > 1) {
                cerr << " (and " << (altWorldLeaves.size() - 1) << " other leaves missing from first tree!)";
            }
            cerr << "\n";
            error = true;
            if (l != null)
                l.dispose();
            return;
        }

        if (l != null)
            l.dispose();
    }

    public void colorSubtree(int c) {
        color = c;
        if (altWorldSelf != null) {
            altWorldSelf.color = c;
            if (altWorldSelf.hdtLink != null) {
                altWorldSelf.hdtLink.mark();
            }
        }

        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            i.data.colorSubtree(c);
        }
    }

    public void markHDTAlternative() {
        if (altWorldSelf != null) {
            if (altWorldSelf.hdtLink != null) {
                altWorldSelf.hdtLink.markAlternative();
            }
        }

        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            i.data.markHDTAlternative();
        }
    }

    public boolean isError() {
        return error;
    }

    public RootedTree contract(RootedTreeFactory factory) {
        computeNullChildrenData();
        return contractImpl(factory);
    }

    public void toDotImpl() {
        System.out.print("n");
        System.out.print(this);
        System.out.print("[label=\"");
        if (isLeaf() && numZeroes > 0) {
            System.out.print("0's: ");
            System.out.print(numZeroes);
        } else {
            System.out.print(name);
        }

        System.out.print("\"];");
        System.out.print("\n");

        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            RootedTree t = i.data;
            t.toDotImpl();
            System.out.print("n");
            System.out.print(this);
            System.out.print(" -> n");
            System.out.print(t);
            System.out.print(";");
            System.out.print("\n");
        }
    }

    public void getListImpl(ArrayList<RootedTree> list) {
        if (isLeaf()) {
            list.add(this);
        }

        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            RootedTree t = i.data;
            t.level = level + 1;
            t.getListImpl(list);
        }
    }

    public void computeNullChildrenData() {
        if (isLeaf())
            return;

        boolean allZeroes = true;
        numZeroes = 0;
        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            i.data.computeNullChildrenData();
            if (i.data.numZeroes == 0) {
                allZeroes = false;
            } else {
                numZeroes += i.data.numZeroes;
            }
        }
        if (!allZeroes) {
            numZeroes = 0;
        }
    }

    public RootedTree contractImpl(RootedTreeFactory factory) {
        if (isLeaf()) {
            return this; // reuse leaves!!
        }

        if (factory == null) {
            factory = new RootedTreeFactory(this.factory);
        }

        INTTYPE_REST totalNumZeroes = 0;
        RootedTree firstNonZeroChild = null;
        RootedTree ourNewNode = null;
        for (TemplatedLinkedList<RootedTree> i = children; i != null; i = i.next) {
            RootedTree t = i.data;
            if (t.numZeroes > 0) {
                totalNumZeroes += t.numZeroes;
            } else {
                if (firstNonZeroChild == null) {
                    firstNonZeroChild = t.contractImpl(factory);
                } else {
                    if (ourNewNode == null) {
                        ourNewNode = factory.getRootedTree();
                        ourNewNode.addChild(firstNonZeroChild);
                    }
                    ourNewNode.addChild(t.contractImpl(factory));
                }
            }
        }

        // Have we found >= 2 non-zero children?
        if (ourNewNode == null) {
            // No... We only have 1 non-zero children!
            if (firstNonZeroChild.numChildren == 2) {
                RootedTree zeroChild = firstNonZeroChild.children.data;
                RootedTree otherOne = firstNonZeroChild.children.next.data;
                if (zeroChild.numZeroes == 0) {
                    RootedTree tmp = otherOne;
                    otherOne = zeroChild;
                    zeroChild = tmp;
                }
                if (zeroChild.numZeroes != 0 && !otherOne.isLeaf()) {
                    // The 1 child has a zero child and only 2 children, the other not being a leaf, i.e. we can merge!
                    zeroChild.numZeroes += totalNumZeroes;
                    return firstNonZeroChild;
                }
                // if (zeroChild->numZeroes == 0) it's not a zerochild!!
            }

            // The child doesn't have a zero child, i.e. no merge...
            ourNewNode = factory.getRootedTree();
            ourNewNode.addChild(firstNonZeroChild);
        }

        // We didn't merge below --- add zero-leaf if we have any zeros...
        if (totalNumZeroes > 0) {
            RootedTree zeroChild = factory.getRootedTree();
            zeroChild.numZeroes = totalNumZeroes;
            ourNewNode.addChild(zeroChild);
        }

        return ourNewNode;
    }
}