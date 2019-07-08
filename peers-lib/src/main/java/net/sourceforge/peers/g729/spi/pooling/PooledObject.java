package net.sourceforge.peers.g729.spi.pooling;

public interface PooledObject {
    void checkIn();

    void checkOut();
}
