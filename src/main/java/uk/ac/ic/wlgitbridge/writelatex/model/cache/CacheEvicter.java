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

    private void evict() {
        System.out.println();
        System.out.println("Performing eviction...");
        System.out.println("Memory before: " + Runtime.getRuntime().freeMemory());
        for (int i = 0; i < 100; i++) {
            E remove = cache.remove();
            dataSource.evict(remove);
        }
        System.out.println("Memory: " + Runtime.getRuntime().freeMemory());
        System.out.println();
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
        cache.add(element);
    }

}
