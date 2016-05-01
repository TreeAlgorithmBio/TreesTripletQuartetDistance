package TreeDistance;

public class HDT {
    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public boolean gotoIteratorValueForList(CountingLinkedList list, int num) {
        if (list == null || list.iteratorHasEnded()) {
            return false;
        }
        list.getIteratorValue(num);
        return !list.iteratorHasEnded();
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public INTTYPE_REST getIteratorValueForNumList(CountingLinkedListNumOnly list, int num) {
        if (list == null) {
            return 0;
        }
        return list.getIteratorValue(num);
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public boolean gotoIteratorValueForNumList(CountingLinkedListNumOnly list, int num) {
        if (list == null || list.iteratorHasEnded()) {
            return false;
        }
        list.getIteratorValue(num);
        return !list.iteratorHasEnded();
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public boolean hasIteratorForNumListEnded(CountingLinkedListNumOnly list) {
        return list == null || list.iteratorHasEnded();
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public void addToNumList(CountingLinkedList parent, AddToType list, int num, INTTYPE_REST value) {
        if (value < 0) {
            System.out.print("WTF?!? Adding '");
            System.out.print(value);
            System.out.print("' for #");
            System.out.print(num);
            System.out.print("\n");
        }

        if (value <= 0)
            return;

        CountingLinkedListNumOnly theList;
        boolean isReset = false;

        switch (list) {
            case i_j: {
                {
                    if (parent.n_i_j == null) {
                        parent.n_i_j = factory.getLLNO();
                        parent.n_i_j.resetIterator();
                        isReset = true;
                    } else {
                        isReset = parent.n_i_j_is_reset;
                    }
                    theList = parent.n_i_j;
                    parent.n_i_j_is_reset = false;
                }
                ;
                break;
            }
            case j_arrow_i: {
                {
                    if (parent.n_j_arrow_i == null) {
                        parent.n_j_arrow_i = factory.getLLNO();
                        parent.n_j_arrow_i.resetIterator();
                        isReset = true;
                    } else {
                        isReset = parent.n_j_arrow_i_is_reset;
                    }
                    theList = parent.n_j_arrow_i;
                    parent.n_j_arrow_i_is_reset = false;
                }
                ;
                break;
            }
            case i_arrow_j: {
                {
                    if (parent.n_i_arrow_j == null) {
                        parent.n_i_arrow_j = factory.getLLNO();
                        parent.n_i_arrow_j.resetIterator();
                        isReset = true;
                    } else {
                        isReset = parent.n_i_arrow_j_is_reset;
                    }
                    theList = parent.n_i_arrow_j;
                    parent.n_i_arrow_j_is_reset = false;
                }
                ;
                break;
            }
            default:
                System.exit(-1);
        }

        if (!isReset) {
            // Go to the next one!
            if (theList.iterator.next == null) {
                theList.iterator.next = factory.getLLNO();
            }
            theList.iterator.type = CountingLinkedListNumOnly.NodeType.Regular;
            theList = theList.iterator = theList.iterator.next;
        }

        theList.type = CountingLinkedListNumOnly.NodeType.End;
        theList.num = num;
        theList.value = value;
    }

    public static HDT constructHDT(RootedTree t2, int maxDegree, HDTFactory dummyHDTFactory) {

    }
}