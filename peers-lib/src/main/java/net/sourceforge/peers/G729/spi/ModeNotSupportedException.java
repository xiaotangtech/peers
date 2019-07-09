package net.sourceforge.peers.G729.spi;

public class ModeNotSupportedException extends Exception {
    private static final long serialVersionUID = -7812669275633259937L;
    private ConnectionMode mode;

    public ModeNotSupportedException(ConnectionMode mode) {
        this.mode = mode;
    }

    public ModeNotSupportedException(String msg) {
        super(msg);
    }

    public ConnectionMode getMode() {
        return this.mode;
    }
}