package net.sourceforge.peers.G729.spi;

import java.util.Collection;

public interface ResourceGroup extends Component {
    Collection<MediaType> getMediaTypes();

    MediaSink getSink(MediaType var1);

    MediaSource getSource(MediaType var1);
}