package net.sourceforge.peers.g729.spi.tone;

import net.sourceforge.peers.g729.spi.listener.Event;

public interface ToneEvent extends Event<ToneDetector> {
    int getFrequency();
}