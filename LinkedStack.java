package CW_NDQ;

public class LinkedStack<T> {
    private Node<T> top;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public LinkedStack() {
        top = null;
        size = 0;
    }

    public void push(T data) {
        Node<T> node = new Node<>(data);
        node.next = top;
        top = node;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }


    public boolean isEmpty() {
        return size == 0;
    }
} 