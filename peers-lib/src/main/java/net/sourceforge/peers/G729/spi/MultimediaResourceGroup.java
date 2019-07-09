package net.sourceforge.peers.G729.spi;

import net.sourceforge.peers.G729.Component;

public interface MultimediaResourceGroup extends Component {
    MultimediaSink getMultimediaSink();

    MultimediaSource getMultimediaSource();
}