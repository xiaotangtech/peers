package net.sourceforge.peers.g729.spi;

import net.sourceforge.peers.g729.Component;
import net.sourceforge.peers.g729.MediaSink;
import net.sourceforge.peers.g729.MediaSource;

import java.util.Collection;

public interface ResourceGroup extends Component {
    Collection<MediaType> getMediaTypes();

    MediaSink getSink(MediaType var1);

    MediaSource getSource(MediaType var1);
}