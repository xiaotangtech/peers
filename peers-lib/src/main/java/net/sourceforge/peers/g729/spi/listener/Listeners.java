package net.sourceforge.peers.g729.spi.listener;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Listeners<L extends Listener> {
    private ArrayList<Listener> list = new ArrayList();
    private ArrayList<Listener> processingList = new ArrayList();
    private Semaphore accessSemaphore = new Semaphore(1);

    public Listeners() {
    }

    public void add(L listener) throws TooManyListenersException {
        try {
            this.accessSemaphore.acquire();
        } catch (InterruptedException var3) {
            ;
        }

        this.list.add(listener);
        this.accessSemaphore.release();
    }

    public void remove(L listener) {
        try {
            this.accessSemaphore.acquire();
        } catch (InterruptedException var3) {
            ;
        }

        this.list.remove(listener);
        this.accessSemaphore.release();
    }

    public void clear() {
        try {
            this.accessSemaphore.acquire();
        } catch (InterruptedException var2) {
            ;
        }

        this.list.clear();
        this.accessSemaphore.release();
    }

    public boolean dispatch(Event event) {
        try {
            this.accessSemaphore.acquire();
        } catch (InterruptedException var4) {
            ;
        }

        this.processingList.clear();
        this.processingList.addAll(this.list);
        this.accessSemaphore.release();
        boolean res = this.processingList.size() != 0;

        for(int i = 0; i < this.processingList.size(); ++i) {
            ((Listener)this.processingList.get(i)).process(event);
        }

        return res;
    }
}