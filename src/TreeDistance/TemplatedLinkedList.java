package TreeDistance;

public class TemplatedLinkedList<dataType> {

    public dataType data;
    public TemplatedLinkedList<dataType> next;

    public TemplatedLinkedList() {
        data = new dataType();
    }

    public final void initialize() {
        next = null;
    }
}