package net.sourceforge.peers.g729;

import net.sourceforge.peers.g729.spi.memory.Frame;

public interface MediaSink extends Component {
    boolean isStarted();

    long getPacketsReceived();

    long getBytesReceived();

    void perform(Frame var1);
}