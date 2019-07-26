package com.example.objectpool;

public interface ObjectPool<T> {

    T get();

    void set(T t);
}