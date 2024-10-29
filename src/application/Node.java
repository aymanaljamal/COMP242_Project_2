package application;

public class Node<A extends Comparable<A>> implements Comparable<Node<A>>{
	private A data;
	private Node next;

	public Node(A data) {
		super();
		this.data = data;
	}

	public A getData() {
		return data;
	}

	public void setData(A data) {
		this.data = data;
	}

	public Node<A> getNext() {
		return next;
	}

	public void setNext(Node<A> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return ""+ data ;
	}

	@Override
	public int compareTo(Node<A> o) {
		
		return 0;
	}

}
