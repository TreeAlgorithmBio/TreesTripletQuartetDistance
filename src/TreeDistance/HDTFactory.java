package TreeDistance;

public class HDTFactory {

    public HDTFactory(int numD, HDTFactory copyMemAllocFrom) {
        this.numD = numD;

        if (copyMemAllocFrom == null) {
            memHDT = new MemoryAllocator<HDT>(DefineConstants.HDTFactorySize + 1);
            memCLL = new MemoryAllocator<CountingLinkedList>(DefineConstants.HDTFactorySize + 1);
            memCLLNO = new MemoryAllocator<CountingLinkedListNumOnly>(DefineConstants.HDTFactorySize + 1);
            memTLL = new MemoryAllocator<TemplatedLinkedList<HDT * >> (DefineConstants.HDTFactorySize + 1);
        } else {
            memHDT = copyMemAllocFrom.memHDT;
            memCLL = copyMemAllocFrom.memCLL;
            memCLLNO = copyMemAllocFrom.memCLLNO;
            memTLL = copyMemAllocFrom.memTLL;
        }
        memHDT.numUses++;
        memCLL.numUses++;
        memCLLNO.numUses++;
        memTLL.numUses++;

        createdHDTs = memHDT.getMemory();
        createdHDTs.left = null;
        currentHDT = createdHDTs;
        hdtLocation = 1;

        createdLL = memCLL.getMemory();
        createdLL.initialize();
        currentLL = createdLL;
        llLocation = 1;

        createdLLNO = memCLLNO.getMemory();
        createdLLNO.initialize();
        currentLLNO = createdLLNO;
        llnoLocation = 1;

        createdTLL = memTLL.getMemory();
        createdTLL.initialize();
        currentTLL = createdTLL;
        currentLocationTLL = 1;
    }

    public HDTFactory() {

    }

    public void dispose() {
        {
            HDT current = createdHDTs;
            while (current != null) {
                HDT next = current.left;
                memHDT.releaseMemory(current);
                current = next;
            }
        }

        {
            CountingLinkedList current = createdLL;
            while (current != null) {
                CountingLinkedList next = current.next;
                memCLL.releaseMemory(current);
                current = next;
            }
        }

        {
            CountingLinkedListNumOnly current = createdLLNO;
            while (current != null) {
                CountingLinkedListNumOnly next = current.next;
                memCLLNO.releaseMemory(current);
                current = next;
            }
        }

        {
            TemplatedLinkedList<HDT> current = createdTLL;
            while (current != null) {
                TemplatedLinkedList<HDT> next = current.next;
                memTLL.releaseMemory(current);
                current = next;
            }
        }

        memHDT.numUses--;
        if (memHDT.numUses == 0) {
            memHDT = null;
        }
        memCLL.numUses--;
        if (memCLL.numUses == 0) {
            memCLL = null;
        }
        memCLLNO.numUses--;
        if (memCLLNO.numUses == 0) {
            memCLLNO = null;
        }
        memTLL.numUses--;
        if (memTLL.numUses == 0) {
            memTLL = null;
        }
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public void deleteTemplatedLinkedList() {
        TemplatedLinkedList<HDT> current = createdTLL;
        while (current != null) {
            TemplatedLinkedList<HDT> next = current.next;
            memTLL.releaseMemory(current);
            current = next;
        }
        createdTLL = currentTLL = null;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public HDT getHDT(HDT.NodeType type, RootedTree link, boolean doLink) {
        if (hdtLocation > DefineConstants.HDTFactorySize) {
            currentHDT.left = memHDT.getMemory();
            currentHDT = currentHDT.left;
            currentHDT.left = null;
            hdtLocation = 1;
        }

        HDT returnMe = currentHDT[hdtLocation];
        returnMe.initialize(getLL(), type, numD, link, doLink);
        returnMe.factory = this;
        hdtLocation++;
        return returnMe;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public CountingLinkedList getLL() {
        if (llLocation > DefineConstants.HDTFactorySize) {
            currentLL.next = memCLL.getMemory();
            currentLL = currentLL.next;
            currentLL.initialize();
            llLocation = 1;
        }

        CountingLinkedList returnMe = currentLL[llLocation];
        returnMe.initialize();
        llLocation++;
        return returnMe;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public CountingLinkedListNumOnly getLLNO() {
        if (llnoLocation > DefineConstants.HDTFactorySize) {
            currentLLNO.next = memCLLNO.getMemory();
            currentLLNO = currentLLNO.next;
            currentLLNO.initialize();
            llnoLocation = 1;
        }

        CountingLinkedListNumOnly returnMe = currentLLNO[llnoLocation];
        returnMe.initialize();
        llnoLocation++;
        return returnMe;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public TemplatedLinkedList<HDT> getTemplatedLinkedList() {
        if (currentLocationTLL > DefineConstants.HDTFactorySize) {
            currentTLL.next = memTLL.getMemory();
            currentTLL = currentTLL.next;
            currentTLL.initialize();
            currentLocationTLL = 1;
        }

        TemplatedLinkedList<HDT> returnMe = currentTLL[currentLocationTLL];
        returnMe.initialize();
        currentLocationTLL++;
        return returnMe;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public long getSizeInRam() {
        long resultHDT = 0;
        {
            HDT current = createdHDTs;
            while (current != null) {
                resultHDT++;
                current = current.left;
            }
        }

        long resultLL = 0;
        {
            CountingLinkedList current = createdLL;
            while (current != null) {
                resultLL++;
                current = current.next;
            }
        }

        long resultLLNO = 0;
        {
            CountingLinkedListNumOnly current = createdLLNO;
            while (current != null) {
                resultLLNO++;
                current = current.next;
            }
        }

        long resultTLL = 0;
        {
            TemplatedLinkedList<HDT> current = createdTLL;
            while (current != null) {
                resultTLL++;
                current = current.next;
            }
        }

//C++ TO JAVA CONVERTER TODO TASK: There is no Java equivalent to 'sizeof':
        return resultHDT * (DefineConstants.HDTFactorySize + 1) * sizeof(HDT) + resultLL * (DefineConstants.HDTFactorySize + 1) * sizeof(CountingLinkedList) + resultLLNO * (DefineConstants.HDTFactorySize + 1) * sizeof(CountingLinkedListNumOnly) + resultTLL * (DefineConstants.HDTFactorySize + 1) * sizeof(TemplatedLinkedList < HDT * >);
    }
}