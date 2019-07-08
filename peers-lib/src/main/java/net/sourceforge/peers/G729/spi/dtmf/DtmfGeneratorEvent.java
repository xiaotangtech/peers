package net.sourceforge.peers.G729.spi.dtmf;

import net.sourceforge.peers.G729.spi.listener.Event;

public class DtmfGeneratorEvent implements Event<DtmfGenerator> {
    public static final int COMPLETED = 1;
    private final DtmfGenerator generator;
    private final int id;

    public DtmfGeneratorEvent(DtmfGenerator generator, int id) {
        this.generator = generator;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public DtmfGenerator getSource() {
        return this.generator;
    }
}