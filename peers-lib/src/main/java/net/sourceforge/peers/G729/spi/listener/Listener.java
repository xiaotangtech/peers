package net.sourceforge.peers.G729.spi.listener;

public interface Listener<E extends Event> {
    void process(E var1);
}
