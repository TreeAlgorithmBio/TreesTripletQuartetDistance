package TreeDistance;

public class TemplatedLinkedList<dataType> {

    public dataType data;
    public TemplatedLinkedList<dataType> next;


    public final void initialize() {
        next = null;
    }
}