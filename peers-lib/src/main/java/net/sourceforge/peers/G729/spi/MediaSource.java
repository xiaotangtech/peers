package net.sourceforge.peers.G729.spi;

public interface MediaSource extends Component {
    void setInitialDelay(long var1);

    void start();

    void stop();

    long getMediaTime();

    void setMediaTime(long var1);

    long getDuration();

    void setDuration(long var1);

    boolean isConnected();

    boolean isStarted();

    long getPacketsTransmitted();

    long getBytesTransmitted();
}