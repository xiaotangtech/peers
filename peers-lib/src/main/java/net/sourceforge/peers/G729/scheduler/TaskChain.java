package net.sourceforge.peers.G729.scheduler;

public class TaskChain implements TaskListener {
    private Task[] task;
    private int wi;
    private int i;
    private TaskChainListener listener;
    private final Object LOCK = new Object();
    private final Scheduler scheduler;

    public TaskChain(int length, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.task = new Task[length];
    }

    public void setListener(TaskChainListener listener) {
        this.listener = listener;
    }

    public void add(Task task) {
        Object var2 = this.LOCK;
        synchronized(this.LOCK) {
            task.setListener(this);
            this.task[this.wi] = task;
            ++this.wi;
        }
    }

    public void start() {
        this.i = 0;
        this.scheduler.submit(this.task[0]);
    }

    private void continueExecution() {
        ++this.i;
        if (this.i < this.task.length && this.task[this.i] != null) {
            this.scheduler.submit(this.task[this.i]);
        } else if (this.listener != null) {
            this.listener.onTermination();
        }

    }

    public void handlerError(Exception e) {
        if (this.listener != null) {
            this.listener.onException(e);
        }

    }

    protected Task[] getTasks() {
        return this.task;
    }

    public void clean() {
        this.wi = 0;
    }

    public void onTerminate() {
        this.continueExecution();
    }
}

