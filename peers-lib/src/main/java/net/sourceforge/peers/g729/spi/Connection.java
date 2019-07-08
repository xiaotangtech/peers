package net.sourceforge.peers.g729.spi;

import net.sourceforge.peers.g729.spi.utils.Text;

import java.io.IOException;

public interface Connection {
    int getId();

    String getTextualId();

    boolean getIsLocal();

    void setIsLocal(boolean var1);

    ConnectionState getState();

    ConnectionMode getMode();

    void setMode(ConnectionMode var1) throws ModeNotSupportedException;

    void setEndpoint(Endpoint var1);

    Endpoint getEndpoint();

    String getDescriptor();

    String getLocalDescriptor();

    String getRemoteDescriptor();

    void generateOffer(boolean var1) throws IOException;

    void setOtherParty(Connection var1) throws IOException;

    void setOtherParty(byte[] var1) throws IOException;

    void setOtherParty(Text var1) throws IOException;

    void addListener(ConnectionListener var1);

    void setConnectionFailureListener(ConnectionFailureListener var1);

    void removeListener(ConnectionListener var1);

    long getPacketsReceived();

    long getBytesReceived();

    long getPacketsTransmitted();

    long getBytesTransmitted();

    double getJitter();

    boolean isAvailable();

    void generateCname();

    String getCname();
}