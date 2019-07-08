package net.sourceforge.peers.g729.codec;

public class IntegerPointer {
    public Integer value = null;

    public IntegerPointer(Integer v) {
        this.value = v;
    }

    public IntegerPointer() {
    }

    public void setValue(Integer a) {
        this.value = a;
    }
}