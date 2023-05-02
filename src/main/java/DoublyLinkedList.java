import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<E> implements Iterable<E> {
    private int size = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    public E get(int index) {
        return node(index).item;
    }

    public void addFirst(E element) {
        linkFirst(element);
    }

    public void addLast(E element) {
        linkLast(element);
    }

    public void add(int index, E element) {
        if (index == size) {
            addLast(element);
        } else {
            linkBefore(element, node(index));
        }
    }

    public E set(int index, E element) {
        final Node<E> node = node(index);
        E old = node.item;
        node.item = element;
        return old;
    }

    public E removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return unlinkFirst();
    }

    public E removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return unlinkLast();
    }

    public E remove(int index) {
        return unlink(node(index));
    }

    public boolean remove(Object object) {
        if (object == null) {
            for (Node<E> node = first; node != null; node = node.next) {
                if (node.item == null) {
                    unlink(node);
                    return true;
                }
            }
        } else {
            for (Node<E> node = first; node != null; node = node.next) {
                if (object.equals(node.item)) {
                    unlink(node);
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        Node<E> node = first;
        while (node != null) {
            Node<E> next = node.next;
            node.item = null;
            node.prev = node.next = null;
            node = next;
        }
        first = last = null;
        size = 0;
    }

    public int indexOf(Object object) {
        int index = 0;
        if (object == null) {
            for (Node<E> node = first; node != null; node = node.next) {
                if (node.item == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node<E> node = first; node != null; node = node.next) {
                if (object.equals(node.item)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public boolean contains(Object object) {
        return indexOf(object) != -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private E unlinkFirst() {
        final E element = first.item;
        final Node<E> next = first.next;
        first.item = null;
        first.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        --size;
        return element;
    }

    private E unlinkLast() {
        final E element = last.item;
        final Node<E> prev = last.prev;
        last.item = null;
        last.prev = null;
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        --size;
        return element;
    }

    private E unlink(Node<E> node) {
        final E element = node.item;
        final Node<E> prev = node.prev;
        final Node<E> next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.item = null;
        --size;
        return element;
    }

    private void linkFirst(E element) {
        final Node<E> node = new Node<>(null, element, first);
        if (first == null) {
            last = node;
        } else {
            first.prev = node;
        }
        first = node;
        ++size;
    }

    private void linkLast(E element) {
        final Node<E> node = new Node<>(last, element, null);
        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        ++size;
    }

    private void linkBefore(E element, Node<E> node) {
        final Node<E> prev = node.prev;
        final Node<E> current = new Node<>(prev, element, node);
        if (prev == null) {
            first = current;
        } else {
            prev.next = current;
        }
        node.prev = current;
        ++size;
    }

    private Node<E> node(int index) {
        ensureIndexInOfBounds(index);
        Node<E> node;
        if (index < (size >> 1)) {
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }
        return node;
    }

    private void ensureIndexInOfBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public String toString() {
        Iterator<E> iterator = iterator();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (iterator.hasNext()) {
            sb.append(iterator.next());
            while (iterator.hasNext()) {
                sb.append(',').append(' ');
                sb.append(iterator.next());
            }
        }
        return sb.append(']').toString();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E element = current.item;
                current = current.next;
                return element;
            }
        };
    }

    private static final class Node<E> {
        private E item;
        private Node<E> prev, next;

        private Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }
}
