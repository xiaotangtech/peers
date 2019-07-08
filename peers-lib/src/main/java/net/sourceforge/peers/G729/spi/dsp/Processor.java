package net.sourceforge.peers.g729.spi.dsp;


import net.sourceforge.peers.g729.spi.format.Format;
import net.sourceforge.peers.g729.spi.memory.Frame;

public interface Processor {
    Codec[] getCodecs();

    Frame process(Frame var1, Format var2, Format var3);
}