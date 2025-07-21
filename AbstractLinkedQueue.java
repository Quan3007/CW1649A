package CW_NDQ;

public interface AbstractLinkedQueue<E> {
    void offer(E element);
    E poll();
    int size();
    boolean isEmpty();
}
