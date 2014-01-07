package net.serubin.serubans.util;

import java.util.Hashtable;
import java.util.Map;

public class TwoWayHashMap<K extends Object, V extends Object> {
    private Map<K, V> forward = new Hashtable<K, V>();
    private Map<V, K> backwards = new Hashtable<V, K>();

    public synchronized void add(K key, V value) {
        forward.put(key, value);
        backwards.put(value, key);
    }

    public synchronized Object get(Object value) {
        if (forward.containsKey(value)) {
            return forward.get(value);
        }
        if (backwards.containsKey(value)) {
            return backwards.get(value);
        }
        return null;
    }

    public synchronized boolean contains(Object value) {
        if (forward.containsKey(value)) {
            return true;
        }
        if (backwards.containsKey(value)) {
            return true;
        }
        return false;
    }
}
