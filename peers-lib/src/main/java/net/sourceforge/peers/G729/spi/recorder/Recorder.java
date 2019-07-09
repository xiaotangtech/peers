package net.sourceforge.peers.G729.spi.recorder;

import net.sourceforge.peers.G729.MediaSink;
import net.sourceforge.peers.G729.spi.listener.TooManyListenersException;

import java.io.IOException;

public interface Recorder extends MediaSink {
    void setRecordDir(String var1);

    void setRecordFile(String var1, boolean var2) throws IOException;

    void setMaxRecordTime(long var1);

    void setPreSpeechTimer(long var1);

    void setPostSpeechTimer(long var1);

    void addListener(RecorderListener var1) throws TooManyListenersException;

    void removeListener(RecorderListener var1);

    void clearAllListeners();
}