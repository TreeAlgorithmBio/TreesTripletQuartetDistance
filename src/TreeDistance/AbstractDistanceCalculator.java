package TreeDistance;

package
public abstract class AbstractDistanceCalculator {
    protected HDTFactory dummyHDTFactory;
    protected RootedTree t1;
    protected RootedTree t2;
    protected HDT hdt;

    public void dispose() {
    }

    protected final void countChildren(RootedTree t) {
        if (t.isLeaf()) {
            t.n = 1;
            return;
        }

        int nSum = 0;
        for (TemplatedLinkedList<RootedTree> i = t.children; i != null; i = i.next) {
            RootedTree childI = i.data;
            countChildren(childI);
            nSum += childI.n;
        }
        t.n = nSum;
    }

    protected final void count(RootedTree v) {
        if (v.isLeaf() || v.n <= 2) {
            // This will make sure the entire subtree has color 0!
            v.colorSubtree(0);

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
            if (hdt.factory != null)
                hdt.factory.dispose();
///#endif

            return;
        }

        // v is not a leaf!
        // Find largest subtree
        TemplatedLinkedList<RootedTree> largest = v.children;
        int largestN = largest.data.n;
        TemplatedLinkedList<RootedTree> beforeLargest = null;
        TemplatedLinkedList<RootedTree> prev = v.children;
        for (TemplatedLinkedList<RootedTree> current = v.children.next; current != null; current = current.next) {
            if (current.data.n > largestN) {
                largest = current;
                beforeLargest = prev;
                largestN = largest.data.n;
            }
            prev = current;
        }
        if (beforeLargest != null) {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: beforeLargest->next = largest->next;
            beforeLargest.next.copyFrom(largest.next);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: largest->next = v->children;
            largest.next.copyFrom(v.children);
            v.children = largest;
        }

        // Color i'th subtree (i > 1) with color i
        int c = 2;
        for (TemplatedLinkedList<RootedTree> current = v.children.next; current != null; current = current.next) {
            current.data.colorSubtree(c);
            c++;
        }

        // Update counters in the HDT
        hdt.updateCounters();
        updateCounters();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
        // Extract
        RootedTree[] extractedVersions = new RootedTree[v.numChildren - 1];
        c = 0;
        for (TemplatedLinkedList<RootedTree> current = v.children.next; current != null; current = current.next) {
            if (current.data.isLeaf() || current.data.n <= 2) {
                extractedVersions[c] = null;
            } else {
                current.data.markHDTAlternative();
                RootedTree extractedT2 = hdt.extractAndGoBack(t1.factory);
                extractedVersions[c] = extractedT2.contract();
                if (extractedT2.factory != null)
                    extractedT2.factory.dispose();
            }
            c++; // Weee :)
        }
///#endif

        // Color to 0
        for (TemplatedLinkedList<RootedTree> current = v.children.next; current != null; current = current.next) {
            current.data.colorSubtree(0);
        }

        // Contract and recurse on 1st child
        RootedTree firstChild = v.children.data;
        if (firstChild.isLeaf() || firstChild.n <= 2) {
            // Do "nothing" (except clean up and possibly color!)
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
            // Notice no recoloring here... It's not neccesary as it is extracted and contracted away,
            // and will actually cause an error if called with firstChild->colorSubtree(0) as t2 is linked
            // to a non-existing hdt (as we just deleted it) (we could wait with deleting it, but as we don't need the coloring why bother)
            if (hdt.factory != null)
                hdt.factory.dispose();
///#else
            firstChild.colorSubtree(0);
///#endif
        } else {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
            boolean hdtTooBig = firstChild.n * DefineConstants.CONTRACT_MAX_EXTRA_SIZE < hdt.leafCount();
            if (hdtTooBig) {
                HDT newHDT;

                firstChild.markHDTAlternative();
                RootedTree extractedT2 = hdt.extractAndGoBack(t1.factory);
                RootedTree contractedT2 = extractedT2.contract();
                if (extractedT2.factory != null)
                    extractedT2.factory.dispose();
                newHDT = HDT.constructHDT(contractedT2, t1.maxDegree, dummyHDTFactory, true);
                if (contractedT2.factory != null)
                    contractedT2.factory.dispose();
                if (hdt.factory != null)
                    hdt.factory.dispose();
                hdt = newHDT;
            }
///#endif
            count(firstChild);
            // HDT is deleted in recursive call!
        }

        // Color 1 and recurse
        c = 0;
        for (TemplatedLinkedList<RootedTree> current = v.children.next; current != null; current = current.next) {
            if (!current.data.isLeaf() && current.data.n > 2) {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: hdt = HDT::constructHDT(extractedVersions[c], t1->maxDegree, dummyHDTFactory, true);
                hdt = HDT.constructHDT(new RootedTree(extractedVersions[c]), t1.maxDegree, dummyHDTFactory, true);
                if (extractedVersions[c].factory != null)
                    extractedVersions[c].factory.dispose();
///#endif

                current.data.colorSubtree(1);

                count(current.data);
            }
            c++; // Weee :)
            // HDT is deleted on recursive calls!
        }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#if doExtractAndContract
        extractedVersions = null;
///#endif
    }

    protected abstract void updateCounters();
}