package net.sourceforge.peers.G729.spi.resource;

public interface TTSEngine {
    void setVoiceName(String var1);

    String getVoiceName();

    void setVolume(int var1);

    int getVolume();

    void setText(String var1);
}