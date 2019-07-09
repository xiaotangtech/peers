package net.sourceforge.peers.G729.scheduler;

public interface TaskChainListener {
    /**
     * Called when task has been executed successfully
     */
    public void onTermination();
    
    /**
     * Called when task has been failed.
     * @param e 
     */
    public void onException(Exception e);
}
