package net.sourceforge.peers.g729;

import java.io.Serializable;

public interface Component extends Serializable {
    String getId();

    String getName();

    void reset();

    void activate();

    void deactivate();
}