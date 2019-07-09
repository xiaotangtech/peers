package net.sourceforge.peers.G729.scheduler;

public interface TaskListener {
    void onTerminate();

    void handlerError(Exception var1);
}
