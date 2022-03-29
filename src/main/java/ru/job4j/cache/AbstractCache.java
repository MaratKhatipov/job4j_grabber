package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/*
Создать структуру данных типа кеш.
Кеш должен быть абстрактный.
То есть необходимо, что бы можно было задать ключ получения объекта кеша
и в случае если его нет в памяти,
задать поведение загрузки этого объекта в кеш. Для это выделим класс:
 */

public abstract class AbstractCache<K, V> {

    private final Map<K, SoftReference<V>> cache = new HashMap<>();

    public void put(K key, V value) {
        cache.put(key, new SoftReference<>(value));
    }

    public V get(K key) {
        V value;
        SoftReference<V> reference = cache.get(key);
        if (reference != null) {
            value = reference.get();
        } else {
            value = load(key);
            put(key, value);
        }
        return value;
    }

    protected abstract V load(K key);
}
