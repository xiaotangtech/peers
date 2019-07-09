package net.sourceforge.peers.G729.spi;

public interface MultimediaResourceGroup extends Component {
    MultimediaSink getMultimediaSink();

    MultimediaSource getMultimediaSource();
}