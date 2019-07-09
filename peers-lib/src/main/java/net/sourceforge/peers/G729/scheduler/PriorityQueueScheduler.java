package net.sourceforge.peers.G729.scheduler;

import net.sourceforge.peers.G729.concurrent.ConcurrentCyclicFIFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class PriorityQueueScheduler {
    public static final Integer RECEIVER_QUEUE = 0;
    public static final Integer SENDER_QUEUE = 1;
    public static final Integer MANAGEMENT_QUEUE = 2;
    public static final Integer UDP_MANAGER_QUEUE = 3;
    public static final Integer INPUT_QUEUE = 4;
    public static final Integer MIXER_MIX_QUEUE = 5;
    public static final Integer OUTPUT_QUEUE = 6;
    public static final Integer HEARTBEAT_QUEUE = -1;
    private Clock clock;
    protected OrderedTaskQueue[] taskQueues;
    protected OrderedTaskQueue[] heartBeatQueue;
    private PriorityQueueScheduler.CoreThread coreThread;
    private PriorityQueueScheduler.CriticalThread criticalThread;
    private boolean isActive;
    private Logger logger;
    private ConcurrentCyclicFIFO<Task> waitingTasks;
    private ConcurrentCyclicFIFO<Task> criticalTasks;
    private PriorityQueueScheduler.WorkerThread[] workerThreads;
    private PriorityQueueScheduler.CriticalWorkerThread[] criticalWorkerThreads;

    public PriorityQueueScheduler(Clock clock) {
        this.taskQueues = new OrderedTaskQueue[7];
        this.heartBeatQueue = new OrderedTaskQueue[5];
        this.logger = LogManager.getLogger(PriorityQueueScheduler.class);
        this.waitingTasks = new ConcurrentCyclicFIFO();
        this.criticalTasks = new ConcurrentCyclicFIFO();
        this.clock = clock;

        int i;
        for(i = 0; i < this.taskQueues.length; ++i) {
            this.taskQueues[i] = new OrderedTaskQueue();
        }

        for(i = 0; i < this.heartBeatQueue.length; ++i) {
            this.heartBeatQueue[i] = new OrderedTaskQueue();
        }

        this.coreThread = new PriorityQueueScheduler.CoreThread("scheduler-core");
        this.criticalThread = new PriorityQueueScheduler.CriticalThread("scheduler-critical");
        this.workerThreads = new PriorityQueueScheduler.WorkerThread[Runtime.getRuntime().availableProcessors() * 2];
        this.criticalWorkerThreads = new PriorityQueueScheduler.CriticalWorkerThread[Runtime.getRuntime().availableProcessors() / 2];

        for(i = 0; i < this.workerThreads.length; ++i) {
            this.workerThreads[i] = new PriorityQueueScheduler.WorkerThread("scheduler-worker-" + i);
        }

        for(i = 0; i < this.criticalWorkerThreads.length; ++i) {
            this.criticalWorkerThreads[i] = new PriorityQueueScheduler.CriticalWorkerThread("scheduler-critical-worker-" + i);
        }

    }

    public PriorityQueueScheduler() {
        this((Clock)null);
    }

    public int getPoolSize() {
        return this.workerThreads.length;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return this.clock;
    }

    public void submit(Task task, Integer index) {
        task.activate(false);
        this.taskQueues[index].accept(task);
    }

    public void submitHeatbeat(Task task) {
        task.activate(true);
        this.heartBeatQueue[this.coreThread.runIndex].accept(task);
    }

    public void submit(TaskChain taskChain) {
        taskChain.start();
    }

    public void start() {
        if (!this.isActive) {
            if (this.clock == null) {
                throw new IllegalStateException("Clock is not set");
            } else {
                this.isActive = true;
                this.logger.info("Starting ");
                this.coreThread.activate();
                this.criticalThread.activate();

                int i;
                for(i = 0; i < this.workerThreads.length; ++i) {
                    this.workerThreads[i].activate();
                }

                for(i = 0; i < this.criticalWorkerThreads.length; ++i) {
                    this.criticalWorkerThreads[i].activate();
                }

                this.logger.info("Started ");
            }
        }
    }

    public void stop() {
        if (this.isActive) {
            this.coreThread.shutdown();
            this.criticalThread.shutdown();

            int i;
            for(i = 0; i < this.workerThreads.length; ++i) {
                this.workerThreads[i].shutdown();
            }

            for(i = 0; i < this.criticalWorkerThreads.length; ++i) {
                this.criticalWorkerThreads[i].shutdown();
            }

            try {
                Thread.sleep(40L);
            } catch (InterruptedException var2) {
                ;
            }

            for(i = 0; i < this.taskQueues.length; ++i) {
                this.taskQueues[i].clear();
            }

            for(i = 0; i < this.heartBeatQueue.length; ++i) {
                this.heartBeatQueue[i].clear();
            }

        }
    }

    public double getMissRate() {
        return 0.0D;
    }

    public long getWorstExecutionTime() {
        return 0L;
    }

    private class CriticalWorkerThread extends Thread {
        private volatile boolean active;
        private Task current;

        public CriticalWorkerThread(String name) {
            super(name);
        }

        public void run() {
            while(this.active) {
                this.current = null;

                while(this.current == null) {
                    try {
                        this.current = (Task)PriorityQueueScheduler.this.criticalTasks.take();
                    } catch (Exception var2) {
                        ;
                    }
                }

                this.current.run();
                PriorityQueueScheduler.this.criticalThread.notifyCompletion();
            }

        }

        public void activate() {
            this.active = true;
            this.start();
        }

        private void shutdown() {
            this.active = false;
        }
    }

    private class WorkerThread extends Thread {
        private volatile boolean active;
        private Task current;

        public WorkerThread(String name) {
            super(name);
        }

        public void run() {
            while(this.active) {
                this.current = null;

                while(this.current == null) {
                    try {
                        this.current = (Task)PriorityQueueScheduler.this.waitingTasks.take();
                    } catch (Exception var2) {
                        PriorityQueueScheduler.this.logger.warn("Could not poll waiting task in timely fashion. Will keep trying.");
                    }
                }

                this.current.run();
                PriorityQueueScheduler.this.coreThread.notifyCompletion();
            }

        }

        public void activate() {
            this.active = true;
            this.start();
        }

        private void shutdown() {
            this.active = false;
        }
    }

    private class CriticalThread extends Thread {
        private volatile boolean active;
        private AtomicInteger activeTasksCount = new AtomicInteger();
        private long cycleStart = 0L;
        private Object LOCK = new Object();

        public CriticalThread(String name) {
            super(name);
        }

        public void activate() {
            this.active = true;
            this.cycleStart = PriorityQueueScheduler.this.clock.getTime();
            this.start();
        }

        public void notifyCompletion() {
            if (this.activeTasksCount.decrementAndGet() == 0) {
                LockSupport.unpark(PriorityQueueScheduler.this.criticalThread);
            }

        }

        public void run() {
            for(; this.active; this.cycleStart += 4000000L) {
                this.executeQueue(PriorityQueueScheduler.this.taskQueues[PriorityQueueScheduler.RECEIVER_QUEUE]);

                while(this.activeTasksCount.get() != 0) {
                    LockSupport.park();
                }

                this.executeQueue(PriorityQueueScheduler.this.taskQueues[PriorityQueueScheduler.SENDER_QUEUE]);

                while(this.activeTasksCount.get() != 0) {
                    LockSupport.park();
                }

                long cycleDuration = PriorityQueueScheduler.this.clock.getTime() - this.cycleStart;
                if (cycleDuration < 4000000L) {
                    try {
                        sleep(4L - cycleDuration / 1000000L, (int)((4000000L - cycleDuration) % 1000000L));
                    } catch (InterruptedException var4) {
                        ;
                    }
                }
            }

        }

        private void executeQueue(OrderedTaskQueue currQueue) {
            currQueue.changePool();

            for(Task t = currQueue.poll(); t != null; t = currQueue.poll()) {
                this.activeTasksCount.incrementAndGet();
                PriorityQueueScheduler.this.criticalTasks.offer(t);
            }

        }

        private void shutdown() {
            this.active = false;
        }
    }

    private class CoreThread extends Thread {
        private volatile boolean active;
        private int currQueue;
        private AtomicInteger activeTasksCount;
        private long cycleStart;
        private int runIndex;
        private Object LOCK;

        public CoreThread(String name) {
            super(name);
            this.currQueue = PriorityQueueScheduler.UDP_MANAGER_QUEUE;
            this.activeTasksCount = new AtomicInteger();
            this.cycleStart = 0L;
            this.runIndex = 0;
            this.LOCK = new Object();
        }

        public void activate() {
            this.active = true;
            this.start();
        }

        public void notifyCompletion() {
            if (this.activeTasksCount.decrementAndGet() == 0) {
                LockSupport.unpark(PriorityQueueScheduler.this.coreThread);
            }

        }

        public void run() {
            for(this.cycleStart = PriorityQueueScheduler.this.clock.getTime(); this.active; this.cycleStart += 20000000L) {
                long taskStart = this.cycleStart;

                for(this.currQueue = PriorityQueueScheduler.MANAGEMENT_QUEUE; this.currQueue <= PriorityQueueScheduler.OUTPUT_QUEUE; ++this.currQueue) {
                    this.executeQueue(PriorityQueueScheduler.this.taskQueues[this.currQueue]);

                    while(this.activeTasksCount.get() != 0) {
                        LockSupport.park();
                    }
                }

                this.executeQueue(PriorityQueueScheduler.this.taskQueues[PriorityQueueScheduler.MANAGEMENT_QUEUE]);

                while(this.activeTasksCount.get() != 0) {
                    LockSupport.park();
                }

                this.runIndex = (this.runIndex + 1) % 5;
                this.executeQueue(PriorityQueueScheduler.this.heartBeatQueue[this.runIndex]);

                while(this.activeTasksCount.get() != 0) {
                    LockSupport.park();
                }

                this.executeQueue(PriorityQueueScheduler.this.taskQueues[PriorityQueueScheduler.MANAGEMENT_QUEUE]);

                while(this.activeTasksCount.get() != 0) {
                    LockSupport.park();
                }

                long cycleDuration = PriorityQueueScheduler.this.clock.getTime() - this.cycleStart;
                if (cycleDuration < 20000000L) {
                    try {
                        sleep(20L - cycleDuration / 1000000L, (int)((20000000L - cycleDuration) % 1000000L));
                    } catch (InterruptedException var6) {
                        ;
                    }
                }
            }

        }

        private void executeQueue(OrderedTaskQueue currQueue) {
            currQueue.changePool();

            for(Task t = currQueue.poll(); t != null; t = currQueue.poll()) {
                this.activeTasksCount.incrementAndGet();
                PriorityQueueScheduler.this.waitingTasks.offer(t);
            }

        }

        private void shutdown() {
            this.active = false;
        }
    }
}
