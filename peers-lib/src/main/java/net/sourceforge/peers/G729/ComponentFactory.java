package net.sourceforge.peers.g729;

import net.sourceforge.peers.g729.spi.Connection;

public interface ComponentFactory {
    Component newAudioComponent(ComponentType var1);

    void releaseAudioComponent(Component var1, ComponentType var2);

    Connection newConnection(boolean var1);

    void releaseConnection(Connection var1, boolean var2);
}