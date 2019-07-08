package net.sourceforge.peers.G729.spi.dsp;


import net.sourceforge.peers.G729.spi.format.Format;

public interface Processor {
    Codec[] getCodecs();

    Frame process(Frame var1, Format var2, Format var3);
}