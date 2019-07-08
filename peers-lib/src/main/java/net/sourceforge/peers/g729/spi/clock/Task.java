package net.sourceforge.peers.g729.spi.clock;

public interface Task {
    void cancel();

    boolean isActive();

    int perform();
}