package net.sourceforge.peers.G729.scheduler;



import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceScheduler implements Scheduler {
//    private static final Logger LOGGER = LogManager.getLogger(ServiceScheduler.class);
    public static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private volatile boolean started;
    private final Clock wallClock;
    private ScheduledExecutorService executor;
    private final ThreadFactory threadFactory;

    public ServiceScheduler(Clock wallClock) {
        this.threadFactory = new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "service-scheduler-" + this.index.incrementAndGet());
            }
        };
        this.started = false;
        this.wallClock = new WallClock();
    }

    public ServiceScheduler() {
        this(new WallClock());
    }

    public Clock getWallClock() {
        return this.wallClock;
    }

    public Future<?> submit(Runnable task) throws RejectedExecutionException {
        if (!this.started) {
            throw new RejectedExecutionException("Scheduler is not running.");
        } else {
            return this.executor.submit(task);
        }
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) throws RejectedExecutionException {
        if (!this.started) {
            throw new RejectedExecutionException("Scheduler is not running.");
        } else {
            return this.executor.schedule(task, delay, unit);
        }
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) throws IllegalArgumentException, RejectedExecutionException {
        if (!this.started) {
            throw new RejectedExecutionException("Scheduler is not running.");
        } else {
            return this.executor.scheduleWithFixedDelay(task, initialDelay, period, unit);
        }
    }

    public void start() {
        if (!this.started) {
            this.started = true;
            this.executor = Executors.newScheduledThreadPool(POOL_SIZE, this.threadFactory);
            ((ScheduledThreadPoolExecutor)this.executor).setRemoveOnCancelPolicy(true);
            ((ScheduledThreadPoolExecutor)this.executor).prestartAllCoreThreads();
//            LOGGER.info("Started scheduler!");
        }

    }

    public void stop() {
        if (this.started) {
            this.started = false;
            this.executor.shutdownNow();
//            LOGGER.info("Stopped scheduler!");
        }

    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.executor.awaitTermination(timeout, unit);
    }
}
