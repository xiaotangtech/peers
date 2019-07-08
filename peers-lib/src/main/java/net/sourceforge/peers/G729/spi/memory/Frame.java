package net.sourceforge.peers.g729.spi.memory;

import net.sourceforge.peers.g729.spi.format.Format;

import java.util.concurrent.atomic.AtomicBoolean;


public class Frame {
    private Partition partition;
    private byte[] data;
    private volatile int offset;
    private volatile int length;
    private volatile long timestamp;
    private volatile long duration = 9223372036854775807L;
    private volatile long sn;
    private volatile boolean eom;
    private volatile Format format;
    private volatile String header;
    protected AtomicBoolean inPartition = new AtomicBoolean(false);

    protected Frame(Partition partition, byte[] data) {
        this.partition = partition;
        this.data = data;
    }

    protected void reset() {
        this.timestamp = 0L;
        this.duration = 0L;
        this.sn = 0L;
        this.eom = false;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return this.data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSequenceNumber() {
        return this.sn;
    }

    public void setSequenceNumber(long sn) {
        this.sn = sn;
    }

    public boolean isEOM() {
        return this.eom;
    }

    public void setEOM(boolean value) {
        this.eom = value;
    }

    public Format getFormat() {
        return this.format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public void recycle() {
        this.partition.recycle(this);
    }

    public Frame clone() {
        Frame frame = Memory.allocate(this.data.length);
        System.arraycopy(this.data, this.offset, frame.data, this.offset, this.length);
        frame.offset = this.offset;
        frame.length = this.length;
        frame.duration = this.duration;
        frame.sn = this.sn;
        frame.eom = this.eom;
        frame.format = this.format;
        frame.timestamp = this.timestamp;
        frame.header = this.header;
        return frame;
    }
}