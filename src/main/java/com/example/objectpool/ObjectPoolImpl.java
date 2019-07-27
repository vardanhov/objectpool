package com.example.objectpool;

import java.util.Stack;

import static com.example.objectpool.MessageKeys.*;

public abstract class ObjectPoolImpl<T> implements ObjectPool<T> {

    private final int minSize;

    private final int maxSize;

    private final Stack<T> pool;

    private int poolSize;

    ObjectPoolImpl(final int minSize, final int maxSize, final boolean autoInit) {
        validate(minSize, maxSize);

        this.minSize = minSize;
        this.maxSize = maxSize;
        this.pool = new Stack<>();

        if (autoInit) {
            for (int i = 0; i < minSize; i++) {
                final T t = create();
                this.pool.push(t);
            }
            this.poolSize = minSize;
        } else {
            this.poolSize = 0;
        }
    }

    @Override
    public synchronized T get() {
        if (poolSize < minSize) {
            final T t = create();
            poolSize++;
            return t;
        }
        if (poolSize < maxSize) {
            if (pool.isEmpty()) {
                final T t = create();
                poolSize++;
                return t;
            }
            return pool.pop();
        }
        try {
            wait();
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            notifyAll();
        }
        return pool.pop();
    }

    @Override
    public synchronized void set(final T t) {
        try {
            pool.push(t);
        } finally {
            notifyAll();
        }
    }

    protected abstract T create();

    public final void process() {
        T t = null;
        try {
            t = get();
            doProcess(t);
        } finally {
            if (t != null) {
                set(t);
            }
        }
    }

    protected abstract void doProcess(T t);

    private static void validate(final int minSize, final int maxSize) {
        if (minSize <= 0) {
            throw new IllegalArgumentException(MIN_SIZE);
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException(MAX_SIZE);
        }
        if (minSize > maxSize) {
            throw new IllegalArgumentException(MIN_GREATER_MAX);
        }
    }
}