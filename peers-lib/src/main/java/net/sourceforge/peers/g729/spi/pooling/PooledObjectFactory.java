package net.sourceforge.peers.g729.spi.pooling;

public interface PooledObjectFactory<T extends PooledObject> {
    T produce();
}
