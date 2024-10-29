package application;

public class TNode<A extends Comparable<A>> implements Comparable<TNode<A>> {
	private	A data;
	private	TNode left;
	private TNode right;

	public TNode(A data) {
		super();
		this.data = data;
	}

	public void setData(A data) {
		this.data = data;
	}

	public A getData() {
		return data;
	}

	public TNode getLeft() {
		return left;
	}

	public void setLeft(TNode left) {
		this.left = left;
	}

	public TNode getRight() {
		return right;
	}

	public void setRight(TNode right) {
		this.right = right;
	}

	public boolean isLeaf() {
		return (left == null && right == null);
	}

	public boolean hasLeft() {
		return left != null;
	}

	public boolean hasRight() {
		return right != null;
	}

	public String toString() {
		return "[" + data + "]";
	}

	@Override
	public int compareTo(TNode<A> o) {
		
		return 0;
	}
}