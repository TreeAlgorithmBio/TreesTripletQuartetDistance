package TreeDistance;

public class CountingLinkedListNumOnly {
    public INTTYPE_REST value = new INTTYPE_REST();
    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: unsigned int num;
    public int num;
    public NodeType type;
    public CountingLinkedListNumOnly next;
    public CountingLinkedListNumOnly iterator;

    public final void initialize() {
        next = null;
    }

    public final void resetIterator() {
        iterator = this;
    }

    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: unsigned int getIteratorNum()
    public final int getIteratorNum() {
        return iterator.num;
    }

    public final boolean iteratorHasEnded() {
        return iterator == null || iterator.type == NodeType.SkipAndEnd;
    }

    //C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: INTTYPE_REST getIteratorValue(unsigned int j)
    public final INTTYPE_REST getIteratorValue(int j) {
        while (!iteratorHasEnded() && iterator.num < j) {
            if (iterator.type == NodeType.End) {
                iterator = null;
            } else {
                iterator = iterator.next;
            }
        }
        if (iteratorHasEnded() || iterator.num > j) {
            return 0;
        }
            /*iterator->num == j*/
        return iterator.value;
    }

    public enum NodeType {
        Regular,
        End,
        SkipAndEnd;

        public static NodeType forValue(int value) {
            return values()[value];
        }

        public int getValue() {
            return this.ordinal();
        }
    }
}