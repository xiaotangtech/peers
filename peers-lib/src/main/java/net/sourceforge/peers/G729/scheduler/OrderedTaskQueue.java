package net.sourceforge.peers.G729.scheduler;


import net.sourceforge.peers.G729.concurrent.ConcurrentCyclicFIFO;

public class OrderedTaskQueue {
	private ConcurrentCyclicFIFO<Task>[] taskList = new ConcurrentCyclicFIFO[2];
	private Integer activeIndex = 0;

	public OrderedTaskQueue() {
		this.taskList[0] = new ConcurrentCyclicFIFO();
		this.taskList[1] = new ConcurrentCyclicFIFO();
	}

	public void accept(Task task) {
		if ((this.activeIndex + 1) % 2 == 0) {
			if (!task.isInQueue0()) {
				this.taskList[0].offer(task);
				task.storedInQueue0();
			}
		} else if (!task.isInQueue1()) {
			this.taskList[1].offer(task);
			task.storedInQueue1();
		}

	}

	public Task poll() {
		Task result = null;
		if (this.activeIndex == 0) {
			result = (Task)this.taskList[0].poll();
			if (result != null) {
				result.removeFromQueue0();
			}
		} else {
			result = (Task)this.taskList[1].poll();
			if (result != null) {
				result.removeFromQueue1();
			}
		}

		return result;
	}

	public void changePool() {
		this.activeIndex = (this.activeIndex + 1) % 2;
	}

	public void clear() {
		this.taskList[0].clear();
		this.taskList[1].clear();
	}

	public int size() {
		return this.taskList[this.activeIndex].size();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Queue[");
		int len = Math.min(30, this.taskList[this.activeIndex].size());

		for(int i = 0; i < len - 1; ++i) {
			sb.append(",");
		}

		sb.append("]");
		return sb.toString();
	}
}
