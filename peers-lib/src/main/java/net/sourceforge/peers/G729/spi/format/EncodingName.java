package net.sourceforge.peers.G729.spi.format;

import net.sourceforge.peers.G729.spi.utils.Text;

public class EncodingName extends Text implements Cloneable {
    public EncodingName() {
    }

    public EncodingName(Text text) {
        byte[] newArray = new byte[text.length()];
        this.strain(newArray, 0, this.length());
        text.duplicate(this);
    }

    public EncodingName(String s) {
        super(s);
    }

    protected EncodingName clone() {
        byte[] newArray = new byte[this.length()];
        Text t = new Text();
        t.strain(newArray, 0, this.length());
        this.duplicate(t);
        return new EncodingName(t);
    }
}