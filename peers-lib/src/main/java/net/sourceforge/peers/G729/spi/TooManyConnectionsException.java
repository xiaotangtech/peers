package net.sourceforge.peers.G729.spi;

public class TooManyConnectionsException extends Exception {
    private static final long serialVersionUID = -7814746502352762934L;

    public TooManyConnectionsException() {
    }

    public TooManyConnectionsException(String msg) {
        super(msg);
    }
}