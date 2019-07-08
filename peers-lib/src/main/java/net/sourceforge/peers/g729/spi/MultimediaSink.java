package net.sourceforge.peers.g729.spi;

import net.sourceforge.peers.g729.MediaSink;

import java.util.Collection;

public interface MultimediaSink {
    Collection<MediaType> getMediaTypes();

    MediaSink getMediaSink(MediaType var1);
}