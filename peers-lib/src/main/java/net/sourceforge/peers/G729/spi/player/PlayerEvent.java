package net.sourceforge.peers.G729.spi.player;

import net.sourceforge.peers.G729.spi.listener.Event;

public interface PlayerEvent extends Event<Player> {
    int START = 1;
    int STOP = 2;
    int FAILED = 3;
    int FILE_NOT_FOUND = 1;
    int BAD_URI = 2;

    int getID();

    int getQualifier();
}