package net.sourceforge.peers.G729.spi.pooling;

public interface PooledObject {
    void checkIn();

    void checkOut();
}
