package net.sourceforge.peers.G729.spi;

import net.sourceforge.peers.G729.Component;
import net.sourceforge.peers.G729.MediaSink;
import net.sourceforge.peers.G729.MediaSource;

import java.util.Collection;

public interface ResourceGroup extends Component {
    Collection<MediaType> getMediaTypes();

    MediaSink getSink(MediaType var1);

    MediaSource getSource(MediaType var1);
}