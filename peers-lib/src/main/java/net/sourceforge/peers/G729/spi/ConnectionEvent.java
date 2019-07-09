package net.sourceforge.peers.G729.spi;

import net.sourceforge.peers.G729.spi.listener.Event;

public interface ConnectionEvent extends Event<Connection> {
    int STATE_CHANGE = 1;
    int MODE_CHANGE = 2;
    int IO_ERROR = 3;

    int getId();
}