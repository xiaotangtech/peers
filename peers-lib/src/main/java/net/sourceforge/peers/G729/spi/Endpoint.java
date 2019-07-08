package net.sourceforge.peers.g729.spi;


import net.sourceforge.peers.g729.Component;
import net.sourceforge.peers.g729.ComponentType;
import net.sourceforge.peers.g729.scheduler.PriorityQueueScheduler;

public interface Endpoint {
    String getLocalName();

    EndpointState getState();

    void start() throws ResourceUnavailableException;

    void stop();

    Connection createConnection(ConnectionType var1, Boolean var2) throws ResourceUnavailableException;

    void deleteConnection(Connection var1);

    void deleteConnection(Connection var1, ConnectionType var2);

    void modeUpdated(ConnectionMode var1, ConnectionMode var2);

    void deleteAllConnections();

    int getActiveConnectionsCount();

    void configure(boolean var1);

    PriorityQueueScheduler getScheduler();

    void setScheduler(PriorityQueueScheduler var1);

    Component getResource(MediaType var1, ComponentType var2);

    boolean hasResource(MediaType var1, ComponentType var2);

    void releaseResource(MediaType var1, ComponentType var2);
}