package net.sourceforge.peers.G729.spi.clock;

public interface Task {
    void cancel();

    boolean isActive();

    int perform();
}