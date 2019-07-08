package net.sourceforge.peers.G729.spi.format;

import java.util.ArrayList;
import java.util.Iterator;

public class Formats {
    public static final int DEFAULT_SIZE = 15;
    private ArrayList<Format> list;

    public Formats() {
        this.list = new ArrayList(15);
    }

    public Formats(int size) {
        this.list = new ArrayList(size);
    }

    public void add(Format format) {
        this.list.add(format);
    }

    public void addAll(Formats other) {
        this.list.addAll(other.list);
    }

    public void remove(Format format) {
        this.list.remove(format);
    }

    public Format get(int i) {
        return (Format)this.list.get(i);
    }

    public boolean contains(Format format) {
        Iterator i$ = this.list.iterator();

        Format f;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            f = (Format)i$.next();
        } while(!f.matches(format));

        return true;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public void clean() {
        this.list.clear();
    }

    public void intersection(Formats other, Formats intersection) {
        intersection.list.clear();
        Iterator iterator = this.list.iterator();

        while(iterator.hasNext()) {
            Format f1 = (Format)iterator.next();
            Iterator iterator1 = other.list.iterator();

            while(iterator1.hasNext()) {
                Format f2 = (Format)iterator1.next();
                if (f1.matches(f2)) {
                    intersection.list.add(f2);
                }
            }
        }

    }

    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("Formats{");

        for(int i = 0; i < this.list.size(); ++i) {
            buff.append(this.list.get(i));
            if (i != this.list.size() - 1) {
                buff.append(",");
            }
        }

        buff.append("}");
        return buff.toString();
    }
}