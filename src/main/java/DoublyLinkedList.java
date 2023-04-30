import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Triển khai danh sách liên kết đôi.
 *
 * @author khoa-omega
 */
public class DoublyLinkedList<E> implements Iterable<E> {
    private int size = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    /**
     * Lấy một phần tử ở vị trí đầu: O(1)
     */
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    /**
     * Lấy một phần tử ở vị trí cuối: O(1)
     */
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    /**
     * Lấy một phần tử ở vị trí cụ thể: O(n)
     */
    public E get(int index) {
        return node(index).item;
    }

    /**
     * Thêm một phần tử vào vị trí đầu: O(1)
     */
    public void addFirst(E element) {
        final Node<E> node = new Node<>(null, element, first);
        if (first == null) {
            last = node;
        } else {
            first.prev = node;
        }
        first = node;
        ++size;
    }

    /**
     * Thêm một phần tử vào vị trí cuối: O(1)
     */
    public void addLast(E element) {
        final Node<E> node = new Node<>(last, element, null);
        if (last == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        ++size;
    }

    /**
     * Thêm một phần tử vào vị trí cụ thể: O(n)
     */
    public void add(int index, E element) {
        if (index == size) {
            addLast(element);
        } else {
            linkBefore(element, node(index));
        }
    }

    /**
     * Cập nhật một phần tử ở vị trí cụ thể: O(n)
     */
    public E set(int index, E element) {
        final Node<E> node = node(index);
        E old = node.item;
        node.item = element;
        return old;
    }

    /**
     * Xóa một phần tử ở vị trí đầu: O(1)
     */
    public E removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return unlink(first);
    }

    /**
     * Xóa một phần tử ở vị trí cuối: O(1)
     */
    public E removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return unlink(last);
    }

    /**
     * Xóa một phần tử ở vị trí cụ thể: O(n)
     */
    public E remove(int index) {
        return unlink(node(index));
    }

    /**
     * Xóa một phần tử cụ thể: O(n)
     */
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

    /**
     * Làm trống danh sách: O(n)
     */
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


    /**
     * @return Chỉ số của một phần tử cụ thể: O(n)
     */
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

    /**
     * Kiểm tra danh sách có trống hay không? : O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Số lượng phần tử trong danh sách: O(1)
     */
    public int size() {
        return size;
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

    private void linkBefore(E element, Node<E> node) {
        final Node<E> prev = node.prev;
        final Node<E> current = new Node<>(prev, element, node);
        if (prev == null) {
            first = current;
        } else {
            prev.next = current;
        }
        node.prev = current;
        size++;
    }

    private Node<E> node(int index) {
        ensure(index);
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

    private void ensure(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Node<E> node = first; node != null; node = node.next) {
            sb.append(node.item);
            if (node.next != null) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> node = first;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public E next() {
                E element = node.item;
                node = node.next;
                return element;
            }
        };
    }

    private static final class Node<T> {
        private T item;
        private Node<T> prev, next;

        private Node(Node<T> prev, T item, Node<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

        @Override
        public String toString() {
            return item == null ? "null" : item.toString();
        }
    }
}
