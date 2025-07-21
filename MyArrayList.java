
package CW_NDQ;

public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 3;
    private E[] elements;
    private int nextIndex;

    public MyArrayList() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.nextIndex = 0;
    }

    public boolean add(E element) {
        ensureCapacity(nextIndex + 1);
        elements[nextIndex++] = element;
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

    public boolean isEmpty() {
        return nextIndex == 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            E[] largerElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < nextIndex; i++) {
                largerElements[i] = elements[i];
            }
            elements = largerElements;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nextIndex; i++) {
            sb.append(elements[i]);
            if (i < nextIndex - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
