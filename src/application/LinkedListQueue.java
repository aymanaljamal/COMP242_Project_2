package application;

public class LinkedListQueue<A extends Comparable<A>> {
	private Node<A> first;
	private Node<A> last;

	public Node<A> getLast() {
		return last;
	}

	public int size() {
	    int count = 0;
	    Node<A> current = first;
	    while (current != null) {
	        count++;
	        current = current.getNext();
	    }
	    return count;
	}


	public void setLast(Node<A> last) {
		this.last = last;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public void clear() {
		first = null;
		last = null;
	}

	public void enqueue(A data) {
		Node<A> newNode = new Node<>(data);
		if (isEmpty()) {
			first = newNode;
		} else {
			last.setNext(newNode);
		}
		last = newNode;
	}

	public A getFront() {
		if (isEmpty()) {
			System.out.println("Queue is empty");
			return null;
		} else {
			return first.getData();
		}
	}

	public A dequeue() {
		A front = getFront();
		if (!isEmpty()) {
			first = first.getNext();

		}
		if (first == null) {
			last = null;
		}
		return front;
	}

	@Override
	public String toString() {
		Node<A> current = first;
		String l = "";
		while (current != null) {
			l += current.getData() + ",";
			current = current.getNext();
		}
		return l;
	}

	public void traverse() {
		Node<A> current = first;
		String l = "";
		while (current != null) {
			l += current.getData() + ",";
			current = current.getNext();
		}
	}

}
