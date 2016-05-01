package TreeDistance;

public class CountingLinkedList {
    public static CountingLinkedList dummyLL = new CountingLinkedList();
    // For triplets
    public INTTYPE_REST n_i = new INTTYPE_REST();
    public INTTYPE_REST n_i_circ = new INTTYPE_REST();
    public INTTYPE_REST n_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ = new INTTYPE_REST();
    // For quartets
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
    public INTTYPE_REST n_0_i = new INTTYPE_REST();
    public INTTYPE_REST n_ii = new INTTYPE_REST();
    public INTTYPE_REST n_0_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_circ_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_i_paren_0_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_paren_circ_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_paren_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_i_circ = new INTTYPE_REST();
    public INTTYPE_REST n_paren_0_i = new INTTYPE_REST();
    public INTTYPE_REST n_paren_i_circ = new INTTYPE_REST();
    public INTTYPE_REST n_paren_0_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_paren_circ_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_paren_i_paren_0_circ = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_0_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_circ_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_i_paren_0_circ = new INTTYPE_REST();
    public INTTYPE_REST n_0_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_0 = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_circ_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_0_arrow_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_paren_0_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_paren_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_circ_arrow_paren_ii = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_0_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_circ_arrow_ii = new INTTYPE_REST();
    public INTTYPE_REST n_paren_ii_arrow_0 = new INTTYPE_REST();
    public INTTYPE_REST n_paren_ii_arrow_circ = new INTTYPE_REST();
    public INTTYPE_REST n_paren_circ_circ_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_0_arrow_i_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ_arrow_0 = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_0_arrow_circ = new INTTYPE_REST();
    public INTTYPE_REST n_circ_arrow_i_arrow_i = new INTTYPE_REST();
    // Added by us
    public INTTYPE_REST n_i_arrow_paren_circ_circ = new INTTYPE_REST();
    public INTTYPE_REST n_0_arrow_ii = new INTTYPE_REST();
    public INTTYPE_REST n_paren_0_circ_arrow_i = new INTTYPE_REST();
    // Figure 15 counters (part 1)
    public CountingLinkedListNumOnly n_i_j; // also used for E calculation
    public INTTYPE_REST n_0_arrow_i_circ = new INTTYPE_REST(); // also used for E calculation
    // Added by us for filling out tables
    // A
    public INTTYPE_REST n_paren_i_paren_circ_circ = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_i_paren_circ_circ = new INTTYPE_REST();
    public CountingLinkedListNumOnly n_j_arrow_i;
    public INTTYPE_REST n_paren_i_paren_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_i_paren_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ_arrow_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_arrow_circ_arrow_square = new INTTYPE_REST();
    public INTTYPE_REST n_paren_circ_square_arrow_i = new INTTYPE_REST();
    public CountingLinkedListNumOnly n_i_arrow_j;
    // New counters for calculating E
    public INTTYPE_REST n_i_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_i_circ_arrow_square = new INTTYPE_REST();
    public INTTYPE_REST n_circ_square_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_paren_i_circ_square = new INTTYPE_REST();
    public INTTYPE_REST n_0_i_circ = new INTTYPE_REST();
    public INTTYPE_REST n_i_circ_arrow_0 = new INTTYPE_REST();
    public INTTYPE_REST n_0_i_arrow_circ = new INTTYPE_REST();
    public INTTYPE_REST n_0_circ_arrow_i = new INTTYPE_REST();
    public INTTYPE_REST n_paren_0_i_circ = new INTTYPE_REST();
    public INTTYPE_REST n_bracket_i_circ_square = new INTTYPE_REST();
///#endif
    public INTTYPE_REST n_bracket_0_i_circ = new INTTYPE_REST();
    // More general stuff
//C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: unsigned int num;
    public int num;
    public NodeType type;
    public CountingLinkedList next;
    public CountingLinkedList iterator;
    public boolean n_i_j_is_reset;
    public boolean n_j_arrow_i_is_reset;
    public boolean n_i_arrow_j_is_reset;
    public CountingLinkedList() {
    }

    public CountingLinkedList(boolean dummy) {
        initialize();

        // Triplets
        n_i = 0;
        n_i_circ = 0;
        n_paren_ii = 0;
        n_i_arrow_circ = 0;

        // Quartets
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
        n_0_i = 0;
        n_ii = 0;
        n_0_paren_ii = 0;
        n_circ_paren_ii = 0;
        n_i_paren_0_circ = 0;
        n_i_paren_circ_circ = 0;
        n_i_paren_circ_square = 0;

        n_bracket_i_circ = 0;

        n_paren_0_i = 0;
        n_paren_i_circ = 0;
        n_paren_0_paren_ii = 0;
        n_paren_circ_paren_ii = 0;
        n_paren_i_paren_0_circ = 0;

        n_bracket_0_paren_ii = 0;
        n_bracket_circ_paren_ii = 0;
        n_bracket_i_paren_0_circ = 0;

        n_0_arrow_i = 0;
        n_i_arrow_0 = 0;
        n_i_arrow_i = 0;
        n_circ_arrow_i = 0;
        n_0_arrow_paren_ii = 0;
        n_i_arrow_paren_0_circ = 0;
        n_i_arrow_paren_circ_square = 0;
        n_circ_arrow_paren_ii = 0;
        n_i_arrow_0_circ = 0;
        n_i_arrow_circ_circ = 0;
        n_i_arrow_circ_square = 0;
        n_circ_arrow_ii = 0;
        n_paren_ii_arrow_0 = 0;
        n_paren_ii_arrow_circ = 0;
        n_paren_circ_circ_arrow_i = 0;
        n_0_arrow_i_arrow_i = 0;
        n_i_arrow_circ_arrow_0 = 0;
        n_i_arrow_0_arrow_circ = 0;
        n_circ_arrow_i_arrow_i = 0;

        n_i_arrow_paren_circ_circ = 0;
        n_0_arrow_ii = 0;

        n_0_arrow_i_circ = 0;

        // Added by us for filling out tables
        // A
        n_paren_i_paren_circ_circ = 0;
        n_bracket_i_paren_circ_circ = 0;
        n_paren_i_paren_circ_square = 0;
        n_bracket_i_paren_circ_square = 0;
        n_i_arrow_circ_arrow_circ = 0;
        n_i_arrow_circ_arrow_square = 0;
        n_paren_circ_square_arrow_i = 0;

        // New counters for calculating E
        n_i_circ_square = 0;
        n_i_circ_arrow_square = 0;
        n_circ_square_arrow_i = 0;
        n_paren_i_circ_square = 0;
        n_0_i_circ = 0;
        n_i_circ_arrow_0 = 0;
        n_0_i_arrow_circ = 0;
        n_0_circ_arrow_i = 0;
        n_paren_0_i_circ = 0;
        n_bracket_i_circ_square = 0;
        n_bracket_0_i_circ = 0;
///#endif
    }

    public final void initialize() {
        next = null;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
        n_i_j = null;
        n_j_arrow_i = n_i_arrow_j = null;
///#endif
    }

    //C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if quartetsToo
    public final void resetIterator() {
        iterator = this;
        if (n_i_j != null) {
            n_i_j.resetIterator();
        }
        n_i_j_is_reset = true;
        if (n_j_arrow_i != null) {
            n_j_arrow_i.resetIterator();
        }
        n_j_arrow_i_is_reset = true;
        if (n_i_arrow_j != null) {
            n_i_arrow_j.resetIterator();
        }
        n_i_arrow_j_is_reset = true;
    }

    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: unsigned int getIteratorNum()
    public final int getIteratorNum() {
        return iterator.num;
    }

    public final boolean iteratorHasEnded() {
        return iterator == null;
    }

    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: CountingLinkedList* getIteratorValue(unsigned int j)
    public final CountingLinkedList getIteratorValue(int j) {
        while (!iteratorHasEnded() && iterator.num < j) {
            if (iterator.type == NodeType.End) {
                iterator = null;
            } else {
                iterator = iterator.next;
            }
        }
        if (iteratorHasEnded() || iterator.num > j) {
            return dummyLL;
        }
            /*iterator->num == j*/
        return iterator;
    }

    public final CountingLinkedList getCurrentIteratorValueAndIncrement() {
        if (iteratorHasEnded()) {
            return null;
        }
        CountingLinkedList result = iterator;
        if (iterator.type == NodeType.End) {
            iterator = null;
        } else {
            iterator = iterator.next;
        }
        return result;
    }

    public enum NodeType {
        Regular,
        End,
        Dummy;

        public static NodeType forValue(int value) {
            return values()[value];
        }

        public int getValue() {
            return this.ordinal();
        }
    }
///#endif
}