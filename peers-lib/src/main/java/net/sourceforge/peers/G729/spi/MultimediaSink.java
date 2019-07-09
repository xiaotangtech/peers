package net.sourceforge.peers.G729.spi;

import java.util.Collection;

public interface MultimediaSink {
    Collection<MediaType> getMediaTypes();

    MediaSink getMediaSink(MediaType var1);
}