package net.sourceforge.peers.G729.spi.dsp;

import java.util.List;

public interface DspFactory {
    Processor newProcessor() throws InstantiationException, ClassNotFoundException, IllegalAccessException;

    void setCodecs(List<String> var1);
}