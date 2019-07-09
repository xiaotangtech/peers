package net.sourceforge.peers.G729.scheduler;

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
    Clock getWallClock();

    Future<?> submit(Runnable var1) throws RejectedExecutionException;

    ScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4) throws RejectedExecutionException;

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) throws IllegalArgumentException, RejectedExecutionException;

    void start();

    void stop();

    boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException;
}
