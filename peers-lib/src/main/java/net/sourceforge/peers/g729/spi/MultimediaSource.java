package net.sourceforge.peers.g729.spi;

import net.sourceforge.peers.g729.MediaSource;
import net.sourceforge.peers.g729.spi.dsp.Processor;

import java.util.Collection;

public interface MultimediaSource {
    Collection<MediaType> getMediaTypes();

    MediaSource getMediaSource(MediaType var1);

    void setDsp(Processor var1, MediaType var2);
}