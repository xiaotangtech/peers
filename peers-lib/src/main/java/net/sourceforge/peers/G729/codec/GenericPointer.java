package net.sourceforge.peers.G729.codec;

public class GenericPointer<T> {
    public T value = null;

    public GenericPointer(T v) {
        this.value = v;
    }

    public GenericPointer() {
    }

    public void setValue(T a) {
        this.value = a;
    }
}