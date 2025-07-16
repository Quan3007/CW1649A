package CW_NDQ;

public interface AbstractLinkedQueue<E> {
    void offer(E element);
    E poll();
    E peek();
    int size();
    boolean isEmpty();
}
