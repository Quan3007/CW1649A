package CW_NDQ.ADT;

import CW_NDQ.AbstractLinkedQueue;

public class LinkedQueue<E> implements AbstractLinkedQueue<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedQueue(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

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


    @Override
    public void offer ( E element ) {
        Node<E> newNode = new Node<>(element);

        // if the queue is empty
        if (head == null && tail == null) {
            this.head = newNode;
            this.tail = newNode;
        } else { // if the queue is not empty
            this.tail.next = newNode;
            this.tail = newNode;
        }

        this.size++;
    }

    @Override
    public E poll() {
        // if the queue is empty
        if (head == null && tail == null) {
            throw new IllegalStateException("Queue is empty");
        }

        E oldElement = this.head.element;

        if (head == tail){ // if the queue has only one element
            this.head = null;
            this.tail = null;
        } else { // if the queue has more than one element
            Node<E> tempNode = this.head;
            this.head = this.head.next;
            tempNode.next = null;
        }

        this.size--;
        return oldElement;
    }


    @Override
    public int size () {
        return this.size;
    }

    @Override
    public boolean isEmpty () {
        if (this.head == null && this.tail == null) {
            return true;
        }
        return false;
    }
}