package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Winston on 17/01/15.
 */
public class LRUCache<E> implements Cache<E> {

    private ListNode<E> first;
    private ListNode<E> last;
    private Map<E, ListNode<E>> nodeTable;

    public LRUCache() {
        first = null;
        last = null;
        nodeTable = new HashMap<E, ListNode<E>>();
    }

    @Override
    public synchronized void add(E element) {
        ListNode<E> node = nodeTable.get(element);
        if (node == null) {
            node = new ListNode<E>(element);
            nodeTable.put(element, node);
        } else {
            if (node == first) {
                first = first.next;
            } else if (node == last) {
                return;
            }
            node.remove();
        }
        if (last == null) {
            first = node;
            last = node;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }
    }

    @Override
    public synchronized E remove() {
        ListNode<E> removed = first;
        if (removed == null) {
            return null;
        } else {
            first = removed.next;
            removed.remove();
            if (removed == last) {
                last = null;
            }
        }
        return removed.getElement();
    }

    private synchronized E peek() {
        if (first == null) {
            return null;
        }
        return first.getElement();
    }

    @Override
    public synchronized List<E> remove(int n) {
        List<E> removed = new LinkedList<E>();
        E current = null;
        for (int i = 0; peek() != null && i < n; i++) {
            current = remove();
            removed.add(current);
        }
        return removed;
    }

    @Override
    public String toString() {
        return asList().toString();
    }

    public List<E> asList() {
        List<E> thisList = new LinkedList<E>();
        for (ListNode<E> current = first; current != null; current = current.next) {
            thisList.add(current.getElement());
        }
        return thisList;
    }

    public void dump() {
        System.out.println("first: " + first);
        System.out.println("first.prev: " + first.prev);
        System.out.println("first.next: " + first.next);
        System.out.println("last: " + last);
        System.out.println("last.prev:" + last.prev);
        System.out.println("last.next:" + last.next);
    }

}
