package net.sourceforge.peers.g729.spi.dtmf;

import net.sourceforge.peers.g729.MediaSource;

public interface DtmfGenerator extends MediaSource {
    void addListener(DtmfGeneratorListener var1);

    void removeListener(DtmfGeneratorListener var1);

    void clearAllListeners();

    void setDigit(String var1);

    void setOOBDigit(String var1);

    String getDigit();

    String getOOBDigit();

    void setToneDuration(int var1);

    int getToneDuration();

    void setVolume(int var1);

    int getVolume();

    void start();

    void stop();
}