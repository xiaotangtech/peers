package net.sourceforge.peers.g729.spi.pooling;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractResourcePool<T extends PooledObject> implements ResourcePool<T> {
    private final Queue<T> resources;
    private int initialCapacity;
    private final AtomicInteger size;

    protected AbstractResourcePool(Queue<T> resources, int initialCapacity) {
        this.resources = resources;
        this.initialCapacity = initialCapacity;
        this.size = new AtomicInteger(initialCapacity);
    }

    protected void populate() {
        for(int index = 0; index < this.initialCapacity; ++index) {
            this.resources.offer(this.createResource());
        }

    }

    public T poll() {
        T resource = (T) this.resources.poll();
        if (resource == null) {
            resource = this.createResource();
            this.size.incrementAndGet();
        }

        resource.checkOut();
        return resource;
    }

    public void offer(T resource) {
        if (resource != null) {
            resource.checkIn();
            this.resources.offer(resource);
        }

    }

    public void release() {
        this.resources.clear();
    }

    public int count() {
        return this.resources.size();
    }

    public int size() {
        return this.size.get();
    }

    public boolean isEmpty() {
        return this.resources.isEmpty();
    }

    protected abstract T createResource();
}