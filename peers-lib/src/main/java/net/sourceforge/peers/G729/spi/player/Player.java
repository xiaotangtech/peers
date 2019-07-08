package net.sourceforge.peers.g729.spi.player;

import net.sourceforge.peers.g729.MediaSource;
import net.sourceforge.peers.g729.spi.ResourceUnavailableException;
import net.sourceforge.peers.g729.spi.listener.TooManyListenersException;

import java.net.MalformedURLException;

public interface Player extends MediaSource {
    void setURL(String var1) throws MalformedURLException, ResourceUnavailableException;

    void addListener(PlayerListener var1) throws TooManyListenersException;

    void removeListener(PlayerListener var1);

    void clearAllListeners();
}