package CW_NDQ;

public class LinkedQueue<T> implements AbstractLinkedQueue<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedQueue(){

    }

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    @Override
    public void offer(T data) {
        Node<T> node = new Node<>(data);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void enqueue(T data) {
        offer(data);
    }

    @Override
    public T poll() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return data;
    }

    public T dequeue() {
        return poll();
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
