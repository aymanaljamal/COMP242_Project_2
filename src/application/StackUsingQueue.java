package application;

public class StackUsingQueue<A extends Comparable<A>> {
	private LinkedListQueue<A> queue1;
	private LinkedListQueue<A> queue2;

	public StackUsingQueue() {
		queue1 = new LinkedListQueue<>();
		queue2 = new LinkedListQueue<>();
	}

	public void push(A data) {
		queue1.enqueue(data);
	}

	public A pop() {
		if (queue1.isEmpty()) {
			return null;
		}
		while (queue1.size() > 1) {
			queue2.enqueue(queue1.dequeue());
		}
		A data = queue1.dequeue();
		LinkedListQueue<A> temp = queue1;
		queue1 = queue2;
		queue2 = temp;
		return data;
	}

	public A peek() {
		if (queue1.isEmpty()) {
			return null;
		}
		while (queue1.size() > 1) {
			queue2.enqueue(queue1.dequeue());
		}
		A data = queue1.dequeue();
		queue2.enqueue(data);
		LinkedListQueue<A> temp = queue1;
		queue1 = queue2;
		queue2 = temp;
		return data;
	}

	public boolean isEmpty() {
		return queue1.isEmpty();
	}

	public String toString() {
		return queue1.toString();
	}

	public void traverse() {
		LinkedListQueue<A> temp = new LinkedListQueue<>();
		while (!queue1.isEmpty()) {
			A data = queue1.dequeue();
			System.out.print(data + " ");
			temp.enqueue(data);
		}
		System.out.println();
		queue1 = temp;
	}

	public void clear() {
		queue1.clear();
		queue2.clear();
	}
}
