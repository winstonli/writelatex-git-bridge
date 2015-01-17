package uk.ac.ic.wlgitbridge.writelatex.model.cache;

/**
 * Created by Winston on 17/01/15.
 */
public interface EvictionDataSource<E> {

    public void evict(E element);

}
