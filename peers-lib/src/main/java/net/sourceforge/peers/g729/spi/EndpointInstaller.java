package net.sourceforge.peers.g729.spi;

public interface EndpointInstaller {
    void install();

    void uninstall();

    boolean canExpand();

    void newEndpoint();
}