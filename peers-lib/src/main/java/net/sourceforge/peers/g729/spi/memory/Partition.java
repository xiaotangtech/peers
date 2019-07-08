package net.sourceforge.peers.g729.spi.memory;


import net.sourceforge.peers.g729.concurrent.ConcurrentCyclicFIFO;

public class Partition {
    protected int size;
    private ConcurrentCyclicFIFO<Frame> heap = new ConcurrentCyclicFIFO();

    protected Partition(int size) {
        this.size = size;
    }

    protected Frame allocate() {
        Frame result = (Frame)this.heap.poll();
        if (result == null) {
            return new Frame(this, new byte[this.size]);
        } else {
            result.inPartition.set(false);
            return result;
        }
    }

    protected void recycle(Frame frame) {
        if (!frame.inPartition.getAndSet(true)) {
            frame.setHeader((String)null);
            frame.setDuration(9223372036854775807L);
            frame.setEOM(false);
            this.heap.offer(frame);
        }
    }
}