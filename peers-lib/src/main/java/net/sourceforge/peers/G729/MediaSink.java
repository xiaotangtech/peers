package net.sourceforge.peers.G729;

public interface MediaSink extends Component {
    boolean isStarted();

    long getPacketsReceived();

    long getBytesReceived();

    void perform(Frame var1);
}