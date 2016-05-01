package TreeDistance;

public class RootedTreeFactory {
    private RootedTree createdRT;
    private RootedTree currentRT;
    private TemplatedLinkedList<RootedTree> createdTLL;
    private TemplatedLinkedList<RootedTree> currentTLL;
    private int currentLocationRT;
    private int currentLocationTLL;
    private int size;
    private MemoryAllocator<RootedTree> memRT;
    private MemoryAllocator<TemplatedLinkedList<RootedTree>> memTLL;

    public RootedTreeFactory(RootedTreeFactory copyMemAllocFrom) {
        this.size = 30;

        if (copyMemAllocFrom == null) {
            memRT = new MemoryAllocator<RootedTree>(size + 1);
            memTLL = new MemoryAllocator<TemplatedLinkedList<RootedTree * >> (size + 1);
        } else {
            memRT = copyMemAllocFrom.memRT;
            memTLL = copyMemAllocFrom.memTLL;
        }
        memRT.numUses++;
        memTLL.numUses++;

        createdRT = memRT.getMemory();
        createdRT.altWorldSelf = null;
        currentRT = createdRT;
        currentLocationRT = 1;
        createdTLL = memTLL.getMemory();
        createdTLL.initialize();
        currentTLL = createdTLL;
        currentLocationTLL = 1;
    }

    public void dispose() {
        {
            RootedTree current = createdRT;
            while (current != null) {
                RootedTree next = current.altWorldSelf;
                memRT.releaseMemory(current);
                current = next;
            }
        }
        {
            TemplatedLinkedList<RootedTree> current = createdTLL;
            while (current != null) {
                TemplatedLinkedList<RootedTree> next = current.next;
                memTLL.releaseMemory(current);
                current = next;
            }
        }

        memRT.numUses--;
        if (memRT.numUses == 0) {
            memRT = null;
        }
        memTLL.numUses--;
        if (memTLL.numUses == 0) {
            memTLL = null;
        }
    }

    public RootedTree getRootedTree(String name) {
        if (currentLocationRT > size) {
            currentRT.altWorldSelf = memRT.getMemory();
            currentRT = currentRT.altWorldSelf;
            currentRT.altWorldSelf = null;
            currentLocationRT = 1;
        }

        RootedTree returnMe = currentRT[currentLocationRT];
        returnMe.initialize(name);
        returnMe.factory = this;
        currentLocationRT++;
        return returnMe;
    }

    public TemplatedLinkedList<RootedTree> getTemplatedLinkedList() {
        if (currentLocationTLL > size) {
            currentTLL.next = memTLL.getMemory();
            currentTLL = currentTLL.next;
            currentTLL.initialize();
            currentLocationTLL = 1;
        }

        TemplatedLinkedList<RootedTree> returnMe = currentTLL[currentLocationTLL];
        returnMe.initialize();
        currentLocationTLL++;
        return returnMe;
    }

    public long getSizeInRam() {
        long resultRT = 0;
        {
            RootedTree current = createdRT;
            while (current != null) {
                resultRT++;
                current = current.altWorldSelf;
            }
        }

        long resultTLL = 0;
        {
            TemplatedLinkedList<RootedTree> current = createdTLL;
            while (current != null) {
                resultTLL++;
                current = current.next;
            }
        }

//C++ TO JAVA CONVERTER TODO TASK: There is no Java equivalent to 'sizeof':
        return resultRT * (size + 1) * sizeof(RootedTree) + resultTLL * (size + 1) * sizeof(TemplatedLinkedList < RootedTree * >);
    }
}