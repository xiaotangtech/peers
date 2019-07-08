package net.sourceforge.peers.g729.spi.tone;

import net.sourceforge.peers.g729.MediaSink;
import net.sourceforge.peers.g729.spi.listener.TooManyListenersException;

public interface ToneDetector extends MediaSink {
    void setFrequency(int[] var1);

    int[] getFrequency();

    void setVolume(int var1);

    int getVolume();

    void activate();

    void deactivate();

    void addListener(ToneDetectorListener var1) throws TooManyListenersException;

    void removeListener(ToneDetectorListener var1);

    void clearAllListeners();
}