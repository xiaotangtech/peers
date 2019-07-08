package net.sourceforge.peers.g729.spi.listener;

public class TooManyListenersException extends Exception {
    private static final long serialVersionUID = -4275521555834796836L;

    public TooManyListenersException() {
    }

    public TooManyListenersException(String msg) {
        super(msg);
    }
}