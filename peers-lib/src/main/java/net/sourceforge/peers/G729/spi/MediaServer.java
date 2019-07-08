package net.sourceforge.peers.g729.spi;

public interface MediaServer {
    void addManager(ServerManager var1);

    void removeManager(ServerManager var1);

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    boolean isRunning();
}