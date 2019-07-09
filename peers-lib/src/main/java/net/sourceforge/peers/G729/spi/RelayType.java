package net.sourceforge.peers.G729.spi;

public enum RelayType {
    MIXER,
    SPLITTER;

    private RelayType() {
    }

    public static final RelayType fromName(String name) {
        RelayType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            RelayType relayType = var1[var3];
            if (relayType.name().equalsIgnoreCase(name)) {
                return relayType;
            }
        }

        throw new IllegalArgumentException("Unknown relay type " + name);
    }
}