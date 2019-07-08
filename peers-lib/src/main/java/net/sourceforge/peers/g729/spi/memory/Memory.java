package net.sourceforge.peers.g729.spi.memory;


import net.sourceforge.peers.g729.concurrent.ConcurrentMap;

public class Memory {
    private static ConcurrentMap<Partition> partitions = new ConcurrentMap();

    public Memory() {
    }

    public static Frame allocate(int size) {
        Partition currPartition = (Partition)partitions.get(size);
        if (currPartition == null) {
            currPartition = new Partition(size);
            Partition oldPartition = (Partition)partitions.putIfAbsent(size, currPartition);
            if (oldPartition != null) {
                currPartition = oldPartition;
            }
        }

        return currPartition.allocate();
    }
}