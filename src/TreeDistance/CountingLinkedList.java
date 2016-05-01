package TreeDistance;

public class CountingLinkedList {
    public static CountingLinkedList dummyLL = new CountingLinkedList();

    // For triplets
    public long n_i;
    public long n_i_circ;
    public long n_paren_ii;
    public long n_i_arrow_circ;

    // For quartets

    public long n_0_i;
    public long n_ii;
    public long n_0_paren_ii;
    public long n_circ_paren_ii;
    public long n_i_paren_0_circ;
    public long n_i_paren_circ_circ;
    public long n_i_paren_circ_square;
    public long n_bracket_i_circ;
    public long n_paren_0_i;
    public long n_paren_i_circ;
    public long n_paren_0_paren_ii;
    public long n_paren_circ_paren_ii;
    public long n_paren_i_paren_0_circ;
    public long n_bracket_0_paren_ii;
    public long n_bracket_circ_paren_ii;
    public long n_bracket_i_paren_0_circ;
    public long n_0_arrow_i;
    public long n_i_arrow_0;
    public long n_i_arrow_i;
    public long n_circ_arrow_i;
    public long n_0_arrow_paren_ii;
    public long n_i_arrow_paren_0_circ;
    public long n_i_arrow_paren_circ_square;
    public long n_circ_arrow_paren_ii;
    public long n_i_arrow_0_circ;
    public long n_i_arrow_circ_circ;
    public long n_i_arrow_circ_square;
    public long n_circ_arrow_ii;
    public long n_paren_ii_arrow_0;
    public long n_paren_ii_arrow_circ;
    public long n_paren_circ_circ_arrow_i;
    public long n_0_arrow_i_arrow_i;
    public long n_i_arrow_circ_arrow_0;
    public long n_i_arrow_0_arrow_circ;
    public long n_circ_arrow_i_arrow_i;

    // Added by us
    public long n_i_arrow_paren_circ_circ;
    public long n_0_arrow_ii;
    public long n_paren_0_circ_arrow_i;

    // Figure 15 counters (part 1)
    public CountingLinkedListNumOnly n_i_j; // also used for E calculation
    public long n_0_arrow_i_circ; // also used for E calculation
    // Added by us for filling out tables
    // A
    public long n_paren_i_paren_circ_circ;
    public long n_bracket_i_paren_circ_circ;
    public CountingLinkedListNumOnly n_j_arrow_i;
    public long n_paren_i_paren_circ_square;
    public long n_bracket_i_paren_circ_square;
    public long n_i_arrow_circ_arrow_circ;
    public long n_i_arrow_circ_arrow_square;
    public long n_paren_circ_square_arrow_i;
    public CountingLinkedListNumOnly n_i_arrow_j;
    // New counters for calculating E
    public long n_i_circ_square;
    public long n_i_circ_arrow_square;
    public long n_circ_square_arrow_i;
    public long n_paren_i_circ_square;
    public long n_0_i_circ;
    public long n_i_circ_arrow_0;
    public long n_0_i_arrow_circ;
    public long n_0_circ_arrow_i;
    public long n_paren_0_i_circ;
    public long n_bracket_i_circ_square;

    public long n_bracket_0_i_circ;
    // More general stuff

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

    }

    public final void initialize() {
        next = null;

        n_i_j = null;
        n_j_arrow_i = n_i_arrow_j = null;

    }

    
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

    
    public final int getIteratorNum() {
        return iterator.num;
    }

    public final boolean iteratorHasEnded() {
        return iterator == null;
    }

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

}