package net.sourceforge.peers.g729.spi;

import java.io.Serializable;

public interface NamingService extends Serializable {
    Endpoint lookup(String var1, boolean var2) throws ResourceUnavailableException;

    Endpoint lookup(String var1) throws ResourceUnavailableException;

    int getEndpointCount();
}