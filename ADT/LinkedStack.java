package CW_NDQ.ADT;

public class LinkedStack<E> {
    private Node<E> top;
    private int size;

    private static class Node<E>{
        // attributes
        private E element;
        private Node<E> next;

        // constructor
        public Node ( E element ) {
            this.element = element;
            this.next = null;
        }
    }

    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    public void push ( E element ) {
        Node<E> newNode = new Node<>(element);

        // if the stack is empty
        if (top == null) {
            this.top = newNode;
        } else { // if the stack is not empty
            newNode.next = this.top;
            this.top = newNode;
        }

        this.size++;
    }

    public E pop () {
        // if the stack is empty
        if (this.top == null) {
            throw new IllegalStateException("Stack is empty");
        }

        // if the stack is not empty
        E oldElement = this.top.element;

        Node<E> tempNode = this.top;
        this.top = this.top.next;
        tempNode.next = null;

        this.size--;
        return oldElement;
    }


    public boolean isEmpty () {
        if (this.top == null && this.size == 0) {
            return true;
        }
        return false;
    }
}