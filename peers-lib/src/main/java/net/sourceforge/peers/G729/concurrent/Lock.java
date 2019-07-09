package net.sourceforge.peers.G729.concurrent;

public class Lock {
    protected boolean locked = false;

    public Lock() {
    }

    public synchronized void lock() throws InterruptedException {
        while(this.locked) {
            this.wait();
        }

        this.locked = true;
    }

    public synchronized void unlock() {
        this.locked = false;
        this.notify();
    }
}