package net.sourceforge.peers.g729.spi;

public enum Valve {
    CLOSE(0, "close"),
    OPEN(1, "open");

    private int code;
    private String name;

    private Valve(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Valve getInstance(String name) {
        if (name.equalsIgnoreCase("close")) {
            return CLOSE;
        } else if (name.equalsIgnoreCase("open")) {
            return OPEN;
        } else {
            throw new IllegalArgumentException("There is no Valve for: " + name);
        }
    }

    public static Valve getValve(int code) {
        return code == 0 ? CLOSE : OPEN;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}