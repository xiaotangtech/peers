package net.sourceforge.peers.G729.codec;

public class CircularBuffer {
    private byte[] buffer;
    private int readCursor = 0;
    private int writeCursor = 0;
    private int availableData = 0;
    private final Object LOCK = new Object();

    public CircularBuffer(int size) {
        this.buffer = new byte[size];
    }

    public void addData(byte[] data) {
        Object var2 = this.LOCK;
        synchronized(this.LOCK) {
            boolean zeros = false;
            if (!zeros) {
                for(int q = 0; q < data.length; ++q) {
                    this.buffer[(this.writeCursor + q) % this.buffer.length] = data[q];
                }

                this.writeCursor = (this.writeCursor + data.length) % this.buffer.length;
                this.availableData += data.length;
                if (this.availableData > this.buffer.length) {
                    this.readCursor = (this.readCursor + this.availableData - this.buffer.length) % this.buffer.length;
                    this.availableData = this.buffer.length;
                }
            }

        }
    }

    public byte[] getData(int size) {
        Object var2 = this.LOCK;
        synchronized(this.LOCK) {
            if (this.availableData < size) {
                return null;
            } else {
                byte[] data = new byte[size];

                for(int q = 0; q < size; ++q) {
                    data[q] = this.buffer[(this.readCursor + q) % this.buffer.length];
                }

                this.readCursor = (this.readCursor + data.length) % this.buffer.length;
                this.availableData -= size;
                return data;
            }
        }
    }
}