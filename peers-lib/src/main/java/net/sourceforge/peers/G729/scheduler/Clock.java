package net.sourceforge.peers.G729.scheduler;

import java.util.concurrent.TimeUnit;

public interface Clock {
    long getTime();

    long getCurrentTime();

    long getTime(TimeUnit var1);

    TimeUnit getTimeUnit();
}
