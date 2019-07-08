package net.sourceforge.peers.G729.spi.pooling;

public interface PooledObjectFactory<T extends PooledObject> {
    T produce();
}
