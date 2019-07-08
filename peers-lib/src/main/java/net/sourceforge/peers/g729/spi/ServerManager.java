package net.sourceforge.peers.g729.spi;

public interface ServerManager {
    ControlProtocol getControlProtocol();

    void activate() throws IllegalStateException;

    void deactivate() throws IllegalStateException;

    boolean isActive();

    void onStarted(Endpoint var1, EndpointInstaller var2);

    void onStopped(Endpoint var1);
}