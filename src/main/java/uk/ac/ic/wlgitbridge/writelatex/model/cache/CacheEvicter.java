package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import uk.ac.ic.wlgitbridge.util.Util;

/**
 * Created by Winston on 17/01/15.
 */
public class CacheEvicter<E> {

    public static final int MB = 1000000;
    private final EvictionDataSource<E> dataSource;
    private final Cache<E> cache;

    public CacheEvicter(EvictionDataSource<E> dataSource) {
        this.dataSource = dataSource;
        cache = new LRUCache<E>();
    }

    private void evict() {
        long before = Runtime.getRuntime().freeMemory();
        if (before < 16 * MB) {
            Util.sout("Performing eviction...");
            while (!cache.isEmpty() && Runtime.getRuntime().freeMemory() < Runtime.getRuntime().maxMemory() / 5) {
                E remove = cache.remove();
                dataSource.evict(remove);
            }
            Runtime.getRuntime().gc();
            Util.sout("Eviction complete " + before + "B -> " + Runtime.getRuntime().freeMemory() + "B");
        }
    }

    public void refresh(E element) {
        evict();
        cache.add(element);
    }

}
