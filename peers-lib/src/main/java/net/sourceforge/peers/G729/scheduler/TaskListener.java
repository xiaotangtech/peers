package net.sourceforge.peers.G729.scheduler;

public interface TaskListener {
    /**
     * Called when task execution terminates normally
     */
    public void onTerminate();
    
    /**
     * Called when error has occurred.
     * 
     * @param e the error class
     */
    public void handlerError(Exception e);
}
