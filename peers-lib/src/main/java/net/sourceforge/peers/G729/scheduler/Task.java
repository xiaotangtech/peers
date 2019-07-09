
package net.sourceforge.peers.G729.scheduler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task implements Runnable {
    private static AtomicInteger id = new AtomicInteger(0);
    private volatile boolean isActive = true;
    private volatile boolean isHeartbeat = true;
    protected TaskListener listener;
    private final Object LOCK = new Object();
    private AtomicBoolean inQueue0 = new AtomicBoolean(false);
    private AtomicBoolean inQueue1 = new AtomicBoolean(false);
//    private Logger logger = LogManager.getLogger(Task.class);
    protected int taskId;

    public Task() {
        this.taskId = id.incrementAndGet();
    }

    public void storedInQueue0() {
        this.inQueue0.set(true);
    }

    public void storedInQueue1() {
        this.inQueue1.set(true);
    }

    public void removeFromQueue0() {
        this.inQueue0.set(false);
    }

    public void removeFromQueue1() {
        this.inQueue1.set(false);
    }

    public Boolean isInQueue0() {
        return this.inQueue0.get();
    }

    public Boolean isInQueue1() {
        return this.inQueue1.get();
    }

    public void setListener(TaskListener listener) {
        this.listener = listener;
    }

    public abstract int getQueueNumber();

    public abstract long perform();

    public void cancel() {
        Object var1 = this.LOCK;
        synchronized(this.LOCK) {
            this.isActive = false;
        }
    }

    public void run() {
        if (this.isActive) {
            try {
                this.perform();
                if (this.listener != null) {
                    this.listener.onTerminate();
                }
            } catch (Exception var2) {
//                this.logger.error("Could not execute task " + this.taskId + ": " + var2.getMessage(), var2);
                if (this.listener != null) {
                    this.listener.handlerError(var2);
                }
            }
        }

    }

    protected void activate(Boolean isHeartbeat) {
        Object var2 = this.LOCK;
        synchronized(this.LOCK) {
            this.isActive = true;
            this.isHeartbeat = isHeartbeat;
        }
    }
}
