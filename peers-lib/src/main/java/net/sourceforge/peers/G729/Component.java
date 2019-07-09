package net.sourceforge.peers.G729;

import java.io.Serializable;

public interface Component extends Serializable {
    String getId();

    String getName();

    void reset();

    void activate();

    void deactivate();
}