package ru.otus.cache;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyCache.class);

    private final Map<Key, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(new Key(key), value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        cache.remove(new Key(key));
        notifyListeners(key, null, "REMOVE");
    }

    @Override
    public V get(K key) {
        V value = cache.get(new Key(key));
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

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(
                listener -> {
                    try {
                        listener.notify(key, value, action);
                    } catch (Exception e) {
                        LOGGER.error("Ошибка при выполнении слушателя", e);
                    }
                }
        );
    }

    @Value
    @AllArgsConstructor
    class Key {
        K key;
    }
}


