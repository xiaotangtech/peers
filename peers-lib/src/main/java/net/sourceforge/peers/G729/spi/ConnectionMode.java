package net.sourceforge.peers.G729.spi;

public enum ConnectionMode {
    INACTIVE("inactive"),
    SEND_ONLY("sendonly"),
    RECV_ONLY("recvonly"),
    SEND_RECV("sendrecv"),
    CONFERENCE("confrnce"),
    NETWORK_LOOPBACK("netwloop"),
    LOOPBACK("loopback"),
    CONTINUITY_TEST("conttest"),
    NETWORK_CONTINUITY_TEST("netwtest");

    private final String description;

    private ConnectionMode(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public static final ConnectionMode fromDescription(String description) {
        if (description != null && !description.isEmpty()) {
            ConnectionMode[] arr$ = values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                ConnectionMode mode = arr$[i$];
                if (mode.description.equalsIgnoreCase(description)) {
                    return mode;
                }
            }
        }

        throw new IllegalArgumentException("Unknown connection mode: " + description);
    }
}