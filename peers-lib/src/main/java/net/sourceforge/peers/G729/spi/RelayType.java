package net.sourceforge.peers.G729.spi;

public enum RelayType {
    MIXER,
    SPLITTER;

    private RelayType() {
    }

    public static final RelayType fromName(String name) {
        RelayType[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            RelayType relayType = arr$[i$];
            if (relayType.name().equalsIgnoreCase(name)) {
                return relayType;
            }
        }

        throw new IllegalArgumentException("Unknown relay type " + name);
    }
}