package uk.ac.ic.wlgitbridge.writelatex.model.cache;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LRUCacheTest {

    private LRUCache<String> cache = new LRUCache<String>();

    @Test
    public void testAdd() throws Exception {
        cache.add("proj1");
        assertEquals(Arrays.asList("proj1"), cache.asList());
        cache.add("proj2");
        assertEquals(Arrays.asList("proj1", "proj2"), cache.asList());
        cache.add("proj3");
        assertEquals(Arrays.asList("proj1", "proj2", "proj3"), cache.asList());
        cache.add("proj2");
        assertEquals(Arrays.asList("proj1", "proj3", "proj2"), cache.asList());
    }

    @Test
    public void testRemove() throws Exception {
        cache.add("proj1");
        cache.add("proj2");
        cache.add("proj3");
        cache.add("proj4");
        assertEquals(Arrays.asList("proj1", "proj2", "proj3", "proj4"), cache.asList());
        assertEquals("proj1", cache.remove());
        assertEquals(Arrays.asList("proj2", "proj3", "proj4"), cache.asList());
        cache.add("proj2");
        assertEquals(Arrays.asList("proj3", "proj4", "proj2"), cache.asList());
        assertEquals("proj3", cache.remove());
        assertEquals(Arrays.asList("proj4", "proj2"), cache.asList());
        cache.add("proj2");
        assertEquals(Arrays.asList("proj4", "proj2"), cache.asList());
        cache.add("proj4");
        assertEquals(Arrays.asList("proj2", "proj4"), cache.asList());
        assertEquals("proj2", cache.remove());
        assertEquals(Arrays.asList("proj4"), cache.asList());
        assertEquals("proj4", cache.remove());
        assertEquals(Arrays.asList(), cache.asList());
        cache.add("proj1");
        cache.add("proj2");
        cache.add("proj3");
        cache.add("proj4");
        assertEquals(Arrays.asList("proj1", "proj2", "proj3"), cache.remove(3));
        assertEquals(Arrays.asList("proj4"), cache.asList());
        cache.add("proj1");
        cache.add("proj2");
        cache.add("proj3");
        cache.add("proj4");
        assertEquals(Arrays.asList("proj1", "proj2", "proj3", "proj4"), cache.remove(4));
        assertEquals(Arrays.asList(), cache.asList());
        cache.add("proj1");
        cache.add("proj2");
        cache.add("proj3");
        cache.add("proj4");
        assertEquals(Arrays.asList("proj1", "proj2"), cache.remove(2));
        assertEquals(Arrays.asList("proj3", "proj4"), cache.asList());
        cache.add("proj1");
        cache.add("proj2");
        cache.add("proj3");
        cache.add("proj4");
        assertEquals(Arrays.asList("proj1"), cache.remove(1));
        assertEquals(Arrays.asList("proj2", "proj3", "proj4"), cache.asList());
    }

}