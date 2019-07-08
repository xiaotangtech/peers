package net.sourceforge.peers.g729.spi.clock;

public interface TimerTask {
    void cancel();

    boolean isActive();
}