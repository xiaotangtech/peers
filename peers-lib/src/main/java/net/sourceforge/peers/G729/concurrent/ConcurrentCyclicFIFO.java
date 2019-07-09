package net.sourceforge.peers.G729.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCyclicFIFO<E> {
	private final AtomicInteger count = new AtomicInteger(0);
	private transient ConcurrentCyclicFIFO.Node<E> head;
	private transient ConcurrentCyclicFIFO.Node<E> last;
	private final ReentrantLock takeLock = new ReentrantLock();
	private final Condition notEmpty;
	private final ReentrantLock putLock;

	private void signalNotEmpty() {
		ReentrantLock takeLock = this.takeLock;
		takeLock.lock();

		try {
			this.notEmpty.signal();
		} finally {
			takeLock.unlock();
		}

	}

	private void insert(ConcurrentCyclicFIFO.Node<E> x) {
		this.last = this.last.next = x;
	}

	private ConcurrentCyclicFIFO.Node<E> extract() {
		ConcurrentCyclicFIFO.Node<E> current = this.head;
		this.head = this.head.next;
		current.item = this.head.item;
		this.head.item = null;
		return current;
	}

	public ConcurrentCyclicFIFO() {
		this.notEmpty = this.takeLock.newCondition();
		this.putLock = new ReentrantLock();
		this.last = this.head = new ConcurrentCyclicFIFO.Node((Object)null);
	}

	public int size() {
		return this.count.get();
	}

	public boolean offer(E e) {
		if (e == null) {
			throw new NullPointerException();
		} else {
			AtomicInteger count = this.count;
			boolean shouldSignal = false;
			ReentrantLock putLock = this.putLock;
			putLock.lock();

			try {
				this.insert(new ConcurrentCyclicFIFO.Node(e));
				shouldSignal = count.getAndIncrement() == 0;
			} finally {
				putLock.unlock();
			}

			if (shouldSignal) {
				this.signalNotEmpty();
			}

			return !shouldSignal;
		}
	}

	public E take() throws InterruptedException {
		AtomicInteger count = this.count;
		ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();

		ConcurrentCyclicFIFO.Node x;
		try {
			try {
				while(count.get() == 0) {
					this.notEmpty.await();
				}
			} catch (InterruptedException var8) {
				this.notEmpty.signal();
				throw var8;
			}

			x = this.extract();
			if (count.getAndDecrement() > 1) {
				this.notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}

		Object result = x.item;
		x.item = null;
		x.next = null;
		return (E) result;
	}

	public E poll() {
		AtomicInteger count = this.count;
		if (count.get() == 0) {
			return null;
		} else {
			ConcurrentCyclicFIFO.Node<E> x = null;
			ReentrantLock takeLock = this.takeLock;
			takeLock.lock();

			try {
				if (count.get() > 0) {
					x = this.extract();
					if (count.getAndDecrement() > 1) {
						this.notEmpty.signal();
					}
				}
			} finally {
				takeLock.unlock();
			}

			if (x != null) {
				E result = x.item;
				x.item = null;
				x.next = null;
				return result;
			} else {
				return null;
			}
		}
	}

	public void clear() {
		this.putLock.lock();
		this.takeLock.lock();

		try {
			this.head.next = null;

			assert this.head.item == null;

			this.last = this.head;
			this.count.set(0);
		} finally {
			this.takeLock.unlock();
			this.putLock.unlock();
		}

	}

	static class Node<E> {
		volatile E item;
		ConcurrentCyclicFIFO.Node<E> next;

		Node(E x) {
			this.item = x;
		}
	}
}
