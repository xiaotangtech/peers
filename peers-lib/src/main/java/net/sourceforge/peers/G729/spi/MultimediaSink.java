package net.sourceforge.peers.G729.spi;

import net.sourceforge.peers.G729.MediaSink;

import java.util.Collection;

public interface MultimediaSink {
    Collection<MediaType> getMediaTypes();

    MediaSink getMediaSink(MediaType var1);
}