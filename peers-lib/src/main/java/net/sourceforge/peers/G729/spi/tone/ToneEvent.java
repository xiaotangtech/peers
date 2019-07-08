package net.sourceforge.peers.G729.spi.tone;

import net.sourceforge.peers.G729.spi.listener.Event;

public interface ToneEvent extends Event<ToneDetector> {
    int getFrequency();
}