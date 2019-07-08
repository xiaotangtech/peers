package net.sourceforge.peers.g729.spi.format;

public class ApplicationFormat extends Format implements Cloneable {
    protected ApplicationFormat(EncodingName name) {
        super(name);
    }

    protected ApplicationFormat(String name) {
        super(new EncodingName(name));
    }

    public ApplicationFormat clone() {
        return new ApplicationFormat(this.getName().clone());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApplicationFormat[").append(this.getName().toString()).append("]");
        return builder.toString();
    }
}