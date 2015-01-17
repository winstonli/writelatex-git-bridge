package uk.ac.ic.wlgitbridge.writelatex.model.cache;

/**
 * Created by Winston on 17/01/15.
 */
public class CacheEvicter<E> {

    private final EvictionDataSource<E> dataSource;
    private final Cache<E> cache;

    public CacheEvicter(EvictionDataSource<E> dataSource) {
        this.dataSource = dataSource;
        cache = new LRUCache<E>();
    }

    public void refresh(E element) {
        cache.add(element);
    }

}
