package net.sourceforge.peers.G729.spi.format;

import net.sourceforge.peers.G729.spi.utils.Text;

public class Format implements Cloneable {
    private EncodingName name;
    private Text options;
    private Boolean sendPTime = false;

    public Format(EncodingName name) {
        this.name = name;
    }

    public EncodingName getName() {
        return this.name;
    }

    public void setName(EncodingName name) {
        this.name = name;
    }

    public Text getOptions() {
        return this.options;
    }

    public void setOptions(Text options) {
        this.options = options;
    }

    public String toString() {
        return this.name.toString();
    }

    public boolean matches(Format fmt) {
        return this.name.equals(fmt.name);
    }

    public boolean shouldSendPTime() {
        return this.sendPTime;
    }

    public void setSendPTime(Boolean newValue) {
        this.sendPTime = newValue;
    }

    public Format clone() {
        return null;
    }
}