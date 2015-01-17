package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import uk.ac.ic.wlgitbridge.util.Util;

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

    private void evict() {
        if (Runtime.getRuntime().freeMemory() < 2000000) {
            Util.sout("Performing eviction...");
            Util.sout("Memory before: " + Runtime.getRuntime().freeMemory());
            while (Runtime.getRuntime().freeMemory() < Runtime.getRuntime().maxMemory() / 5) {
                E remove = cache.remove();
                dataSource.evict(remove);
            }
            Runtime.getRuntime().gc();
            Util.sout("Memory afterwards: " + Runtime.getRuntime().freeMemory());
        }
    }

    public void start() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CacheEvicter.this.evict();
                }
            }

        }.start();
    }

    public void refresh(E element) {
        evict();
        cache.add(element);
    }

}
