package net.sourceforge.peers.G729.spi.pooling;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractConcurrentResourcePool<T extends PooledObject> extends AbstractResourcePool<T> {
    protected AbstractConcurrentResourcePool(int initialCapacity) {
        super(new ConcurrentLinkedQueue(), initialCapacity);
    }
}