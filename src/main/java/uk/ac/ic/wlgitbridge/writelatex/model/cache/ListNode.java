package uk.ac.ic.wlgitbridge.writelatex.model.cache;

/**
 * Created by Winston on 17/01/15.
 */
public class ListNode<E> {

    public ListNode<E> prev;
    public ListNode<E> next;
    private E element;

    public ListNode(E element) {
        this.element = element;
    }

    public void remove() {
        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }
        prev = null;
        next = null;
    }

    public E getElement() {
        return element;
    }

    @Override
    public String toString() {
        return element.toString();
    }

}
