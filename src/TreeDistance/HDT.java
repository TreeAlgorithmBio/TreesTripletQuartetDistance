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
    
    public void handleLeaf()
	{
		// This is a leaf!
		// Triplets
		if (link == null)
		{
			countingVars.num = 0;
			countingVars.n_i = numZeroes;
		}
		else
		{
			countingVars.num = this.link.color;
			countingVars.n_i = 1;
		}
		countingVars.n_i_circ = 0;
		countingVars.n_paren_ii = 0;
		countingVars.n_i_arrow_circ = 0;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		countingVars.n_0_i = 0;
		countingVars.n_ii = 0;
		countingVars.n_0_paren_ii = 0;
		countingVars.n_circ_paren_ii = 0;
		countingVars.n_i_paren_0_circ = 0;
		countingVars.n_i_paren_circ_circ = 0;
		countingVars.n_i_paren_circ_square = 0;
    
		countingVars.n_paren_0_i = 0;
		countingVars.n_paren_i_circ = 0;
		countingVars.n_paren_0_paren_ii = 0;
		countingVars.n_paren_circ_paren_ii = 0;
		countingVars.n_paren_i_paren_0_circ = 0;
    
		countingVars.n_bracket_0_paren_ii = 0;
		countingVars.n_bracket_circ_paren_ii = 0;
		countingVars.n_bracket_i_paren_0_circ = 0;
		countingVars.n_bracket_i_circ = 0;
    
		countingVars.n_0_arrow_i = 0;
		countingVars.n_i_arrow_0 = 0;
		countingVars.n_i_arrow_i = 0;
		countingVars.n_circ_arrow_i = 0;
		countingVars.n_0_arrow_paren_ii = 0;
		countingVars.n_i_arrow_paren_0_circ = 0;
		countingVars.n_i_arrow_paren_circ_square = 0;
		countingVars.n_circ_arrow_paren_ii = 0;
		countingVars.n_i_arrow_0_circ = 0;
		countingVars.n_i_arrow_circ_circ = 0;
		countingVars.n_i_arrow_circ_square = 0;
		countingVars.n_circ_arrow_ii = 0;
		countingVars.n_paren_ii_arrow_0 = 0;
		countingVars.n_paren_ii_arrow_circ = 0;
		countingVars.n_paren_circ_circ_arrow_i = 0;
		countingVars.n_0_arrow_i_arrow_i = 0;
		countingVars.n_i_arrow_circ_arrow_0 = 0;
		countingVars.n_i_arrow_0_arrow_circ = 0;
		countingVars.n_circ_arrow_i_arrow_i = 0;
    
		// Added by us
		countingVars.n_i_arrow_paren_circ_circ = 0;
		countingVars.n_0_arrow_ii = 0;
		countingVars.n_paren_0_circ_arrow_i = 0;
    
		//countingVars->n_i_j = NULL; // it already is!
		countingVars.n_0_arrow_i_circ = 0;
    
		// Added by us for filling out tables
		// A
		countingVars.n_paren_i_paren_circ_circ = 0;
		countingVars.n_bracket_i_paren_circ_circ = 0;
		countingVars.n_paren_i_paren_circ_square = 0;
		countingVars.n_bracket_i_paren_circ_square = 0;
		countingVars.n_i_arrow_circ_arrow_circ = 0;
		countingVars.n_i_arrow_circ_arrow_square = 0;
		countingVars.n_paren_circ_square_arrow_i = 0;
    
		// New counters for calculating E
		countingVars.n_i_circ_square = 0;
		countingVars.n_i_circ_arrow_square = 0;
		countingVars.n_circ_square_arrow_i = 0;
		countingVars.n_paren_i_circ_square = 0;
		countingVars.n_0_i_circ = 0;
		countingVars.n_i_circ_arrow_0 = 0;
		countingVars.n_0_i_arrow_circ = 0;
		countingVars.n_0_circ_arrow_i = 0;
		countingVars.n_paren_0_i_circ = 0;
		countingVars.n_bracket_i_circ_square = 0;
		countingVars.n_bracket_0_i_circ = 0;
	///#endif
    
		countingVars.type = CountingLinkedList.NodeType.End;
    
		// Sums and stuff (triplets)
		if (countingVars.num != 0)
		{
			n_circ = 1;
		}
		else
		{
			n_circ = 0;
		}
		n_circ_square = 0;
    
		// Sums (quartets)
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		n_0_circ = 0;
		n_circ_circ = 0;
		n_square_paren_circ_circ = 0;
		n_paren_circ_circ = 0;
		n_paren_circ_square = 0;
		n_circ_arrow_paren_square_square = 0;
		n_bracket_circ_square = 0;
		n_paren_0_circ = 0;
    
		n_0_arrow_circ = 0;
		n_circ_arrow_0 = 0;
		n_0_arrow_circ_circ = 0;
    
		// Added by us
		n_circ_arrow_square_square = 0;
    
		// Added by us for filling out tables
		n_bracket_circ_circ = 0;
		n_paren_circ_paren_square_square = 0;
		n_circ_arrow_circ = 0;
		n_circ_arrow_square = 0;
		n_paren_circ_circ_arrow_square = 0;
		n_bracket_circ_paren_square_square = 0;
		n_circ_arrow_square_arrow_square = 0;
    
		// New sums for calculating E
		n_circ_square_triangle = 0;
		n_circ_square_arrow_triangle = 0;
		n_circ_arrow_square_triangle = 0;
		n_paren_circ_square_triangle = 0;
		n_0_circ_square = 0;
		n_0_circ_arrow_square = 0;
		n_0_arrow_circ_square = 0;
		n_circ_square_arrow_0 = 0;
		n_circ_arrow_0_square = 0;
		n_paren_0_circ_square = 0;
		n_bracket_circ_square_triangle = 0;
		n_bracket_0_circ_square = 0;
	///#endif
	}
    
    public long getIteratorValueForNumList(CountingLinkedListNumOnly list, int num)
	{
		if (list == null)
		{
			return 0;
		}
		return list.getIteratorValue(num);
	}
    
    
    public void handleCCToC()
	{
		// Recurse (if not I child) (we really _should_ have both children!)
		if (!left.up2date)
		{
			left.updateCounters();
		}
		if (!right.up2date)
		{
			right.updateCounters();
		}
    
		// NOTE: We generate this with c2 (the higher one) being the left child and c1 (the lower one) being the right child
		HDT c1 = this.right;
		HDT c2 = this.left;
    
		// n_i is just the sum of the 2 children, thus n_circ is also just the sum of the 2 children
		n_circ = c1.n_circ + c2.n_circ;
    
		// Initialize sum to 0 so we can increment it in the loop
		n_circ_square = 0;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		// Initialize sums to 0 so we can increment it in the loop
		n_circ_circ = 0;
		n_square_paren_circ_circ = 0;
		n_paren_circ_circ = 0;
		n_paren_circ_square = 0;
		n_circ_arrow_paren_square_square = 0;
		n_bracket_circ_square = 0;
		n_circ_arrow_square_square = 0; // Added by us
    
		// Not dependent on i
		n_0_circ = c1.n_0_circ + c2.n_0_circ;
		n_paren_0_circ = c1.n_paren_0_circ + c2.n_paren_0_circ;
    
		// Added by us for filling out tables (initialize)
		n_bracket_circ_circ = 0;
		n_paren_circ_paren_square_square = 0;
		n_circ_arrow_circ = 0;
		n_circ_arrow_square = 0;
		n_paren_circ_circ_arrow_square = 0;
		n_bracket_circ_paren_square_square = 0;
		n_circ_arrow_square_arrow_square = 0;
    
		// Fetch the childrens "result counting data"
		quartResolvedAgree = c1.quartResolvedAgree + c2.quartResolvedAgree;
		quartResolvedAgreeDiag = c1.quartResolvedAgreeDiag + c2.quartResolvedAgreeDiag;
		quartResolvedAgreeUpper = c1.quartResolvedAgreeUpper + c2.quartResolvedAgreeUpper;
    
		// Initialize sums and stuff for calculating E
		n_circ_square_triangle = c1.n_circ_square_triangle + c2.n_circ_square_triangle;
		n_circ_square_arrow_triangle = 0;
		n_circ_arrow_square_triangle = 0;
		n_paren_circ_square_triangle = c1.n_paren_circ_square_triangle + c2.n_paren_circ_square_triangle;
		n_0_circ_square = c1.n_0_circ_square + c2.n_0_circ_square;
		n_0_circ_arrow_square = 0;
		n_0_arrow_circ_square = 0;
		n_circ_square_arrow_0 = 0;
		n_circ_arrow_0_square = 0;
		n_paren_0_circ_square = c1.n_paren_0_circ_square + c2.n_paren_0_circ_square;
		n_bracket_circ_square_triangle = 0;
		n_bracket_0_circ_square = 0;
    
		// Fetch sum for calculating E from children
		quartSumE = c1.quartSumE + c2.quartSumE;
	///#endif
    
		// Fetch the childrens "result counting data"
		tripResolved = c1.tripResolved + c2.tripResolved;
		tripUnresolved = c1.tripUnresolved + c2.tripUnresolved;
    
		// Pointer stuff for walking over all children's data
		CountingLinkedList c1Next = c1.countingVars;
		CountingLinkedList c1Count = null;
		CountingLinkedList c2Next = c2.countingVars;
		CountingLinkedList c2Count = null;
		CountingLinkedList ourCount = countingVars;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		long c1Zero = 0;
		long c2Zero = 0;
    
		// The list is sorted, i.e. if zero's there it's the first one!
		if (c1Next.num == 0)
		{
			c1Zero = c1Next.n_i;
		}
		if (c2Next.num == 0)
		{
			c2Zero = c2Next.n_i;
		}
    
		// Not dependent on i (begin)
		n_0_arrow_circ = c1.n_0_arrow_circ + c2.n_0_arrow_circ + c1Zero * c2.n_circ;
    
		n_circ_arrow_0 = c1.n_circ_arrow_0 + c2.n_circ_arrow_0 + c1.n_circ * c2Zero;
    
		n_0_arrow_circ_circ = c1.n_0_arrow_circ_circ + c2.n_0_arrow_circ_circ + c1Zero * c2.n_circ_circ;
		// Not dependent on i (end)
	///#endif
    
		while (c1Next != null || c2Next != null)
		{
			if (c2Next == null || (c1Next != null && c1Next.num < c2Next.num))
			{
				// Operate only on c1
				c1Count = c1Next;
				c2Count = CountingLinkedList.dummyLL;
				ourCount.num = c1Count.num;
    
				if (c1Next.type == CountingLinkedList.NodeType.End)
				{
					c1Next = null;
				}
				else
				{
					c1Next = c1Next.next;
				}
			}
			else if (c1Next == null || (c2Next != null && c2Next.num < c1Next.num))
			{
				// Operate only on c2
				c2Count = c2Next;
				c1Count = CountingLinkedList.dummyLL;
				ourCount.num = c2Count.num;
    
				if (c2Next.type == CountingLinkedList.NodeType.End)
				{
					c2Next = null;
				}
				else
				{
					c2Next = c2Next.next;
				}
			}
			else //c1Count->num == c2Count->num
			{
				c1Count = c1Next;
				c2Count = c2Next;
				ourCount.num = c1Count.num;
    
				if (c1Next.type == CountingLinkedList.NodeType.End)
				{
					c1Next = null;
				}
				else
				{
					c1Next = c1Next.next;
				}
				if (c2Next.type == CountingLinkedList.NodeType.End)
				{
					c2Next = null;
				}
				else
				{
					c2Next = c2Next.next;
				}
			}
    
			// Update counters (triplets)
			ourCount.n_i = c1Count.n_i + c2Count.n_i;
			if (ourCount.num == 0)
			{
				// Go to next one => We're done!
				if (c1Next == null && c2Next == null)
				{
					ourCount.type = CountingLinkedList.NodeType.End;
				}
				else
				{
					// Go to next one (there's more!)
					ourCount.type = CountingLinkedList.NodeType.Regular;
					if (ourCount.next == null)
					{
						ourCount.next = factory.getLL();
					}
					ourCount = ourCount.next;
					c1Count = c1Next;
					c2Count = c2Next;
				}
    
				continue;
			}
			ourCount.n_i_circ = c1Count.n_i_circ + c2Count.n_i_circ;
			ourCount.n_paren_ii = c1Count.n_paren_ii + c2Count.n_paren_ii;
			ourCount.n_i_arrow_circ = c1Count.n_i_arrow_circ + c2Count.n_i_arrow_circ + c1Count.n_i * (c2.n_circ - c2Count.n_i);
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			// 2nd group in figure 12 (quartets only)
			ourCount.n_0_i = c1Count.n_0_i + c2Count.n_0_i;
			ourCount.n_ii = c1Count.n_ii + c2Count.n_ii;
			ourCount.n_0_paren_ii = c1Count.n_0_paren_ii + c2Count.n_0_paren_ii;
			ourCount.n_circ_paren_ii = c1Count.n_circ_paren_ii + c2Count.n_circ_paren_ii;
			ourCount.n_i_paren_0_circ = c1Count.n_i_paren_0_circ + c2Count.n_i_paren_0_circ;
			ourCount.n_i_paren_circ_circ = c1Count.n_i_paren_circ_circ + c2Count.n_i_paren_circ_circ;
			ourCount.n_i_paren_circ_square = c1Count.n_i_paren_circ_square + c2Count.n_i_paren_circ_square;
    
			// In the box
			ourCount.n_bracket_i_circ = ourCount.n_i * (n_circ - ourCount.n_i);
    
			// 3rd group in figure 12 (quartets only)
			ourCount.n_paren_0_i = c1Count.n_paren_0_i + c2Count.n_paren_0_i;
			ourCount.n_paren_i_circ = c1Count.n_paren_i_circ + c2Count.n_paren_i_circ;
			ourCount.n_paren_0_paren_ii = c1Count.n_paren_0_paren_ii + c2Count.n_paren_0_paren_ii;
			ourCount.n_paren_circ_paren_ii = c1Count.n_paren_circ_paren_ii + c2Count.n_paren_circ_paren_ii;
			ourCount.n_paren_i_paren_0_circ = c1Count.n_paren_i_paren_0_circ + c2Count.n_paren_i_paren_0_circ;
    
			// 4th group in figure 12 (quartets only)
			ourCount.n_bracket_0_paren_ii = c1Count.n_bracket_0_paren_ii + c2Count.n_bracket_0_paren_ii + Util.binom2(c1Count.n_i) * c2Zero + c1Zero * c2Count.n_paren_ii + c1Count.n_i * c2Count.n_i_arrow_0;
    
			ourCount.n_bracket_circ_paren_ii = c1Count.n_bracket_circ_paren_ii + c2Count.n_bracket_circ_paren_ii + Util.binom2(c1Count.n_i) * (c2.n_circ - c2Count.n_i) + (c1.n_circ - c1Count.n_i) * c2Count.n_paren_ii + c1Count.n_i * c2Count.n_i_arrow_circ;
    
			ourCount.n_bracket_i_paren_0_circ = c1Count.n_bracket_i_paren_0_circ + c2Count.n_bracket_i_paren_0_circ + c1Zero * (c1.n_circ - c1Count.n_i) * c2Count.n_i + c1Count.n_i * (c2.n_paren_0_circ - c2Count.n_paren_0_i) + c1Zero * c2Count.n_circ_arrow_i + (c1.n_circ - c1Count.n_i) * c2Count.n_0_arrow_i;
    
			// 5th group in figure 12 (quartets only)
			ourCount.n_0_arrow_i = c1Count.n_0_arrow_i + c2Count.n_0_arrow_i + c1Zero * c2Count.n_i;
    
			ourCount.n_i_arrow_0 = c1Count.n_i_arrow_0 + c2Count.n_i_arrow_0 + c1Count.n_i * c2Zero;
    
			ourCount.n_i_arrow_i = c1Count.n_i_arrow_i + c2Count.n_i_arrow_i + c1Count.n_i * c2Count.n_i;
    
			ourCount.n_circ_arrow_i = c1Count.n_circ_arrow_i + c2Count.n_circ_arrow_i + (c1.n_circ - c1Count.n_i) * c2Count.n_i;
    
			ourCount.n_0_arrow_paren_ii = c1Count.n_0_arrow_paren_ii + c2Count.n_0_arrow_paren_ii + c1Zero * c2Count.n_paren_ii;
    
			// NOTICE: THIS HAS CHANGED FROM THE ARTICLE!
			ourCount.n_i_arrow_paren_0_circ = c1Count.n_i_arrow_paren_0_circ + c2Count.n_i_arrow_paren_0_circ + c1Count.n_i * (c2.n_paren_0_circ - c2Count.n_paren_0_i);
    
			ourCount.n_i_arrow_paren_circ_square = c1Count.n_i_arrow_paren_circ_square + c2Count.n_i_arrow_paren_circ_square + c1Count.n_i * (c2.n_paren_circ_square - c2Count.n_paren_i_circ);
    
			ourCount.n_circ_arrow_paren_ii = c1Count.n_circ_arrow_paren_ii + c2Count.n_circ_arrow_paren_ii + (c1.n_circ - c1Count.n_i) * c2Count.n_paren_ii;
    
			ourCount.n_i_arrow_0_circ = c1Count.n_i_arrow_0_circ + c2Count.n_i_arrow_0_circ + c1Count.n_i * (c2.n_0_circ - c2Count.n_0_i);
    
			ourCount.n_i_arrow_circ_circ = c1Count.n_i_arrow_circ_circ + c2Count.n_i_arrow_circ_circ + c1Count.n_i * (c2.n_circ_circ - c2Count.n_ii);
    
			ourCount.n_i_arrow_circ_square = c1Count.n_i_arrow_circ_square + c2Count.n_i_arrow_circ_square + c1Count.n_i * (c2.n_circ_square - c2Count.n_i_circ);
    
			ourCount.n_circ_arrow_ii = c1Count.n_circ_arrow_ii + c2Count.n_circ_arrow_ii + (c1.n_circ - c1Count.n_i) * c2Count.n_ii;
    
			ourCount.n_paren_ii_arrow_0 = c1Count.n_paren_ii_arrow_0 + c2Count.n_paren_ii_arrow_0 + c1Count.n_paren_ii * c2Zero;
    
			ourCount.n_paren_ii_arrow_circ = c1Count.n_paren_ii_arrow_circ + c2Count.n_paren_ii_arrow_circ + c1Count.n_paren_ii * (c2.n_circ - c2Count.n_i);
    
			ourCount.n_paren_circ_circ_arrow_i = c1Count.n_paren_circ_circ_arrow_i + c2Count.n_paren_circ_circ_arrow_i + (c1.n_paren_circ_circ - c1Count.n_paren_ii) * c2Count.n_i;
    
			ourCount.n_0_arrow_i_arrow_i = c1Count.n_0_arrow_i_arrow_i + c2Count.n_0_arrow_i_arrow_i + c1Zero * c2Count.n_i_arrow_i + c1Count.n_0_arrow_i * c2Count.n_i;
    
			ourCount.n_i_arrow_circ_arrow_0 = c1Count.n_i_arrow_circ_arrow_0 + c2Count.n_i_arrow_circ_arrow_0 + c1Count.n_i * (c2.n_circ_arrow_0 - c2Count.n_i_arrow_0) + c1Count.n_i_arrow_circ * c2Zero;
    
			ourCount.n_i_arrow_0_arrow_circ = c1Count.n_i_arrow_0_arrow_circ + c2Count.n_i_arrow_0_arrow_circ + c1Count.n_i * (c2.n_0_arrow_circ - c2Count.n_0_arrow_i) + c1Count.n_i_arrow_0 * (c2.n_circ - c2Count.n_i);
    
			ourCount.n_circ_arrow_i_arrow_i = c1Count.n_circ_arrow_i_arrow_i + c2Count.n_circ_arrow_i_arrow_i + (c1.n_circ - c1Count.n_i) * c2Count.n_i_arrow_i + c1Count.n_circ_arrow_i * c2Count.n_i;
    
			// Added by us!
			ourCount.n_i_arrow_paren_circ_circ = c1Count.n_i_arrow_paren_circ_circ + c2Count.n_i_arrow_paren_circ_circ + c1Count.n_i * (c2.n_paren_circ_circ - c2Count.n_paren_ii);
    
			ourCount.n_0_arrow_ii = c1Count.n_0_arrow_ii + c2Count.n_0_arrow_ii + c1Zero * c2Count.n_ii;
    
			ourCount.n_paren_0_circ_arrow_i = c1Count.n_paren_0_circ_arrow_i + c2Count.n_paren_0_circ_arrow_i + (c1.n_paren_0_circ - c1Count.n_paren_0_i) * c2Count.n_i;
    
			// Figure 15 counters (part 1-4, all with j) & Figure 16 sums (with j)
			ourCount.resetIterator();
			c1Count.resetIterator();
			c2Count.resetIterator();
			c1.countingVars.resetIterator();
			c2.countingVars.resetIterator();
    
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int wantedMax = degree+1;
			int wantedMax = degree+1;
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int lastJPlus1 = 1;
			int lastJPlus1 = 1;
			if (ourCount.n_i_j != null)
			{
				ourCount.n_i_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
			if (ourCount.n_j_arrow_i != null)
			{
				ourCount.n_j_arrow_i.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
			if (ourCount.n_i_arrow_j != null)
			{
				ourCount.n_i_arrow_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
    
			// Added by us for filling out tables
			// A
			ourCount.n_bracket_i_paren_circ_circ = c1Count.n_bracket_i_paren_circ_circ + c2Count.n_bracket_i_paren_circ_circ + (c1.n_bracket_circ_circ - Util.binom2(c1Count.n_i)) * c2Count.n_i + c1Count.n_i * (c2.n_paren_circ_circ - c2Count.n_paren_ii);
    
			ourCount.n_bracket_i_paren_circ_square = c1Count.n_bracket_i_paren_circ_square + c2Count.n_bracket_i_paren_circ_square + (c1.n_bracket_circ_square - c1Count.n_bracket_i_circ) * c2Count.n_i + c1Count.n_i * (c2.n_paren_circ_square - c2Count.n_paren_i_circ);
    
			ourCount.n_i_arrow_circ_arrow_circ = c1Count.n_i_arrow_circ_arrow_circ + c2Count.n_i_arrow_circ_arrow_circ + c1Count.n_i * (c2.n_circ_arrow_circ - c2Count.n_i_arrow_i);
    
			ourCount.n_i_arrow_circ_arrow_square = c1Count.n_i_arrow_circ_arrow_square + c2Count.n_i_arrow_circ_arrow_square + c1Count.n_i * (c2.n_circ_arrow_square - c2Count.n_i_arrow_circ - c2Count.n_circ_arrow_i);
    
			// E
			ourCount.n_i_circ_arrow_square = c1Count.n_i_circ_arrow_square + c2Count.n_i_circ_arrow_square; // More below
    
			ourCount.n_bracket_i_circ_square = c1Count.n_bracket_i_circ_square + c2Count.n_bracket_i_circ_square + c1Count.n_i * (c2.n_circ_square - c2Count.n_i_circ);
    
			while (true)
			{
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int j = wantedMax;
				int j = wantedMax;
                                //FRANK COMMENT OUT
				//NEXT_LEAST_J(c1Count.n_i_j); NEXT_LEAST_J(c2Count.n_i_j); NEXT_LEAST_J(c1Count.n_j_arrow_i); NEXT_LEAST_J(c2Count.n_j_arrow_i); NEXT_LEAST_J(c1Count.n_i_arrow_j); NEXT_LEAST_J(c2Count.n_i_arrow_j); if (gotoIteratorValueForList(c1.countingVars, lastJPlus1) && c1.countingVars.getIteratorNum() < j) j = c1.countingVars.getIteratorNum();
				if (gotoIteratorValueForList(c2.countingVars, lastJPlus1) && c2.countingVars.getIteratorNum() < j)
				{
					j = c2.countingVars.getIteratorNum();
				}
    
				if (j >= wantedMax)
					break;
    
				// n_i_j
				long new_n_i_j = getIteratorValueForNumList(c1Count.n_i_j, j) + getIteratorValueForNumList(c2Count.n_i_j, j);
				addToNumList(ourCount, AddToType.i_j, j, new_n_i_j);
    
				// n_j_arrow_i
				long new_n_j_arrow_i = getIteratorValueForNumList(c1Count.n_j_arrow_i, j) + getIteratorValueForNumList(c2Count.n_j_arrow_i, j) + c1.countingVars.getIteratorValue(j).n_i * c2Count.n_i;
				addToNumList(ourCount, AddToType.j_arrow_i, j, new_n_j_arrow_i);
    
				// n_i_arrow_j
				long new_n_i_arrow_j = getIteratorValueForNumList(c1Count.n_i_arrow_j, j) + getIteratorValueForNumList(c2Count.n_i_arrow_j, j) + c1Count.n_i * c2.countingVars.getIteratorValue(j).n_i;
				addToNumList(ourCount, AddToType.i_arrow_j, j, new_n_i_arrow_j);
    
				if (ourCount.num != j)
				{
					// i != j
					long addThis; 
    
					// E start
					// n_i_circ_arrow_square
					addThis = getIteratorValueForNumList(c1Count.n_i_j, j) * (c2.n_circ - c2Count.n_i - c2.countingVars.getIteratorValue(j).n_i); // j -  ij
					if (addThis < 0)
					{
						System.out.print("WTF #15?!?");
						System.out.print("\n");
					}
					ourCount.n_i_circ_arrow_square += addThis;
    
					addThis = (c1.n_circ - c1Count.n_i - c1.countingVars.getIteratorValue(j).n_i) * getIteratorValueForNumList(c2Count.n_i_j, j); // ij -  j
					if (addThis < 0)
					{
						System.out.print("WTF #17?!?");
						System.out.print("\n");
					}
					ourCount.n_bracket_i_circ_square += addThis;
					// E end
    
					// Added by us for filling out tables
					ourCount.n_bracket_i_paren_circ_circ += c1.countingVars.getIteratorValue(j).n_i * getIteratorValueForNumList(c2Count.n_j_arrow_i, j);
					ourCount.n_bracket_i_paren_circ_square += c1.countingVars.getIteratorValue(j).n_i * (c2Count.n_circ_arrow_i - getIteratorValueForNumList(c2Count.n_j_arrow_i, j));
					ourCount.n_i_arrow_circ_arrow_circ += getIteratorValueForNumList(c1Count.n_i_arrow_j, j) * c2.countingVars.getIteratorValue(j).n_i;
					ourCount.n_i_arrow_circ_arrow_square += getIteratorValueForNumList(c1Count.n_i_arrow_j, j) * (c2.n_circ - c2Count.n_i - c2.countingVars.getIteratorValue(j).n_i);
				}
				lastJPlus1 = j + 1;
			}
    
			ourCount.n_0_arrow_i_circ = c1Count.n_0_arrow_i_circ + c2Count.n_0_arrow_i_circ + c1Zero * c2Count.n_i_circ;
    
			// Added by us for filling out tables
			ourCount.n_paren_i_paren_circ_circ = c1Count.n_paren_i_paren_circ_circ + c2Count.n_paren_i_paren_circ_circ;
    
			ourCount.n_paren_i_paren_circ_square = c1Count.n_paren_i_paren_circ_square + c2Count.n_paren_i_paren_circ_square;
    
			ourCount.n_paren_circ_square_arrow_i = c1Count.n_paren_circ_square_arrow_i + c2Count.n_paren_circ_square_arrow_i + (c1.n_paren_circ_square - c1Count.n_paren_i_circ) * c2Count.n_i;
    
			// New counters for calculating E
			ourCount.n_i_circ_square = c1Count.n_i_circ_square + c2Count.n_i_circ_square;
    
			ourCount.n_circ_square_arrow_i = c1Count.n_circ_square_arrow_i + c2Count.n_circ_square_arrow_i + (c1.n_circ_square - c1Count.n_i_circ) * c2Count.n_i;
    
			ourCount.n_paren_i_circ_square = c1Count.n_paren_i_circ_square + c2Count.n_paren_i_circ_square;
    
			ourCount.n_0_i_circ = c1Count.n_0_i_circ + c2Count.n_0_i_circ;
    
			ourCount.n_i_circ_arrow_0 = c1Count.n_i_circ_arrow_0 + c2Count.n_i_circ_arrow_0 + c1Count.n_i_circ * c2Zero;
    
			ourCount.n_0_i_arrow_circ = c1Count.n_0_i_arrow_circ + c2Count.n_0_i_arrow_circ + c1Count.n_0_i * (c2.n_circ - c2Count.n_i);
    
			ourCount.n_0_circ_arrow_i = c1Count.n_0_circ_arrow_i + c2Count.n_0_circ_arrow_i + (c1.n_0_circ - c1Count.n_0_i) * c2Count.n_i;
    
			ourCount.n_paren_0_i_circ = c1Count.n_paren_0_i_circ + c2Count.n_paren_0_i_circ;
    
			ourCount.n_bracket_0_i_circ = c1Count.n_bracket_0_i_circ + c2Count.n_bracket_0_i_circ + c1Zero * c2Count.n_i_circ + c1Count.n_i * (c2.n_0_circ - c2Count.n_0_i) + (c1.n_circ - c1Count.n_i) * c2Count.n_0_i;
	///#endif
    
			// Sums and stuff
			n_circ_square += ourCount.n_i_circ;
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			n_circ_circ += ourCount.n_ii;
			n_square_paren_circ_circ += ourCount.n_i_paren_circ_circ;
			n_paren_circ_circ += ourCount.n_paren_ii;
			n_paren_circ_square += ourCount.n_paren_i_circ;
			n_circ_arrow_paren_square_square += ourCount.n_circ_arrow_paren_ii; // FIXED!!
			n_bracket_circ_square += ourCount.n_bracket_i_circ;
    
			// Added by us
			n_circ_arrow_square_square += ourCount.n_i_arrow_circ_circ;
    
			// Added by us for filling out tables
			// A
			n_bracket_circ_circ += Util.binom2(ourCount.n_i);
			n_paren_circ_paren_square_square += ourCount.n_paren_i_paren_circ_circ;
			n_circ_arrow_circ += ourCount.n_i_arrow_i;
			n_circ_arrow_square += ourCount.n_i_arrow_circ;
			n_paren_circ_circ_arrow_square += ourCount.n_paren_ii_arrow_circ;
			n_bracket_circ_paren_square_square += ourCount.n_bracket_i_paren_circ_circ;
			n_circ_arrow_square_arrow_square += ourCount.n_circ_arrow_i_arrow_i;
    
			// New sums and stuff for calculating E
			n_circ_square_arrow_triangle += ourCount.n_circ_square_arrow_i;
			n_circ_arrow_square_triangle += ourCount.n_i_arrow_circ_square;
    
			n_0_circ_arrow_square += ourCount.n_0_i_arrow_circ;
			n_0_arrow_circ_square += ourCount.n_0_arrow_i_circ;
			n_circ_square_arrow_0 += ourCount.n_i_circ_arrow_0;
			n_circ_arrow_0_square += ourCount.n_i_arrow_0_circ;
    
			n_bracket_circ_square_triangle += ourCount.n_bracket_i_circ_square;
			n_bracket_0_circ_square += ourCount.n_bracket_0_i_circ;
	///#endif
    
			// Update resolved/resolved and unresolved/unresolved
			// (Figure 10)
			tripResolved += c1Count.n_i * c2Count.n_i_arrow_circ;
			tripResolved += Util.binom2(c1Count.n_i) * (c2.n_circ - c2Count.n_i);
			tripResolved += (c1.n_circ - c1Count.n_i) * c2Count.n_paren_ii;
			tripUnresolved += c1Count.n_i * (c2.n_circ_square - c2Count.n_i_circ);
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			//quartResolvedAgree & quartResolvedDisagree
    
			// alpha & alpha
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_i_arrow_paren_circ_circ;
			quartResolvedAgreeDiag += (long) Util.binom2(c1Count.n_i) * (c2.n_paren_circ_circ - c2Count.n_paren_ii);
    
			// beta & alpha
			quartResolvedAgree += (long) c1Count.n_i * c2Count.n_i_arrow_paren_circ_square;
			quartResolvedAgree += (long) Util.binom2(c1Count.n_i) * (c2.n_paren_circ_square - c2Count.n_paren_i_circ);
			quartResolvedAgree += (long)(c1.n_bracket_circ_square - c1Count.n_bracket_i_circ) * c2Count.n_paren_ii;
			quartResolvedAgree += (long) c1Count.n_i * (c2.n_circ_arrow_paren_square_square - c2Count.n_i_arrow_paren_circ_circ - c2Count.n_circ_arrow_paren_ii);
    
			// beta & beta (part 1)
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_i_arrow_circ_square;
			quartResolvedAgreeDiag += (long) Util.binom2(c1Count.n_i) * (c2.n_circ_square - c2Count.n_i_circ);
			quartResolvedAgreeDiag += (long) c1Count.n_i * (c2.n_square_paren_circ_circ - c2Count.n_i_paren_circ_circ - c2Count.n_circ_paren_ii);
    
			// beta & beta (part 2)
			quartResolvedAgreeDiag += (long) c1Count.n_i * (c2.n_circ_arrow_square_square - c2Count.n_i_arrow_circ_circ - c2Count.n_circ_arrow_ii);
			quartResolvedAgreeDiag += (long)(c1.n_bracket_circ_square - c1Count.n_bracket_i_circ) * c2Count.n_ii;
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_i_paren_circ_square;
    
			// gamma & alpha
			quartResolvedAgree += (long) c1Count.n_i * c2Count.n_i_arrow_paren_0_circ;
			quartResolvedAgree += (long) Util.binom2(c1Count.n_i) * (c2.n_paren_0_circ - c2Count.n_paren_0_i);
			quartResolvedAgree += (long) c1Zero * (c1.n_circ - c1Count.n_i) * c2Count.n_paren_ii;
			quartResolvedAgree += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_0_arrow_paren_ii;
			quartResolvedAgree += (long) c1Zero * c2Count.n_circ_arrow_paren_ii;
    
			// gamma & beta (part 1)
			quartResolvedAgree += (long) Util.binom2(c1Count.n_i) * (c2.n_0_circ - c2Count.n_0_i);
			quartResolvedAgree += (long) c1Count.n_i * c2Count.n_i_arrow_0_circ;
			quartResolvedAgree += (long) c1Zero * c2Count.n_circ_paren_ii;
			quartResolvedAgree += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_0_paren_ii;
    
			// gamma & beta (part 2)
			quartResolvedAgree += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_0_arrow_ii;
			quartResolvedAgree += (long) c1Zero * c2Count.n_circ_arrow_ii;
			quartResolvedAgree += (long) c1Zero * (c1.n_circ - c1Count.n_i) * c2Count.n_ii;
			quartResolvedAgree += (long) c1Count.n_i * c2Count.n_i_paren_0_circ;
    
			// gamma & gamma (part 1)
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_i_arrow_circ_arrow_0;
			quartResolvedAgreeDiag += (long) Util.binom2(c1Count.n_i) * (c2.n_circ_arrow_0 - c2Count.n_i_arrow_0);
			quartResolvedAgreeDiag += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_paren_ii_arrow_0;
			quartResolvedAgreeDiag += (long) c1Count.n_bracket_circ_paren_ii * c2Zero;
			quartResolvedAgreeDiag += (long) c1Zero * c2Count.n_paren_circ_paren_ii;
    
			// gamma & gamma (part 2)
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_i_arrow_0_arrow_circ;
			quartResolvedAgreeDiag += (long) Util.binom2(c1Count.n_i) * (c2.n_0_arrow_circ - c2Count.n_0_arrow_i);
			quartResolvedAgreeDiag += (long) c1Zero * c2Count.n_paren_ii_arrow_circ;
			quartResolvedAgreeDiag += (long) c1Count.n_bracket_0_paren_ii * (c2.n_circ - c2Count.n_i);
			quartResolvedAgreeDiag += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_paren_0_paren_ii;
    
			// gamma & gamma (part 3) (aka the exiting conclusion)
			quartResolvedAgreeDiag += (long) c1Zero * c2Count.n_circ_arrow_i_arrow_i;
			quartResolvedAgreeDiag += (long)(c1.n_circ - c1Count.n_i) * c2Count.n_0_arrow_i_arrow_i;
			quartResolvedAgreeDiag += (long) c1Zero * (c1.n_circ - c1Count.n_i) * c2Count.n_i_arrow_i;
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_paren_0_circ_arrow_i;
			quartResolvedAgreeDiag += (long) c1Count.n_bracket_i_paren_0_circ * c2Count.n_i;
			quartResolvedAgreeDiag += (long) c1Count.n_i * c2Count.n_paren_i_paren_0_circ;
    
			//
			// +------------------------------------------+
			// | New sums for calculating A (Added by us) |
			// +------------------------------------------+
			//
    
			// alpha & beta
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_i_arrow_circ_circ;
			quartResolvedAgreeUpper += (long) Util.binom2(c1Count.n_i) * (c2.n_circ_circ - c2Count.n_ii);
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_i_paren_circ_circ;
    
			// alpha & gamma
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_i_arrow_circ_arrow_circ;
			quartResolvedAgreeUpper += (long) Util.binom2(c1Count.n_i) * (c2.n_circ_arrow_circ - c2Count.n_i_arrow_i);
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_paren_circ_circ_arrow_i;
			quartResolvedAgreeUpper += (long) c1Count.n_bracket_i_paren_circ_circ * c2Count.n_i;
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_paren_i_paren_circ_circ;
    
			// beta & gamma (part 1)
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_i_arrow_circ_arrow_square;
			quartResolvedAgreeUpper += (long) Util.binom2(c1Count.n_i) * (c2.n_circ_arrow_square - c2Count.n_i_arrow_circ - c2Count.n_circ_arrow_i);
			quartResolvedAgreeUpper += (long) c1Count.n_i * (c2.n_paren_circ_circ_arrow_square - c2Count.n_paren_ii_arrow_circ - c2Count.n_paren_circ_circ_arrow_i);
			quartResolvedAgreeUpper += (long)(c1.n_bracket_circ_paren_square_square - c1Count.n_bracket_i_paren_circ_circ - c1Count.n_bracket_circ_paren_ii) * c2Count.n_i;
			quartResolvedAgreeUpper += (long) c1Count.n_i * (c2.n_paren_circ_paren_square_square - c2Count.n_paren_i_paren_circ_circ - c2Count.n_paren_circ_paren_ii);
    
			// beta & gamma (part 2)
			quartResolvedAgreeUpper += (long) c1Count.n_i * (c2.n_circ_arrow_square_arrow_square - c2Count.n_i_arrow_circ_arrow_circ - c2Count.n_circ_arrow_i_arrow_i);
			quartResolvedAgreeUpper += (long)(c1.n_bracket_circ_square - c1Count.n_bracket_i_circ) * c2Count.n_i_arrow_i;
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_paren_circ_square_arrow_i;
			quartResolvedAgreeUpper += (long) c1Count.n_bracket_i_paren_circ_square * c2Count.n_i;
			quartResolvedAgreeUpper += (long) c1Count.n_i * c2Count.n_paren_i_paren_circ_square;
    
			//
			// +------------------------------------------+
			// | New sums for calculating E (Added by us) |
			// +------------------------------------------+
			//
    
			// delta & delta
			quartSumE += (long) c1Count.n_i * (c2.n_circ_square_triangle - c2Count.n_i_circ_square);
    
			// delta & epsilon
			quartSumE += (long) c1Count.n_i * (c2.n_circ_square_arrow_triangle - c2Count.n_i_circ_arrow_square - c2Count.n_circ_square_arrow_i);
			quartSumE += (long)(c1.n_bracket_circ_square_triangle - c1Count.n_bracket_i_circ_square) * c2Count.n_i;
			quartSumE += (long) c1Count.n_i * (c2.n_paren_circ_square_triangle - c2Count.n_paren_i_circ_square);
    
			// epsilon & delta (countinues below)
			quartSumE += (long) c1Count.n_i * (c2.n_0_circ_square - c2Count.n_0_i_circ);
    
			// epsilon & epsilon (part 1) (continues below)
			quartSumE += (long) c1Count.n_i * (c2.n_circ_square_arrow_0 - c2Count.n_i_circ_arrow_0);
    
			// epsilon & epsilon (part 2) (continues below)
			quartSumE += (long) c1Count.n_i * (c2.n_0_circ_arrow_square - c2Count.n_0_i_arrow_circ - c2Count.n_0_circ_arrow_i);
			quartSumE += (long)(c1.n_bracket_0_circ_square - c1Count.n_bracket_0_i_circ) * c2Count.n_i;
			quartSumE += (long) c1Count.n_i * (c2.n_paren_0_circ_square - c2Count.n_paren_0_i_circ);
	///#endif
    
			// Go to next on children unless we're done
			if (c1Next == null && c2Next == null)
			{
				ourCount.type = CountingLinkedList.NodeType.End;
			}
			else
			{
				// Go to next one (there's more!)
				ourCount.type = CountingLinkedList.NodeType.Regular;
				if (ourCount.next == null)
				{
					ourCount.next = factory.getLL();
				}
				ourCount = ourCount.next;
			}
		}
		n_circ_square /= 2;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		n_paren_circ_square /= 2;
		n_bracket_circ_square /= 2;
    
		// New sums for calculating E
		// epsilon & delta (continued from inside the loop)
		quartSumE += (long) c1Zero * c2.n_circ_square_triangle;
    
		// epsilon & epsilon (part 1) (continued from inside the loop)
		quartSumE += (long) c1.n_bracket_circ_square_triangle * c2Zero;
		quartSumE += (long) c1Zero * c2.n_paren_circ_square_triangle;
    
		// epsilon & epsilon (part 2) (continued from inside the loop)
		quartSumE += (long) c1Zero * c2.n_circ_square_arrow_triangle;
    
		// Div E sum-counters :)
		if (n_bracket_circ_square_triangle % 3 != 0)
		{
			System.out.print("n_bracket_circ_square_triangle mod 3 check failed :(");
			System.out.print("\n");
		}
		n_bracket_circ_square_triangle /= 3;
    
		if (n_bracket_0_circ_square % 2 != 0)
		{
			System.out.print("n_bracket_0_circ_square mod 2 check failed :(");
			System.out.print("\n");
		}
		n_bracket_0_circ_square /= 2;
    
		if (n_circ_square_arrow_0 % 2 != 0)
		{
			System.out.print("n_circ_square_arrow_0 mod 2 check failed :(");
			System.out.print("\n");
		}
		n_circ_square_arrow_0 /= 2;
    
		if (n_0_arrow_circ_square % 2 != 0)
		{
			System.out.print("n_0_arrow_circ_square mod 2 check failed :(");
			System.out.print("\n");
		}
		n_0_arrow_circ_square /= 2;
	///#endif
	}
    
    public void handleIGToC()
	{
		// NOTE: We generate this with left being I and right being G!
    
		if (!right.up2date)
		{
			right.updateCounters();
		}
    
		// Fetch the childrens "result counting data"
		tripResolved = right.tripResolved;
		tripUnresolved = right.tripUnresolved;
    
		// These are just inherited from the G node
		n_circ = right.n_circ;
		n_circ_square = right.n_circ_square;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		// Fetch the childrens "result counting data"
		quartResolvedAgree = right.quartResolvedAgree;
		quartResolvedAgreeDiag = right.quartResolvedAgreeDiag;
		quartResolvedAgreeUpper = right.quartResolvedAgreeUpper;
    
		// These sums are just inherited from the G node
		n_circ_circ = right.n_circ_circ;
		n_square_paren_circ_circ = right.n_square_paren_circ_circ;
		n_paren_circ_circ = right.n_paren_circ_circ;
		n_paren_circ_square = right.n_paren_circ_square;
		n_bracket_circ_square = right.n_bracket_circ_square;
    
		// Sums
		// Not actually defined in G! IN G!!! (i.e. it *should* in fact be 0)
		n_circ_arrow_paren_square_square = 0;
		n_circ_arrow_square_square = 0; // Added by us
    
		// Not dependent on i
		n_0_circ = right.n_0_circ;
		n_paren_0_circ = right.n_paren_0_circ;
    
		// 5th group, not dependent on i
		// Not actually defined in G! IN G!!! (i.e. it *should* in fact be 0)
		n_0_arrow_circ = 0;
		n_circ_arrow_0 = 0;
		n_0_arrow_circ_circ = 0;
    
		// Added by us for filling out tables
		n_bracket_circ_circ = right.n_bracket_circ_circ;
		n_paren_circ_paren_square_square = right.n_paren_circ_paren_square_square;
		n_circ_arrow_circ = 0;
		n_circ_arrow_square = 0;
		n_paren_circ_circ_arrow_square = 0;
		n_bracket_circ_paren_square_square = right.n_bracket_circ_paren_square_square;
		n_circ_arrow_square_arrow_square = 0;
    
		// Initialize sums and stuff for calculating E
		n_circ_square_triangle = right.n_circ_square_triangle;
		n_circ_square_arrow_triangle = 0;
		n_circ_arrow_square_triangle = 0;
		n_paren_circ_square_triangle = right.n_paren_circ_square_triangle;
		n_0_circ_square = right.n_0_circ_square;
		n_0_circ_arrow_square = 0;
		n_0_arrow_circ_square = 0;
		n_circ_square_arrow_0 = 0;
		n_circ_arrow_0_square = 0;
		n_paren_0_circ_square = right.n_paren_0_circ_square;
		n_bracket_circ_square_triangle = right.n_bracket_circ_square_triangle;
		n_bracket_0_circ_square = right.n_bracket_0_circ_square;
    
		// Fetch sum for calculating E from children
		quartSumE = right.quartSumE;
	///#endif
    
		CountingLinkedList current = right.countingVars;
		CountingLinkedList ourCount = countingVars;
		while (current != null)
		{
			ourCount.num = current.num;
			ourCount.type = current.type;
    
			// Triplets
			ourCount.n_i = current.n_i;
			if (ourCount.num == 0)
			{
				// Go to next one => We're done!
				if (ourCount.type != CountingLinkedList.NodeType.End)
				{
					// Go to next one (there's more!)
					if (ourCount.next == null)
					{
						ourCount.next = factory.getLL();
					}
					ourCount = ourCount.next;
    
					current = current.next;
				}
				else
				{
					current = null;
				}
    
				continue;
			}
    
			ourCount.n_i_circ = current.n_i_circ;
			ourCount.n_paren_ii = current.n_paren_ii;
			ourCount.n_i_arrow_circ = 0; // Not actually defined in G! IN G!!! (i.e. it *should* in fact be 0)
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			// 2nd group in figure 12 (quartets only)
			ourCount.n_0_i = current.n_0_i;
			ourCount.n_ii = current.n_ii;
			ourCount.n_0_paren_ii = current.n_0_paren_ii;
			ourCount.n_circ_paren_ii = current.n_circ_paren_ii;
			ourCount.n_i_paren_0_circ = current.n_i_paren_0_circ;
			ourCount.n_i_paren_circ_circ = current.n_i_paren_circ_circ;
			ourCount.n_i_paren_circ_square = current.n_i_paren_circ_square;
    
			// In the box (based on calculations that are just inherited why we can just inherit the value)
			ourCount.n_bracket_i_circ = current.n_bracket_i_circ;
    
			// 3rd group in figure 12 (quartets only)
			ourCount.n_paren_0_i = current.n_paren_0_i;
			ourCount.n_paren_i_circ = current.n_paren_i_circ;
			ourCount.n_paren_0_paren_ii = current.n_paren_0_paren_ii;
			ourCount.n_paren_circ_paren_ii = current.n_paren_circ_paren_ii;
			ourCount.n_paren_i_paren_0_circ = current.n_paren_i_paren_0_circ;
    
			// 4th group in figure 12 (quartets only)
			ourCount.n_bracket_0_paren_ii = current.n_bracket_0_paren_ii;
			ourCount.n_bracket_circ_paren_ii = current.n_bracket_circ_paren_ii;
			ourCount.n_bracket_i_paren_0_circ = current.n_bracket_i_paren_0_circ;
    
			// 5th group in figure 12 (quartets only)
			ourCount.n_0_arrow_i = 0;
			ourCount.n_i_arrow_0 = 0;
			ourCount.n_i_arrow_i = 0;
			ourCount.n_circ_arrow_i = 0;
			ourCount.n_0_arrow_paren_ii = 0;
			ourCount.n_i_arrow_paren_0_circ = 0;
			ourCount.n_i_arrow_paren_circ_square = 0;
			ourCount.n_circ_arrow_paren_ii = 0;
			ourCount.n_i_arrow_0_circ = 0;
			ourCount.n_i_arrow_circ_circ = 0;
			ourCount.n_i_arrow_circ_square = 0;
			ourCount.n_circ_arrow_ii = 0;
			ourCount.n_paren_ii_arrow_0 = 0;
			ourCount.n_paren_ii_arrow_circ = 0;
			ourCount.n_paren_circ_circ_arrow_i = 0;
			ourCount.n_0_arrow_i_arrow_i = 0;
			ourCount.n_i_arrow_circ_arrow_0 = 0;
			ourCount.n_i_arrow_0_arrow_circ = 0;
			ourCount.n_circ_arrow_i_arrow_i = 0;
    
			// Added by us
			ourCount.n_i_arrow_paren_circ_circ = 0;
			ourCount.n_0_arrow_ii = 0;
			ourCount.n_paren_0_circ_arrow_i = 0;
    
			// Figure 15 counters (part 1+2, all with j)
			ourCount.resetIterator();
			current.resetIterator();
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int wantedMax = degree+1;
			int wantedMax = degree+1;
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int lastJPlus1 = 1;
			int lastJPlus1 = 1;
			if (ourCount.n_i_j != null)
			{
				ourCount.n_i_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
    
			while (true)
			{
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int j = wantedMax;
				int j = wantedMax;
                                //FRANK C OUT
				//NEXT_LEAST_J(current.n_i_j) 
                                if (j >= wantedMax) break;
    
				// n_i_j
				addToNumList(ourCount, AddToType.i_j, j, getIteratorValueForNumList(current.n_i_j, j));
				lastJPlus1 = j + 1;
			}
    
			ourCount.n_0_arrow_i_circ = 0;
    
    
			// Added by us for filling out tables
			// A
			ourCount.n_paren_i_paren_circ_circ = current.n_paren_i_paren_circ_circ;
			ourCount.n_bracket_i_paren_circ_circ = current.n_bracket_i_paren_circ_circ;
			if (ourCount.n_j_arrow_i != null)
			{
				ourCount.n_j_arrow_i.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
			ourCount.n_paren_i_paren_circ_square = current.n_paren_i_paren_circ_square;
			ourCount.n_bracket_i_paren_circ_square = current.n_bracket_i_paren_circ_square;
			ourCount.n_i_arrow_circ_arrow_circ = 0;
			ourCount.n_i_arrow_circ_arrow_square = 0;
			ourCount.n_paren_circ_square_arrow_i = 0;
			if (ourCount.n_i_arrow_j != null)
			{
				ourCount.n_i_arrow_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
    
			// New counters for calculating E
			ourCount.n_i_circ_square = current.n_i_circ_square;
			ourCount.n_i_circ_arrow_square = 0;
			ourCount.n_circ_square_arrow_i = 0;
			ourCount.n_paren_i_circ_square = current.n_paren_i_circ_square;
			ourCount.n_0_i_circ = current.n_0_i_circ;
			ourCount.n_i_circ_arrow_0 = 0;
			ourCount.n_0_i_arrow_circ = 0;
			ourCount.n_0_circ_arrow_i = 0;
			ourCount.n_paren_0_i_circ = current.n_paren_0_i_circ;
			ourCount.n_bracket_i_circ_square = current.n_bracket_i_circ_square;
			ourCount.n_bracket_0_i_circ = current.n_bracket_0_i_circ;
	///#endif
    
			// Go to next on children unless we're done
			if (ourCount.type != CountingLinkedList.NodeType.End)
			{
				// Go to next one (there's more!)
				if (ourCount.next == null)
				{
					ourCount.next = factory.getLL();
				}
				ourCount = ourCount.next;
    
				current = current.next;
			}
			else
			{
				current = null;
			}
		}
	}
    
    public void handleCTransform()
	{
		// Triplets (sum, n_i_circ has been set to 0, i.e. n_circ_squar is also 0!)
		n_circ_square = 0;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		// Sums initialized to 0, summed in loop below
		n_paren_circ_circ = 0;
		n_paren_circ_square = 0;
    
		// Not dependent on i, thus placed here. Value is 0 (always! (when transforming C to G))
		n_0_circ = 0;
    
		// Get the number of leafs with color 0
		// The list is sorted, i.e. if zero's there it's the first one!
		long gZero = 0;
		if (countingVars.num == 0)
		{
			gZero = countingVars.n_i;
		}
    
		// Not dependent on i (n_circ has been updated in handleC!)
		n_paren_0_circ = gZero * n_circ;
    
    
		/*
		// Added by us for filling out tables (Actually undefined!)
		n_circ_arrow_circ = 0;
		n_circ_arrow_square = 0;
		n_paren_circ_circ_arrow_square = 0;
		n_circ_arrow_square_arrow_square = 0;
		*/
    
		// Added by us for filling out tables (initialize)
		n_paren_circ_paren_square_square = 0;
    
		// Reset sums and stuff for calculating E
		n_circ_square_triangle = 0;
		n_0_circ_square = 0;
		n_paren_circ_square_triangle = 0;
		n_paren_0_circ_square = 0;
	///#endif
    
		CountingLinkedList current = countingVars;
		if (current.num == 0)
		{
			if (current.type == CountingLinkedList.NodeType.End)
			{
				current = null;
			}
			else
			{
				current = current.next; // don't do weird stuff for the n_0 case...
			}
		}
    
		while (current != null)
		{
			//current->n_i is unchanged by this transform!
			// Triplets
			current.n_i_circ = 0;
			current.n_paren_ii = Util.binom2(current.n_i);
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			// 2nd group in figure 12 (quartets only)
			current.n_0_i = 0;
			current.n_ii = 0;
			current.n_0_paren_ii = 0;
			current.n_circ_paren_ii = 0;
			current.n_i_paren_0_circ = 0;
			current.n_i_paren_circ_circ = 0;
			current.n_i_paren_circ_square = 0;
    
			// 3nd group in figure 12 (quartets only)
			current.n_paren_0_i = gZero * current.n_i;
			current.n_paren_i_circ = current.n_bracket_i_circ; // Wee, we could spare a calculation here =)
			current.n_paren_0_paren_ii = current.n_bracket_0_paren_ii;
			current.n_paren_circ_paren_ii = current.n_bracket_circ_paren_ii;
			current.n_paren_i_paren_0_circ = current.n_bracket_i_paren_0_circ;
    
			// Count up the sums
			n_paren_circ_circ += current.n_paren_ii;
			n_paren_circ_square += current.n_paren_i_circ;
    
			// Figure 15 counters (part 1)
			if (current.n_i_j != null)
			{
				current.n_i_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
    
			// Added by us for filling out tables
			// A
			current.n_paren_i_paren_circ_circ = current.n_bracket_i_paren_circ_circ;
			current.n_paren_i_paren_circ_square = current.n_bracket_i_paren_circ_square;
    
			// Added by us for filling out tables (sum)
			// A
			n_paren_circ_paren_square_square += current.n_paren_i_paren_circ_circ;
    
			// New counters for calculating E
			current.n_i_circ_square = 0;
			current.n_0_i_circ = 0;
			current.n_paren_i_circ_square = current.n_bracket_i_circ_square;
			current.n_paren_0_i_circ = current.n_bracket_0_i_circ;
    
			// New sums for E
			n_paren_circ_square_triangle += current.n_paren_i_circ_square;
			n_paren_0_circ_square += current.n_paren_0_i_circ;
	///#endif
    
			// Go to next on children unless we're done
			if (current.type != CountingLinkedList.NodeType.End)
			{
				// Go to next one (there's more!)
				current = current.next;
			}
			else
			{
				current = null;
			}
		}
    
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		// These are all based on stuff that is reset to 0
		n_circ_circ = 0;
		n_square_paren_circ_circ = 0;
    
		// Halve it :)
		n_paren_circ_square /= 2;
    
		// Take a third of some of the E sums
		if (n_paren_circ_square_triangle % 3 != 0)
		{
			System.out.print("n_paren_circ_square_triangle mod 3 test... FAIL!!!");
			System.out.print("\n");
		}
		n_paren_circ_square_triangle /= 3;
    
		// Halve some other ones :)
		if (n_paren_0_circ_square % 2 != 0)
		{
			System.out.print("n_paren_0_circ_square mod 2 test... FAIL!!!");
			System.out.print("\n");
		}
		n_paren_0_circ_square /= 2;
	///#endif
	}
    
    public void handleG()
	{
		// Not a leaf, i.e. a GG->G
		HDT g1 = this.left;
		HDT g2 = this.right;
    
		if (!g1.up2date)
		{
			g1.updateCounters();
		}
		if (!g2.up2date)
		{
			g2.updateCounters();
		}
    
		// n_i is just the sum of the 2 children, thus n_circ is also just the sum of the 2 children
		n_circ = g1.n_circ + g2.n_circ;
    
		// Initialize sum to 0 so we can increment it in the loop
		n_circ_square = 0;
    
		// Fetch the childrens "result counting data"
		tripResolved = g1.tripResolved + g2.tripResolved;
		tripUnresolved = g1.tripUnresolved + g2.tripUnresolved;
    
		// Pointer stuff for walking over all children's data
		CountingLinkedList g1Next = g1.countingVars;
		CountingLinkedList g1Count = null;
		CountingLinkedList g2Next = g2.countingVars;
		CountingLinkedList g2Count = null;
		CountingLinkedList ourCount = countingVars;
    
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		long g1Zero = 0;
		long g2Zero = 0;
    
		// The list is sorted, i.e. if zero's there it's the first one!
		if (g1Next.num == 0)
		{
			g1Zero = g1Next.n_i;
		}
		if (g2Next.num == 0)
		{
			g2Zero = g2Next.n_i;
		}
    
		// Not dependent on i (begin)
		n_0_circ = g1.n_0_circ + g2.n_0_circ + g1Zero * g2.n_circ + g2Zero * g1.n_circ;
    
		n_paren_0_circ = g1.n_paren_0_circ + g2.n_paren_0_circ;
		// Not dependent on i (end)
    
		// Initialize sums to 0 so we can increment it in the loop
		n_circ_circ = 0;
		n_square_paren_circ_circ = 0;
		n_paren_circ_circ = 0;
		n_paren_circ_square = 0;
		n_bracket_circ_square = 0;
    
		// Added by us for filling out tables (initialize)
		n_bracket_circ_circ = 0;
		n_paren_circ_paren_square_square = 0;
		n_bracket_circ_paren_square_square = 0;
    
		// Fetch the childrens "result counting data"
		quartResolvedAgree = g1.quartResolvedAgree + g2.quartResolvedAgree;
		quartResolvedAgreeDiag = g1.quartResolvedAgreeDiag + g2.quartResolvedAgreeDiag;
		quartResolvedAgreeUpper = g1.quartResolvedAgreeUpper + g2.quartResolvedAgreeUpper;
    
		// Initialize sums and stuff for calculating E
		n_circ_square_triangle = 0;
		n_paren_circ_square_triangle = g1.n_paren_circ_square_triangle + g2.n_paren_circ_square_triangle;
		n_0_circ_square = 0;
		n_paren_0_circ_square = g1.n_paren_0_circ_square + g2.n_paren_0_circ_square;
		n_bracket_circ_square_triangle = 0;
		n_bracket_0_circ_square = 0;
    
		// Fetch sum for calculating E from children
		quartSumE = g1.quartSumE + g2.quartSumE;
	///#endif
    
		while (g1Next != null || g2Next != null)
		{
			if (g2Next == null || (g1Next != null && g1Next.num < g2Next.num))
			{
				// Operate only on g1
				g1Count = g1Next;
				g2Count = CountingLinkedList.dummyLL;
				ourCount.num = g1Count.num;
    
				if (g1Next.type == CountingLinkedList.NodeType.End)
				{
					g1Next = null;
				}
				else
				{
					g1Next = g1Next.next;
				}
			}
			else if (g1Next == null || (g2Next != null && g2Next.num < g1Next.num))
			{
				// Operate only on g2
				g2Count = g2Next;
				g1Count = CountingLinkedList.dummyLL;
				ourCount.num = g2Count.num;
    
				if (g2Next.type == CountingLinkedList.NodeType.End)
				{
					g2Next = null;
				}
				else
				{
					g2Next = g2Next.next;
				}
			}
			else //g1Count->num == g2Count->num
			{
				g1Count = g1Next;
				g2Count = g2Next;
				ourCount.num = g1Count.num;
    
				if (g1Next.type == CountingLinkedList.NodeType.End)
				{
					g1Next = null;
				}
				else
				{
					g1Next = g1Next.next;
				}
				if (g2Next.type == CountingLinkedList.NodeType.End)
				{
					g2Next = null;
				}
				else
				{
					g2Next = g2Next.next;
				}
			}
    
			// Update counters (triplets)
			ourCount.n_i = g1Count.n_i + g2Count.n_i;
    
			if (ourCount.num == 0)
			{
				// Go to next one => We're done!
				if (g1Next == null && g2Next == null)
				{
					ourCount.type = CountingLinkedList.NodeType.End;
				}
				else
				{
					// Go to next one (there's more!)
					ourCount.type = CountingLinkedList.NodeType.Regular;
					if (ourCount.next == null)
					{
						ourCount.next = factory.getLL();
					}
					ourCount = ourCount.next;
					g1Count = g1Next;
					g2Count = g2Next;
				}
    
				continue;
			}
    
			ourCount.n_i_circ = g1Count.n_i_circ + g2Count.n_i_circ + g1Count.n_i * (g2.n_circ - g2Count.n_i) + g2Count.n_i * (g1.n_circ - g1Count.n_i);
    
			ourCount.n_paren_ii = g1Count.n_paren_ii + g2Count.n_paren_ii;
			//ourCount->n_i_arrow_circ = 0; // undefined actually!
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			// 2nd group in figure 12 (quartets only)
			ourCount.n_0_i = g1Count.n_0_i + g2Count.n_0_i + g1Zero * g2Count.n_i + g2Zero * g1Count.n_i;
    
			ourCount.n_ii = g1Count.n_ii + g2Count.n_ii + g1Count.n_i * g2Count.n_i; // and not the other way around too as we should then halve it :)
    
			ourCount.n_0_paren_ii = g1Count.n_0_paren_ii + g2Count.n_0_paren_ii + g1Zero * g2Count.n_paren_ii + g2Zero * g1Count.n_paren_ii;
    
			ourCount.n_circ_paren_ii = g1Count.n_circ_paren_ii + g2Count.n_circ_paren_ii + (g1.n_circ - g1Count.n_i) * g2Count.n_paren_ii + (g2.n_circ - g2Count.n_i) * g1Count.n_paren_ii;
    
			ourCount.n_i_paren_0_circ = g1Count.n_i_paren_0_circ + g2Count.n_i_paren_0_circ + g1Count.n_i * (g2.n_paren_0_circ - g2Count.n_paren_0_i) + g2Count.n_i * (g1.n_paren_0_circ - g1Count.n_paren_0_i);
    
			ourCount.n_i_paren_circ_circ = g1Count.n_i_paren_circ_circ + g2Count.n_i_paren_circ_circ + g1Count.n_i * (g2.n_paren_circ_circ - g2Count.n_paren_ii) + g2Count.n_i * (g1.n_paren_circ_circ - g1Count.n_paren_ii);
    
			ourCount.n_i_paren_circ_square = g1Count.n_i_paren_circ_square + g2Count.n_i_paren_circ_square + g1Count.n_i * (g2.n_paren_circ_square - g2Count.n_paren_i_circ) + g2Count.n_i * (g1.n_paren_circ_square - g1Count.n_paren_i_circ);
    
			// In the box
			ourCount.n_bracket_i_circ = ourCount.n_i * (n_circ - ourCount.n_i);
    
			// 3rd group in figure 12 (quartets only)
			ourCount.n_paren_0_i = g1Count.n_paren_0_i + g2Count.n_paren_0_i;
			ourCount.n_paren_i_circ = g1Count.n_paren_i_circ + g2Count.n_paren_i_circ;
			ourCount.n_paren_0_paren_ii = g1Count.n_paren_0_paren_ii + g2Count.n_paren_0_paren_ii;
			ourCount.n_paren_circ_paren_ii = g1Count.n_paren_circ_paren_ii + g2Count.n_paren_circ_paren_ii;
			ourCount.n_paren_i_paren_0_circ = g1Count.n_paren_i_paren_0_circ + g2Count.n_paren_i_paren_0_circ;
    
			// 4th group in figure 12 (quartets only)
			ourCount.n_bracket_0_paren_ii = g1Count.n_bracket_0_paren_ii + g2Count.n_bracket_0_paren_ii + g1Zero * g2Count.n_paren_ii + g2Zero * g1Count.n_paren_ii;
    
			ourCount.n_bracket_circ_paren_ii = g1Count.n_bracket_circ_paren_ii + g2Count.n_bracket_circ_paren_ii + (g1.n_circ - g1Count.n_i) * g2Count.n_paren_ii + (g2.n_circ - g2Count.n_i) * g1Count.n_paren_ii;
    
			ourCount.n_bracket_i_paren_0_circ = g1Count.n_bracket_i_paren_0_circ + g2Count.n_bracket_i_paren_0_circ + g1Count.n_i * (g2.n_paren_0_circ - g2Count.n_paren_0_i) + g2Count.n_i * (g1.n_paren_0_circ - g1Count.n_paren_0_i);
    
			// Figure 15 counters (part 1-4, all with j) & Figure 16 sums (with j)
			ourCount.resetIterator();
			g1Count.resetIterator();
			g2Count.resetIterator();
			g1.countingVars.resetIterator();
			g2.countingVars.resetIterator();
    
			// New counters for calculating E (Continues below)
			ourCount.n_i_circ_square = g1Count.n_i_circ_square + g2Count.n_i_circ_square + g1Count.n_i * (g2.n_circ_square - g2Count.n_i_circ) + g2Count.n_i * (g1.n_circ_square - g1Count.n_i_circ);
    
			ourCount.n_bracket_i_circ_square = g1Count.n_bracket_i_circ_square + g2Count.n_bracket_i_circ_square + g1Count.n_i * (g2.n_circ_square - g2Count.n_i_circ) + g2Count.n_i * (g1.n_circ_square - g1Count.n_i_circ);
    
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int wantedMax = degree+1;
			int wantedMax = degree+1;
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int lastJPlus1 = 1;
			int lastJPlus1 = 1;
			if (ourCount.n_i_j != null)
			{
				ourCount.n_i_j.type = CountingLinkedListNumOnly.NodeType.SkipAndEnd;
			}
    
			while (true)
			{
	//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	//ORIGINAL LINE: unsigned int j = wantedMax;
				int j = wantedMax;
				//NEXT_LEAST_J(g1Count.n_i_j) NEXT_LEAST_J(g2Count.n_i_j) 
                                if (gotoIteratorValueForList(g1.countingVars, lastJPlus1) && g1.countingVars.getIteratorNum() < j) j = g1.countingVars.getIteratorNum();
				if (gotoIteratorValueForList(g2.countingVars, lastJPlus1) && g2.countingVars.getIteratorNum() < j)
				{
					j = g2.countingVars.getIteratorNum();
				}
    
				if (j >= wantedMax)
					break;
    
				// n_i_j
				long new_n_i_j = getIteratorValueForNumList(g1Count.n_i_j, j) + getIteratorValueForNumList(g2Count.n_i_j, j) + g1Count.n_i * g2.countingVars.getIteratorValue(j).n_i + g2Count.n_i * g1.countingVars.getIteratorValue(j).n_i;
				addToNumList(ourCount, AddToType.i_j, j, new_n_i_j);
    
				if (ourCount.num != j)
				{
					// i != j
					long part1;
					long part2;
    
					// New counters for E (Continued from above)
					part1 = getIteratorValueForNumList(g1Count.n_i_j, j) * (g2.n_circ - g2Count.n_i - g2.countingVars.getIteratorValue(j).n_i);
					part2 = getIteratorValueForNumList(g2Count.n_i_j, j) * (g1.n_circ - g1Count.n_i - g1.countingVars.getIteratorValue(j).n_i);
					if (part1 < 0 || part2 < 0)
					{
						System.out.print("WTF #16?!?");
						System.out.print("\n");
					}
					else
					{
						ourCount.n_i_circ_square += part1 + part2;
						ourCount.n_bracket_i_circ_square += part1 + part2; // Yes, the same :)
					}
				}
    
				if (ourCount.num < j)
				{
					// j > i
					//
					// +------------------------------------------+
					// | New sums for calculating E (Added by us) |
					// +------------------------------------------+
					//
    
					// delta & delta
					quartSumE += (long) getIteratorValueForNumList(g1Count.n_i_j, j) * (g2.n_circ_square - g2Count.n_i_circ - g2.countingVars.getIteratorValue(j).n_i_circ + getIteratorValueForNumList(g2Count.n_i_j, j)); //ij - j_circ - ij
				}
    
				lastJPlus1 = j + 1;
			}
    
			// Added by us for filling out tables
			// A
			ourCount.n_paren_i_paren_circ_circ = g1Count.n_paren_i_paren_circ_circ + g2Count.n_paren_i_paren_circ_circ;
    
			ourCount.n_bracket_i_paren_circ_circ = g1Count.n_bracket_i_paren_circ_circ + g2Count.n_bracket_i_paren_circ_circ + g1Count.n_i * (g2.n_paren_circ_circ - g2Count.n_paren_ii) + g2Count.n_i * (g1.n_paren_circ_circ - g1Count.n_paren_ii);
    
			//if (ourCount->n_j_arrow_i != NULL) ourCount->n_j_arrow_i->type = CountingLinkedListNumOnly::SkipAndEnd;
    
			ourCount.n_paren_i_paren_circ_square = g1Count.n_paren_i_paren_circ_square + g2Count.n_paren_i_paren_circ_square;
    
			ourCount.n_bracket_i_paren_circ_square = g1Count.n_bracket_i_paren_circ_square + g2Count.n_bracket_i_paren_circ_square + g1Count.n_i * (g2.n_paren_circ_square - g2Count.n_paren_i_circ) + g2Count.n_i * (g1.n_paren_circ_square - g1Count.n_paren_i_circ);
    
			/*
			// Undefined :)
			ourCount->n_i_arrow_circ_arrow_circ = 0;
			ourCount->n_i_arrow_circ_arrow_square = 0;
			ourCount->n_paren_circ_square_arrow_i = 0;
			if (ourCount->n_i_arrow_j != NULL) ourCount->n_i_arrow_j->type = CountingLinkedListNumOnly::SkipAndEnd;
			*/
    
			// New counters for calculating E
			ourCount.n_paren_i_circ_square = g1Count.n_paren_i_circ_square + g2Count.n_paren_i_circ_square;
    
			ourCount.n_0_i_circ = g1Count.n_0_i_circ + g2Count.n_0_i_circ + g1Count.n_0_i * (g2.n_circ - g2Count.n_i) + g1Count.n_i * (g2.n_0_circ - g2Count.n_0_i) + g1Count.n_i_circ * g2Zero + g2Count.n_0_i * (g1.n_circ - g1Count.n_i) + g2Count.n_i * (g1.n_0_circ - g1Count.n_0_i) + g2Count.n_i_circ * g1Zero;
    
			ourCount.n_paren_0_i_circ = g1Count.n_paren_0_i_circ + g2Count.n_paren_0_i_circ;
    
			ourCount.n_bracket_0_i_circ = g1Count.n_bracket_0_i_circ + g2Count.n_bracket_0_i_circ + g1Count.n_0_i * (g2.n_circ - g2Count.n_i) + g1Count.n_i * (g2.n_0_circ - g2Count.n_0_i) + g1Count.n_i_circ * g2Zero + g2Count.n_0_i * (g1.n_circ - g1Count.n_i) + g2Count.n_i * (g1.n_0_circ - g1Count.n_0_i) + g2Count.n_i_circ * g1Zero;
	///#endif
    
			// Sums and stuff
			n_circ_square += ourCount.n_i_circ;
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			n_circ_circ += ourCount.n_ii;
			n_square_paren_circ_circ += ourCount.n_i_paren_circ_circ;
			n_paren_circ_circ += ourCount.n_paren_ii;
			n_paren_circ_square += ourCount.n_paren_i_circ;
			n_bracket_circ_square += ourCount.n_bracket_i_circ;
    
			// Added by us for filling out tables (initialize)
			n_bracket_circ_circ += Util.binom2(ourCount.n_i);
			n_paren_circ_paren_square_square += ourCount.n_paren_i_paren_circ_circ;
			n_bracket_circ_paren_square_square += ourCount.n_bracket_i_paren_circ_circ;
    
			// New sums and stuff for calculating E
			n_circ_square_triangle += ourCount.n_i_circ_square;
			n_0_circ_square += ourCount.n_0_i_circ;
    
			n_bracket_circ_square_triangle += ourCount.n_bracket_i_circ_square;
			n_bracket_0_circ_square += ourCount.n_bracket_0_i_circ;
	///#endif
    
			// Update resolved/resolved and unresolved/unresolved
			// (Figure 10)
			tripResolved += g1Count.n_paren_ii * (g2.n_circ - g2Count.n_i);
			tripResolved += g2Count.n_paren_ii * (g1.n_circ - g1Count.n_i);
    
			tripUnresolved += g1Count.n_i * (g2.n_circ_square - g2Count.n_i_circ);
			tripUnresolved += g2Count.n_i * (g1.n_circ_square - g1Count.n_i_circ);
    
			// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
			//quartResolvedAgree & quartResolvedDisagree
    
			// alpha & alpha
			quartResolvedAgreeDiag += (long) g1Count.n_paren_ii * (g2.n_paren_circ_circ - g2Count.n_paren_ii);
    
			// beta & alpha
			quartResolvedAgree += (long) g1Count.n_paren_ii * (g2.n_paren_circ_square - g2Count.n_paren_i_circ);
			quartResolvedAgree += (long) g2Count.n_paren_ii * (g1.n_paren_circ_square - g1Count.n_paren_i_circ);
    
			// beta & beta (part 1)
			quartResolvedAgreeDiag += (long) g1Count.n_paren_ii * (g2.n_circ_square - g2Count.n_i_circ);
			quartResolvedAgreeDiag += (long) g2Count.n_paren_ii * (g1.n_circ_square - g1Count.n_i_circ);
			quartResolvedAgreeDiag += (long) g1Count.n_i * (g2.n_square_paren_circ_circ - g2Count.n_i_paren_circ_circ - g2Count.n_circ_paren_ii);
			quartResolvedAgreeDiag += (long) g2Count.n_i * (g1.n_square_paren_circ_circ - g1Count.n_i_paren_circ_circ - g1Count.n_circ_paren_ii);
    
			// beta & beta (part 2)
			quartResolvedAgreeDiag += (long) g1Count.n_ii * (g2.n_paren_circ_square - g2Count.n_paren_i_circ);
			quartResolvedAgreeDiag += (long) g2Count.n_ii * (g1.n_paren_circ_square - g1Count.n_paren_i_circ);
			quartResolvedAgreeDiag += (long) g1Count.n_i * g2Count.n_i_paren_circ_square;
			quartResolvedAgreeDiag += (long) g2Count.n_i * g1Count.n_i_paren_circ_square;
    
			// gamma & alpha
			quartResolvedAgree += (long) g1Count.n_paren_ii * (g2.n_paren_0_circ - g2Count.n_paren_0_i);
			quartResolvedAgree += (long) g2Count.n_paren_ii * (g1.n_paren_0_circ - g1Count.n_paren_0_i);
    
			// gamma & beta (part 1) (continued below the loop!)
			quartResolvedAgree += (long) g1Count.n_paren_ii * (g2.n_0_circ - g2Count.n_0_i);
			quartResolvedAgree += (long) g2Count.n_paren_ii * (g1.n_0_circ - g1Count.n_0_i);
			quartResolvedAgree += (long)(g1.n_circ - g1Count.n_i) * g2Count.n_0_paren_ii;
			quartResolvedAgree += (long)(g2.n_circ - g2Count.n_i) * g1Count.n_0_paren_ii;
    
			// gamma & beta (part 2)
			quartResolvedAgree += (long) g1Count.n_ii * (g2.n_paren_0_circ - g2Count.n_paren_0_i);
			quartResolvedAgree += (long) g2Count.n_ii * (g1.n_paren_0_circ - g1Count.n_paren_0_i);
			quartResolvedAgree += (long) g1Count.n_i * g2Count.n_i_paren_0_circ;
			quartResolvedAgree += (long) g2Count.n_i * g1Count.n_i_paren_0_circ;
    
			// gamma & gamma (part 1)
			quartResolvedAgreeDiag += (long) g1Zero * g2Count.n_paren_circ_paren_ii;
			quartResolvedAgreeDiag += (long) g2Zero * g1Count.n_paren_circ_paren_ii;
    
			// gamma & gamma (part 2)
			quartResolvedAgreeDiag += (long)(g1.n_circ - g1Count.n_i) * g2Count.n_paren_0_paren_ii;
			quartResolvedAgreeDiag += (long)(g2.n_circ - g2Count.n_i) * g1Count.n_paren_0_paren_ii;
    
			// gamma & gamma (part 3)
			quartResolvedAgreeDiag += (long) g1Count.n_i * g2Count.n_paren_i_paren_0_circ;
			quartResolvedAgreeDiag += (long) g2Count.n_i * g1Count.n_paren_i_paren_0_circ;
    
    
			//
			// +------------------------------------------+
			// | New sums for calculating A (Added by us) |
			// +------------------------------------------+
			//
    
			// alpha & beta
			quartResolvedAgreeUpper += (long) g1Count.n_paren_ii * (g2.n_circ_circ - g2Count.n_ii);
			quartResolvedAgreeUpper += (long) g2Count.n_paren_ii * (g1.n_circ_circ - g1Count.n_ii);
			quartResolvedAgreeUpper += (long) g1Count.n_i_paren_circ_circ * g2Count.n_i;
			quartResolvedAgreeUpper += (long) g2Count.n_i_paren_circ_circ * g1Count.n_i;
    
			// alpha & gamma
			quartResolvedAgreeUpper += (long) g1Count.n_i * g2Count.n_paren_i_paren_circ_circ;
			quartResolvedAgreeUpper += (long) g2Count.n_i * g1Count.n_paren_i_paren_circ_circ;
    
			// beta & gamma (part 1)
			quartResolvedAgreeUpper += (long) g1Count.n_i * (g2.n_paren_circ_paren_square_square - g2Count.n_paren_i_paren_circ_circ - g2Count.n_paren_circ_paren_ii);
			quartResolvedAgreeUpper += (long) g2Count.n_i * (g1.n_paren_circ_paren_square_square - g1Count.n_paren_i_paren_circ_circ - g1Count.n_paren_circ_paren_ii);
    
			// beta & gamma (part 2)
			quartResolvedAgreeUpper += (long) g1Count.n_i * g2Count.n_paren_i_paren_circ_square;
			quartResolvedAgreeUpper += (long) g2Count.n_i * g1Count.n_paren_i_paren_circ_square;
    
			//
			// +------------------------------------------+
			// | New sums for calculating E (Added by us) |
			// +------------------------------------------+
			//
    
			// delta & delta
			quartSumE += (long) g1Count.n_i * (g2.n_circ_square_triangle - g2Count.n_i_circ_square);
			quartSumE += (long) g2Count.n_i * (g1.n_circ_square_triangle - g1Count.n_i_circ_square);
    
			// delta & epsilon
			quartSumE += (long)(g1.n_paren_circ_square_triangle - g1Count.n_paren_i_circ_square) * g2Count.n_i;
			quartSumE += (long)(g2.n_paren_circ_square_triangle - g2Count.n_paren_i_circ_square) * g1Count.n_i;
    
			// epsilon & delta (continues below)
			quartSumE += (long) g1Count.n_i * (g2.n_0_circ_square - g2Count.n_0_i_circ);
			quartSumE += (long) g2Count.n_i * (g1.n_0_circ_square - g1Count.n_0_i_circ);
    
			quartSumE += (long) g1Count.n_0_i * (g2.n_circ_square - g2Count.n_i_circ);
			quartSumE += (long) g2Count.n_0_i * (g1.n_circ_square - g1Count.n_i_circ);
    
			// epsilon & epsilon (part 1) (below)
    
			// epsilon & epsilon (part 2)
			quartSumE += (long)(g1.n_paren_0_circ_square - g1Count.n_paren_0_i_circ) * g2Count.n_i;
			quartSumE += (long)(g2.n_paren_0_circ_square - g2Count.n_paren_0_i_circ) * g1Count.n_i;
	///#endif
    
			// Go to next on children unless we're done
			if (g1Next == null && g2Next == null)
			{
				ourCount.type = CountingLinkedList.NodeType.End;
			}
			else
			{
				// Go to next one (there's more!)
				ourCount.type = CountingLinkedList.NodeType.Regular;
				if (ourCount.next == null)
				{
					ourCount.next = factory.getLL();
				}
				ourCount = ourCount.next;
			}
		}
    
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		//gamma & beta (part 1) (continued from inside the loop) (figure 13)
		quartResolvedAgree += (long) g1Zero * g2.n_square_paren_circ_circ;
		quartResolvedAgree += (long) g2Zero * g1.n_square_paren_circ_circ;
	///#endif
    
		n_circ_square /= 2;
    
		// Quartets
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#if quartetsToo
		n_paren_circ_square /= 2;
		n_bracket_circ_square /= 2;
    
		// New stuff for E
    
		// epsilon & delta (continues from inside the loop)
		quartSumE += (long) g1Zero * g2.n_circ_square_triangle;
		quartSumE += (long) g2Zero * g1.n_circ_square_triangle;
    
		// epsilon & epsilon (part 1)
		quartSumE += (long) g1.n_paren_circ_square_triangle * g2Zero;
		quartSumE += (long) g2.n_paren_circ_square_triangle * g1Zero;
    
		// Div E sum-counters :)
		if (n_circ_square_triangle % 3 != 0)
		{
			System.out.print("n_circ_square_triangle mod 3 error!");
			System.out.print("\n");
		}
		n_circ_square_triangle /= 3;
    
		if (n_0_circ_square % 2 != 0)
		{
			System.out.print("n_0_circ_square mod 2 error!");
			System.out.print("\n");
		}
		n_0_circ_square /= 2;
    
		if (n_bracket_circ_square_triangle % 3 != 0)
		{
			System.out.print("n_bracket_circ_square_triangle mod 3 check failed :(");
			System.out.print("\n");
		}
		n_bracket_circ_square_triangle /= 3;
    
		if (n_bracket_0_circ_square % 2 != 0)
		{
			System.out.print("n_bracket_0_circ_square mod 2 check failed :(");
			System.out.print("\n");
		}
		n_bracket_0_circ_square /= 2;
	///#endif
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
    private long tripResolved;
    private long tripUnresolved;

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
            //FRANK ADD TO RETURN NOTHING
            return null;
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
    public void addToNumList(CountingLinkedList parent, AddToType list, int num, long value)
	{
		if (value < 0)
		{
			System.out.print("WTF?!? Adding '");
			System.out.print(value);
			System.out.print("' for #");
			System.out.print(num);
			System.out.print("\n");
		}
    
		if (value <= 0)
			return;
    
		CountingLinkedListNumOnly theList = new CountingLinkedListNumOnly();
		boolean isReset = false;
    
		switch (list)
		{
			case i_j:
			{
                                //FRANK C OUT
				//INITIALIZE_PAREN_AND_SET_LIST(parent.n_i_j, parent.n_i_j_is_reset);
				break;
			}
			case j_arrow_i:
			{
                                //FRANK C OUT
				//INITIALIZE_PAREN_AND_SET_LIST(parent.n_j_arrow_i, parent.n_j_arrow_i_is_reset);
				break;
			}
			case i_arrow_j:
			{
                                //FRANK C OUT
				//INITIALIZE_PAREN_AND_SET_LIST(parent.n_i_arrow_j, parent.n_i_arrow_j_is_reset);
				break;
			}
			default:
				System.exit(-1);
		}
    
		if (!isReset)
		{
			// Go to the next one!
                            
			if (theList.iterator.next == null)
			{
				theList.iterator.next = factory.getLLNO();
			}
			theList.iterator.type = CountingLinkedListNumOnly.NodeType.Regular;
			theList = theList.iterator = theList.iterator.next;
		}
    
		theList.type = CountingLinkedListNumOnly.NodeType.End;
		theList.num = num;
		theList.value = value;
	}
    
    public boolean gotoIteratorValueForList(CountingLinkedList list, int num)
	{
		if (list == null || list.iteratorHasEnded())
		{
			return false;
		}
		list.getIteratorValue(num);
		return !list.iteratorHasEnded();
	}
}
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//ORIGINAL LINE: #define ADD_CHILD(PARENT, NEW_CHILD) { NEW_CHILD->childParent = PARENT; TemplatedLinkedList<HDT*> *newItem = factory->getTemplatedLinkedList(); newItem->data = NEW_CHILD; newItem->next = PARENT->children; PARENT->children = newItem; }

