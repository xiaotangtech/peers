package net.sourceforge.peers.g729.spi.listener;

public interface Listener<E extends Event> {
    void process(E var1);
}
