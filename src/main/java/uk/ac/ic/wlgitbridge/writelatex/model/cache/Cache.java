package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import java.util.List;

/**
 * Created by Winston on 17/01/15.
 */
public interface Cache<E> {

    public void add(E element);
    public E remove();
    public List<E> remove(int n);
    public boolean isEmpty();

}
