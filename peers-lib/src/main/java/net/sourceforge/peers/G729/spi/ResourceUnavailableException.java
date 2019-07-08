package net.sourceforge.peers.G729.spi;

public class ResourceUnavailableException extends Exception {
    private static final long serialVersionUID = -7645219194855839093L;

    public ResourceUnavailableException(String message, InterruptedException e) {
        super(message);
    }

    public ResourceUnavailableException() {
    }

    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceUnavailableException(String message) {
        super(message);
    }

    public ResourceUnavailableException(Throwable cause) {
        super(cause);
    }
}
