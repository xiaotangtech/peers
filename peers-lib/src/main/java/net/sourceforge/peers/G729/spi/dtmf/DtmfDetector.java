package net.sourceforge.peers.G729.spi.dtmf;

import net.sourceforge.peers.G729.MediaSink;
import net.sourceforge.peers.G729.spi.listener.TooManyListenersException;

public interface DtmfDetector extends MediaSink {
    int DEFAULT_SIGNAL_LEVEL = -30;
    int DEFAULT_SIGNAL_DURATION = 80;
    int DEFAULT_INTERDIGIT_INTERVAL = 500;

    int getInterdigitInterval();

    int getVolume();

    void activate();

    void deactivate();

    void flushBuffer();

    void clearDigits();

    void addListener(DtmfDetectorListener var1) throws TooManyListenersException;

    void removeListener(DtmfDetectorListener var1);

    void clearAllListeners();
}