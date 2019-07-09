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
            ConnectionMode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ConnectionMode mode = var1[var3];
                if (mode.description.equalsIgnoreCase(description)) {
                    return mode;
                }
            }
        }

        throw new IllegalArgumentException("Unknown connection mode: " + description);
    }
}