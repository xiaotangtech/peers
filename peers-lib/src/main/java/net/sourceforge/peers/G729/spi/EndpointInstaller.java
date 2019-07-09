package net.sourceforge.peers.G729.spi;

public interface EndpointInstaller {
    void install();

    void uninstall();

    boolean canExpand();

    void newEndpoint();
}