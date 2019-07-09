package net.sourceforge.peers.G729.spi.clock;

public interface TimerTask {
    void cancel();

    boolean isActive();
}