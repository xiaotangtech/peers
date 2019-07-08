package net.sourceforge.peers.g729.spi;

import net.sourceforge.peers.g729.Component;

public interface MultimediaResourceGroup extends Component {
    MultimediaSink getMultimediaSink();

    MultimediaSource getMultimediaSource();
}