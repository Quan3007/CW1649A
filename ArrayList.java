package CW_NDQ;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 3;
    private E[] elements;
    private int nextIndex;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.nextIndex = 0;
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_CAPACITY;
        this.elements = (E[]) new Object[initialCapacity];
        this.nextIndex = 0;
    }

    public boolean add(E element) {
        ensureCapacity(nextIndex + 1);
        elements[nextIndex++] = element;
        return true;
    }

    public boolean add(int index, E element) {
        if (index < 0 || index > nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        ensureCapacity(nextIndex + 1);
        for (int i = nextIndex; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        nextIndex++;
        return true;
    }

    public E get(int index) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return elements[index];
    }

    public E set(int index, E element) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        E oldElement = elements[index];
        for (int i = index; i < nextIndex - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--nextIndex] = null;
        if (nextIndex > 0 && nextIndex < elements.length / 3 && elements.length > DEFAULT_CAPACITY) {
            int newCapacity = Math.max(elements.length / 2, DEFAULT_CAPACITY);
            E[] smallerElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < nextIndex; i++) {
                smallerElements[i] = elements[i];
            }
            elements = smallerElements;
        }
        return oldElement;
    }

    public int size() {
        return nextIndex;
    }

    public int indexOf(E element) {
        for (int i = 0; i < nextIndex; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(E element) {
        for (int i = 0; i < nextIndex; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return nextIndex == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < nextIndex; i++) {
            elements[i] = null;
        }
        nextIndex = 0;
        if (elements.length > DEFAULT_CAPACITY) {
            elements = (E[]) new Object[DEFAULT_CAPACITY];
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            @SuppressWarnings("unchecked")
            E[] largerElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < nextIndex; i++) {
                largerElements[i] = elements[i];
            }
            elements = largerElements;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nextIndex; i++) {
            sb.append(elements[i]);
            if (i < nextIndex - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            public boolean hasNext() { return cursor < nextIndex; }
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[cursor++];
            }
        };
    }
}
