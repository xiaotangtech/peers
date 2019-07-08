package net.sourceforge.peers.g729.spi.pooling;

public interface ResourcePool<T extends PooledObject> {
    T poll();

    void offer(T var1);

    void release();

    int count();

    int size();

    boolean isEmpty();
}