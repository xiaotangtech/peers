package net.sourceforge.peers.G729.concurrent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMap<E> extends ConcurrentHashMap<Integer, E> {
	private static final long serialVersionUID = 8270100031373807057L;

	public ConcurrentMap() {
	}

	public Iterator<Integer> keysIterator() {
		return (Iterator)this.keys();
	}

	public Iterator<E> valuesIterator() {
		return (Iterator)this.elements();
	}
}