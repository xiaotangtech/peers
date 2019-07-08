package net.sourceforge.peers.g729.spi.recorder;

import net.sourceforge.peers.g729.spi.listener.Event;

public interface RecorderEvent extends Event<Recorder> {
    int START = 1;
    int STOP = 2;
    int FAILED = 3;
    int SPEECH_DETECTED = 4;
    int MAX_DURATION_EXCEEDED = 1;
    int NO_SPEECH = 2;
    int SUCCESS = 3;

    int getID();

    int getQualifier();
}
