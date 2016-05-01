/**
 * Created by khushboomandlecha on 4/30/16.
 */

///#include "counting_linked_list.h"
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RootedTree; // forward declaration
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class HDTFactory; // forward declaration
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CountingLinkedList; // forward declaration
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CountingLinkedListNumOnly; // forward declaration

//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//ORIGINAL LINE: #define INITIALIZE_PAREN_AND_SET_LIST(N_NAME, RESET_NAME) { if (parent->N_NAME == NULL) { parent->N_NAME = factory->getLLNO(); parent->N_NAME->resetIterator(); isReset = true; } else isReset = parent->RESET_NAME; theList = parent->N_NAME; parent->RESET_NAME = false; }

//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//ORIGINAL LINE: #define NEXT_LEAST_J(LIST) { if ( gotoIteratorValueForNumList(LIST, lastJPlus1) && LIST->getIteratorNum() < j ) j = LIST->getIteratorNum(); }

package TreeDistance;

public class HDT
{
    public enum NodeType
    {
        I,
        C,
        G,
        NotConverted;

        public int getValue()
        {
            return this.ordinal();
        }

        public static NodeType forValue(int value)
        {
            return values()[value];
        }
    }
    public final void initialize(CountingLinkedList countingVars, NodeType type, int numD, RootedTree link)
    {
        initialize(countingVars, type, numD, link, true);
    }
    public final void initialize(CountingLinkedList countingVars, NodeType type, int numD)
    {
        initialize(countingVars, type, numD, null, true);
    }
    //C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above:
//ORIGINAL LINE: void initialize(CountingLinkedList *countingVars, NodeType type, int numD, RootedTree *link = null, boolean doLink = true)
    public final void initialize(CountingLinkedList countingVars, NodeType type, int numD, RootedTree link, boolean doLink)
    {
        parent = childParent = left = right = null;
        children = null;
        convertedFrom = NodeType.NotConverted;
        goBackVariable = null;
        tripResolved = 0;
        tripUnresolved = 0;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
        quartResolvedAgree = 0;
        quartResolvedAgreeDiag = 0;
        quartResolvedAgreeUpper = 0;
        // New sum for calculating E
        quartSumE = 0;
///#endif
        up2date = altMarked = false;

        this.type = type;
        this.link = link;
        if (link != null && doLink)
        {
            link.hdtLink = this;
        }
        this.degree = numD;
        this.countingVars = countingVars;
    }
    public static HDT constructHDT(RootedTree t, int numD, HDTFactory copyStuffFromFactory)
    {
        return constructHDT(t, numD, copyStuffFromFactory, true);
    }
    //C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above:
//ORIGINAL LINE: static HDT* constructHDT(RootedTree *t, int numD, HDTFactory *copyStuffFromFactory, boolean doLink = true)
    public static HDT constructHDT(RootedTree t, int numD, HDTFactory copyStuffFromFactory, boolean doLink)
    {
        HDTFactory factory = new HDTFactory(numD, copyStuffFromFactory);
        HDT hdt = preFirstRound(t, numD, doLink, factory);
        while (hdt.children != null)
        {
            hdt = hdt.round(factory);
        }
        hdt.factory = factory;
        hdt.factory.deleteTemplatedLinkedList();
        return hdt;
    }

    public final void forceLinks()
    {
        if (link != null)
        {
            link.hdtLink = this;
        }
        if (left != null)
        {
            left.forceLinks();
        }
        if (right != null)
        {
            right.forceLinks();
        }
    }
    public final void toDot()
    {
        System.out.print("digraph g {");
        System.out.print("\n");
        System.out.print("node[shape=circle];");
        System.out.print("\n");
        toDotImpl();
        System.out.print("}");
        System.out.print("\n");
    }
    public final void mark()
    {
        up2date = false;

        if (parent == null || !parent.up2date)
            return;
        parent.mark();
    }
    public final void markAlternative()
    {
        altMarked = true;

        if (parent == null || parent.altMarked)
            return;
        parent.markAlternative();
    }
    public final long leafCount()
    {
        if (countingVars.num == 0)
        {
            return n_circ + countingVars.n_i;
        }
        return n_circ;
    }
    public final RootedTree extractAndGoBack(RootedTreeFactory rtfactory)
    {
        RootedTreeFactory factory = new RootedTreeFactory(rtfactory);
        extractAndGoBackImpl(null, factory);
        return goBackVariable;
    }
    public final void updateCounters()
    {
        if (this.convertedFrom == NodeType.C && this.left == null && this.right == null)
        {
            handleLeaf();
        }
        else if (this.convertedFrom == NodeType.C)
        {
            if (left.type == NodeType.C && right.type == NodeType.C)
            {
                handleCCToC();
            }
            else
            {
                handleIGToC();
            }

            handleCTransform();
        }
        else if (this.type == NodeType.C)
        {
            if (left.type == NodeType.C && right.type == NodeType.C)
            {
                handleCCToC();
            }
            else
            {
                handleIGToC();
            }
        }
        else if (this.type == NodeType.G)
        {
            handleG();
        }
        up2date = true;
    }
    public final long getResolvedTriplets()
    {
        return tripResolved;
    }
    public final long getUnresolvedTriplets()
    {
        return tripUnresolved;
    }

    public HDT left;
    public HDT right;
    public HDTFactory factory;

    public boolean altMarked;

    // Used for extract+contract to work
    public int numZeroes;

    // Quartets
    // Summing agreeing/disagreing resolved quartets
    public long quartResolvedAgree;
    public long quartResolvedAgreeDiag;
    public long quartSumE;
    public long quartResolvedAgreeUpper;

    private RootedTree goBackVariable;
    private NodeType type;
    private NodeType convertedFrom;
    private RootedTree link;
    private HDT parent;
    private HDT childParent;
    private TemplatedLinkedList<HDT> children;
    private int degree;

    // Soda13 color 0+1+...+d
    private CountingLinkedList countingVars;
    private int n_circ;
    private long n_circ_square;

    // Quartets
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
    private long n_0_circ;
    private long n_paren_0_circ;
    private long n_circ_circ;
    private long n_square_paren_circ_circ;
    private long n_paren_circ_circ;
    private long n_paren_circ_square;
    private long n_circ_arrow_paren_square_square;
    private long n_bracket_circ_square;
    private long n_0_arrow_circ;
    private long n_circ_arrow_0;
    private long n_0_arrow_circ_circ;

    // Added by us
    private long n_circ_arrow_square_square;

    // Added by us for filling out tables
    private long n_bracket_circ_circ;
    private long n_paren_circ_paren_square_square;
    private long n_circ_arrow_circ;
    private long n_circ_arrow_square;
    private long n_paren_circ_circ_arrow_square;
    private long n_bracket_circ_paren_square_square;
    private long n_circ_arrow_square_arrow_square;
    // New counters for calculating E
    private long n_circ_square_triangle;
    private long n_circ_square_arrow_triangle;
    private long n_circ_arrow_square_triangle;
    private long n_paren_circ_square_triangle;
    private long n_0_circ_square;
    private long n_0_circ_arrow_square;
    private long n_0_arrow_circ_square;
    private long n_circ_square_arrow_0;
    private long n_circ_arrow_0_square;
    private long n_paren_0_circ_square;
    private long n_bracket_circ_square_triangle;
    private long n_bracket_0_circ_square;
///#endif

    // Summing resolved/resolved and unresolved/unresolved
    //private INTTYPE_REST tripResolved = new INTTYPE_REST();
    //private INTTYPE_REST tripUnresolved = new INTTYPE_REST();

    // Marking stuff as changed or updated
    private boolean up2date;

    private static HDT preFirstRound(RootedTree t, int numD, boolean doLink, HDTFactory factory)
    {
        if (t.isLeaf())
        {
            HDT hdt;
            if (t.numZeroes == 0)
            {
                hdt = factory.getHDT(NodeType.G, t, doLink);
            }
            else
            {
                hdt = factory.getHDT(NodeType.G, null, doLink);
                hdt.numZeroes = t.numZeroes;
            }
            hdt.convertedFrom = NodeType.C;
            return hdt;
        }

        // Inner node
        HDT node = factory.getHDT(NodeType.I, null, doLink);
        for (TemplatedLinkedList<RootedTree> i = t.children; i != null; i = i.next)
        {
            HDT child = preFirstRound(i.data, numD, doLink, factory);
            {
                child.childParent = node;
                TemplatedLinkedList<HDT> newItem = factory.getTemplatedLinkedList();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->data = child;
                //newItem.data.copyFrom(child);
                newItem.data = child;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->next = node->children;
                //newItem.next.copyFrom(node.children);
                newItem.next = node.children;
                node.children = newItem;
            };
        }
        return node;
    }
    private HDT round(HDTFactory factory)
    {
        // NOTE: C -> G when parent I etc is moved down!!

        // Composition 3: If we're a C we only have 1 child.
        // If that's a C, use CC->C, skip the child and go directly to that-one's child (if exists)
        if (type == NodeType.C && children != null && children.next == null) //children.size() == 1
        {
            HDT child = children.data;
            if (child.type == NodeType.C)
            {
                // CC->C, skip 2nd C and recurse
                HDT newC = factory.getHDT(NodeType.C, null, false);
                newC.left = this;
                newC.left.parent = newC;
                newC.right = child;
                newC.right.parent = newC;

                // If there's children, there's only 1. We recurse on that one and add the result to our children list.
                if (child.children != null)
                {
                    child = child.children.data;
                    child.childParent = null;
                    child = child.round(factory);
                    {
                        child.childParent = newC;
                        TemplatedLinkedList<HDT> newItem = factory.getTemplatedLinkedList();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->data = child;
                        //newItem.data.copyFrom(child);
                        newItem.data = child;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->next = newC->children;
                        //newItem.next.copyFrom(newC.children);
                        newItem.next = newC.children;
                        newC.children = newItem;
                    };
                }
                return newC;
            }
        }

        // Recurse on non-G-children, build GG->G
        TemplatedLinkedList<HDT> lastG = null;
        int foundGs = 0;
        int downwardsOpenChildren = 0;
        TemplatedLinkedList<HDT> prevChild = null;
        for (TemplatedLinkedList<HDT> i = children; i != null; i = i.next)
        {
            // In each round we first transform all downwards closed C componenets
            // being children of I componenets into G componenets
            if (i.data.type == NodeType.C && type == NodeType.I && i.data.isDownwardsClosed())
            {
                // Convert to G
                i.data.type = NodeType.G;
                i.data.convertedFrom = NodeType.C;
            }

            // Composition 1
            if (i.data.type == NodeType.G)
            {
                foundGs++;

                // We found 2 G's
                if (lastG != null)
                {
                    // Merge the two G's by removing one and replaceing the other with the new G that points to the two old ones

                    // Replace one with a new one with left, right and parent pointers set
                    HDT newG = factory.getHDT(NodeType.G, null, false);
                    newG.left = lastG.data;
                    newG.left.parent = newG;
                    newG.right = i.data;
                    newG.right.parent = newG;
                    newG.childParent = this;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: lastG->data = newG;
                    //lastG.data.copyFrom(newG);
                    lastG.data = newG;

                    // Delete the other
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: prevChild->next = i->next;
                    //prevChild.next.copyFrom(i.next);
                    prevChild.next = i.next;
                    i = prevChild;

                    // Reset lastG
                    lastG = null;
                }
                else
                {
                    lastG = i;
                }

                prevChild = i; //here too as we continue...

                // Don't recurse on G's
                continue;
            }
            if (!i.data.isDownwardsClosed())
            {
                downwardsOpenChildren++;
            }

            // Recurse and save the "new child"
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: i->data = i->data->round(factory);
            //i.data.copyFrom(i.data.round(factory));
            i.data = i.data.round(factory);
            i.data.childParent = this;

            prevChild = i;
        }

        // Non-forking I with 1 G component: IG->C (Composition 2)
        if (type == NodeType.I && downwardsOpenChildren < 2 && foundGs == 1)
        {
            HDT newC = factory.getHDT(NodeType.C, null, false);
            newC.left = this;
            newC.left.parent = newC;
            newC.right = lastG.data; // We've seen 1 G --- we saved that here
            newC.right.parent = newC;
            for (TemplatedLinkedList<HDT> i = children; i != null; i = i.next)
            {
                if (i.data != lastG.data)
                {
                    {
                        i.data.childParent = newC;
                        TemplatedLinkedList<HDT> newItem = factory.getTemplatedLinkedList();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->data = i->data;
                        //newItem.data.copyFrom(i.data);
                        newItem.data = i.data;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: newItem->next = newC->children;
                        //newItem.next.copyFrom(newC.children);
                        newItem.next = newC.children;
                        newC.children = newItem;
                    };
                }
            }

            return newC;
        }

        return this;
    }
    private boolean isDownwardsClosed()
    {
        return children == null;
    }
    private void toDotImpl()
    {
        System.out.print("n");
        System.out.print(this);
        System.out.print("[label=\"");
        if (convertedFrom != NodeType.NotConverted)
        {
            switch (convertedFrom)
            {
                case I:
                    System.out.print("I");
                    break;
                case C:
                    System.out.print("C");
                    break;
                case G:
                    System.out.print("G");
                    break;
                case NotConverted:
                    System.out.print("NotConverted");
                    break; // Shouldn't happen!
            }
            System.out.print(" -> ");
        }
        switch (type)
        {
            case I:
                System.out.print("I");
                break;
            case C:
                System.out.print("C");
                break;
            case G:
                System.out.print("G");
                break;
            case NotConverted:
                System.out.print("NotConverted");
                break; // Shouldn't happen!
        }
        if (link != null)
        {
            System.out.print("; ");
            System.out.print(link.name);
        }
        if (type == NodeType.G && convertedFrom == NodeType.C && left == null && right == null)
        {
            System.out.print("; 0's: ");
            System.out.print(numZeroes);
        }
        System.out.print("\"];");
        System.out.print("\n");

        if (left != null)
        {
            left.toDotImpl();
            System.out.print("n");
            System.out.print(this);
            System.out.print(" -> n");
            System.out.printf("%d", ";");
            System.out.printf("%d", "\n");
        }
        if (right != null)
        {
            right.toDotImpl();
            System.out.printf("%d", "n");
            System.out.printf("%d", this);
            System.out.printf("%d", " -> n");
            System.out.printf("%d", ";");
            System.out.printf("%d", "\n");
        }
    }
    private RootedTree extractAndGoBackImpl(RootedTree addToMe, RootedTreeFactory factory)
    {
        if (this.convertedFrom == NodeType.C && this.left == null && this.right == null)
        {
            if (link == null)
            {
                // We're a leaf, if no link we're a "there once was x leafs with color 0 here"
                link = factory.getRootedTree("");
                link.numZeroes = n_circ;
                if (countingVars.num == 0)
                {
                    link.numZeroes += countingVars.n_i;
                }
            }
            addToMe.addChild(link);
            goBackVariable = addToMe;
            return addToMe;
        }
        else if (left.type == NodeType.I && right.type == NodeType.G)
        {
            // I is newer marked, i.e. if we've got here it's because G is in fact marked!

            // Handle IG -> C (-> G)?
            RootedTree newNode = factory.getRootedTree("");
            goBackVariable = newNode;
            right.extractAndGoBackImpl(newNode, factory);

            left.altMarked = false;
            right.altMarked = false;

            if (type == NodeType.C)
            {
                return newNode;
            }

            // Type = G
            addToMe.addChild(newNode);
            return null;
        }
        else if (convertedFrom == NodeType.C || type == NodeType.C)
        {
            // Handle CC -> C (-> G)?
            RootedTree newLeft;
            RootedTree newRight;
            if (!right.altMarked)
            {
                newLeft = left.extractAndGoBackImpl(null, factory);

                newRight = factory.getRootedTree("");
                newRight.numZeroes = right.n_circ;
                if (right.countingVars.num == 0)
                {
                    newRight.numZeroes += right.countingVars.n_i;
                }

                if (type == NodeType.C)
                {
                    RootedTree realNewRight = factory.getRootedTree("");
                    RootedTree tmp = newRight;
                    newRight = realNewRight;
                    newRight.addChild(tmp);
                }

                right.goBackVariable = newRight;
            }
            else if (!left.altMarked)
            {
                newLeft = factory.getRootedTree("");
                RootedTree newDummy = factory.getRootedTree("");
                newDummy.numZeroes = left.n_circ;
                if (left.countingVars.num == 0)
                {
                    newDummy.numZeroes += left.countingVars.n_i;
                }
                newLeft.addChild(newDummy);
                left.goBackVariable = newLeft;

                newRight = right.extractAndGoBackImpl(null, factory);
            }
            else
            {
                newLeft = left.extractAndGoBackImpl(null, factory);
                newRight = right.extractAndGoBackImpl(null, factory);
            }
            newLeft.addChild(right.goBackVariable);

            goBackVariable = left.goBackVariable;

            left.altMarked = false;
            right.altMarked = false;

            if (type == NodeType.C)
            {
                return newRight;
            }
            // Type G
            addToMe.addChild(goBackVariable);
            return null;
        }
        else if (this.type == NodeType.G)
        {
            // Handle GG->G
            if (!left.altMarked)
            {
                RootedTree newNode = factory.getRootedTree("");
                newNode.numZeroes = left.n_circ;
                if (left.countingVars.num == 0)
                {
                    newNode.numZeroes += left.countingVars.n_i;
                }

                addToMe.addChild(newNode);
                left.goBackVariable = addToMe;
            }
            else
            {
                left.extractAndGoBackImpl(addToMe, factory);
            }

            if (!right.altMarked)
            {
                RootedTree newNode = factory.getRootedTree("");
                newNode.numZeroes = right.n_circ;
                if (right.countingVars.num == 0)
                {
                    newNode.numZeroes += right.countingVars.n_i;
                }

                addToMe.addChild(newNode);
                right.goBackVariable = addToMe;
            }
            else
            {
                right.extractAndGoBackImpl(addToMe, factory);
            }

            left.altMarked = false;
            right.altMarked = false;

            return null;
        }
        else
        {
            System.out.print("Didn't expect this type combination...");
            System.out.print("\n");
            System.exit(-1);
        }
    }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void handleLeaf();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void handleCCToC();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void handleIGToC();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void handleCTransform();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void handleG();

    //C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: bool gotoIteratorValueForList(CountingLinkedList *list, unsigned int num);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		boolean gotoIteratorValueForList(CountingLinkedList list, uint num);
    private enum AddToType
    {
        i_j,
        paren_i_j,
        j_arrow_i,
        i_arrow_j,
        i_paren_i_j,
        paren_i_paren_i_j,
        bracket_i_paren_i_j;

        public int getValue()
        {
            return this.ordinal();
        }

        public static AddToType forValue(int value)
        {
            return values()[value];
        }
    }
//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: INTTYPE_REST getIteratorValueForNumList(CountingLinkedListNumOnly *list, unsigned int num);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		INTTYPE_REST getIteratorValueForNumList(CountingLinkedListNumOnly list, uint num);
//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: bool gotoIteratorValueForNumList(CountingLinkedListNumOnly *list, unsigned int num);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		boolean gotoIteratorValueForNumList(CountingLinkedListNumOnly list, uint num);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		boolean hasIteratorForNumListEnded(CountingLinkedListNumOnly list);
//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: void addToNumList(CountingLinkedList *parent, AddToType list, unsigned int num, INTTYPE_REST value);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//		void addToNumList(CountingLinkedList parent, AddToType list, uint num, INTTYPE_REST value);
///#endif
}
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//ORIGINAL LINE: #define ADD_CHILD(PARENT, NEW_CHILD) { NEW_CHILD->childParent = PARENT; TemplatedLinkedList<HDT*> *newItem = factory->getTemplatedLinkedList(); newItem->data = NEW_CHILD; newItem->next = PARENT->children; PARENT->children = newItem; }

