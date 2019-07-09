package net.sourceforge.peers.G729.scheduler;

public interface TaskChainListener {
    void onTermination();

    void onException(Exception var1);
}
