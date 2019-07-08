package net.sourceforge.peers.G729.spi;

public enum ConnectionState {
    NULL(0, "NULL", -1L),
    CREATING(1, "CREATING", 5L),
    HALF_OPEN(2, "HALF_OPEN", 300L),
    OPENING(3, "OPENING", 5L),
    OPEN(4, "OPEN", 14400L),
    CLOSING(5, "CLOSING", 5L);

    private int code;
    private String stateName;
    private long timeout;

    private ConnectionState(int code, String stateName, long timeout) {
        this.stateName = stateName;
        this.code = code;
        this.timeout = timeout;
    }

    public static ConnectionState getInstance(String name) {
        if (name.equalsIgnoreCase("NULL")) {
            return NULL;
        } else if (name.equalsIgnoreCase("HALF_OPEN")) {
            return HALF_OPEN;
        } else if (name.equalsIgnoreCase("OPEN")) {
            return OPEN;
        } else if (name.equalsIgnoreCase("CREATING")) {
            return CREATING;
        } else if (name.equalsIgnoreCase("OPENING")) {
            return OPENING;
        } else {
            return name.equalsIgnoreCase("CLOSING") ? CLOSING : null;
        }
    }

    public String toString() {
        return this.stateName;
    }

    public int getCode() {
        return this.code;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}