package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import uk.ac.ic.wlgitbridge.util.Util;

import java.util.List;

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
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.freeMemory();
        if (before < 16 * MB) {
            Util.sout("Performing eviction...");
            while (!cache.isEmpty() && runtime.freeMemory() < runtime.maxMemory() / 5) {
                List<E> remove = cache.remove(10);
                for (E element : remove) {
                    dataSource.evict(element);
                }
                runtime.gc();
            }
            Util.sout("Eviction complete " + before + "B -> " + runtime.freeMemory() + "B");
        }
    }

    public void refresh(E element) {
        evict();
        cache.add(element);
    }

}
