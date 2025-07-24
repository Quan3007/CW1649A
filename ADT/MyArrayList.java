
package CW_NDQ.ADT;

public class MyArrayList<E> {

    private E[] elements;
    private int nextIndex;

    public MyArrayList() {
        this.elements = (E[]) new Object[3];
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


    public E remove ( int index ) {
        if (index < 0 || index > nextIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        E oldElement = elements[index];

        // shift elements to the left
        for ( int i = index; i < nextIndex - 1; i++ ) {
            elements[i] = elements[i + 1];
        }

        elements[nextIndex - 1] = null;
        nextIndex--;

        if (nextIndex < elements.length / 3) {
            E[] smallerElements = (E[]) new Object[elements.length / 2];

            for ( int i = 0; i < nextIndex; i++ ) {
                smallerElements[i] = elements[i];
            }

            elements = smallerElements;
        }

        return oldElement;
    }

    public int size() {
        return nextIndex;
    }

    public boolean isEmpty () {
        if (nextIndex == 0) {
            return true;
        }

        return false;
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
        StringBuilder result = new StringBuilder();
        result.append("[");
        for ( int i = 0; i < nextIndex; i++ ) {
            result.append(elements[i]);

            if (i < nextIndex - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}

