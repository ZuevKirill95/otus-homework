package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, null, "REMOVE");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyListeners(key, null, "GET");

        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String PUT) {
        listeners.forEach(
                listener -> {
                    try {
                        listener.notify(key, value, PUT);
                    } catch (Exception e) {
                        LOGGER.error("Ошибка при выполнении слушателя", e);
                    }
                }
        );
    }
}
