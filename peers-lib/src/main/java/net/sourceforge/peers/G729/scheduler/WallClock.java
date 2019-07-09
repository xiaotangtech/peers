package net.sourceforge.peers.G729.scheduler;

import java.util.concurrent.TimeUnit;

public class WallClock implements Clock {
    private TimeUnit timeUnit;

    public WallClock() {
        this.timeUnit = TimeUnit.NANOSECONDS;
    }

    public long getTime() {
        return System.nanoTime();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }

    public long getTime(TimeUnit timeUnit) {
        return timeUnit.convert(System.nanoTime(), this.timeUnit);
    }
}