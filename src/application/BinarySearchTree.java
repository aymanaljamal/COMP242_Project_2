package application;

import javafx.collections.ObservableList;

public class BinarySearchTree<A extends Comparable<A>> {
	private Stack<A> nextStack = new Stack<>();
	private Stack<A> prefStack = new Stack<>();
	private TNode<A> root;

	public TNode<A> getRoot() {
		return root;
	}

	public void setRoot(TNode<A> root) {
		this.root = root;
	}

	public BinarySearchTree() {
		root = null;
	}

	public BinarySearchTree(A data) {
		root = new TNode<>(data);
	}

	public void insert(A data) {
		if (isEmpty())
			root = new TNode<A>(data);
		else
			insert(data, root);
	}

	private void insert(A data, TNode<A> node) {
		if (data.compareTo(node.getData()) >= 0) { 
			if (!node.hasRight())
				node.setRight(new TNode<>(data));
			else
				insert(data, node.getRight());
		} else {
			if (!node.hasLeft())
				node.setLeft(new TNode<>(data));
			else
				insert(data, node.getLeft());
		}
	}

	public TNode<A> delete(A data) {
		TNode<A> current = root;
		TNode<A> parent = root;
		boolean isLeftChild = false;

		if (isEmpty())
			return null; 

		while (current != null && !current.getData().equals(data)) {
			parent = current;
			if (data.compareTo(current.getData()) < 0) {
				current = current.getLeft();
				isLeftChild = true;
			} else {
				current = current.getRight();
				isLeftChild = false;
			}
		}
		if (current == null)
			return null; 
		
		if (!current.hasLeft() && !current.hasRight()) {
			if (current == root)
				root = null;
			else {
				if (isLeftChild)
					parent.setLeft(null);
				else
					parent.setRight(null);
			}
		} 
		else if (current.hasLeft()) { 
			if (current == root) {
				root = current.getLeft();
			} else if (isLeftChild) {
				parent.setLeft(current.getLeft());
			} else {
				parent.setRight(current.getLeft());
			}
		} else if (current.hasRight()) {
			if (current == root) {
				root = current.getRight();
			} else if (isLeftChild) {
				parent.setLeft(current.getRight());
			} else {
				parent.setRight(current.getRight());
			}
		} 
		else {
			TNode<A> successor = getSuccessor(current);
			if (current == root)
				root = successor;
			else if (isLeftChild) {
				parent.setLeft(successor);
			} else {
				parent.setRight(successor);
			}
			successor.setLeft(current.getLeft());
		}

		return current;
	}

	private TNode<A> getSuccessor(TNode<A> node) {
		TNode<A> parentOfSuccessor = node;
		TNode<A> successor = node;
		TNode<A> current = node.getRight();
		while (current != null) {
			parentOfSuccessor = successor;
			successor = current;
			current = current.getLeft();
		}
		if (successor != node.getRight()) { 
			parentOfSuccessor.setLeft(successor.getRight());
			successor.setRight(node.getRight());
		}
		return successor;
	}

	public TNode<A> find(A data) {
		return find(data, root);
	}

	private TNode<A> find(A data, TNode<A> node) {
		if (node == null) {
			return null;
		}
		if (node.getData().equals(data)) {
			return node;
		}

		TNode<A> leftResult = find(data, node.getLeft());
		if (leftResult != null) {
			return leftResult;
		}
		return find(data, node.getRight());
	}

	public boolean isEmpty() {
		return root == null;
	}

	public TNode<A> largest() {
		return largest(root);
	}

	private TNode<A> largest(TNode<A> node) {
		if (node != null) {
			if (!node.hasRight())
				return node;
			return largest(node.getRight());
		}
		return null;
	}

	public TNode<A> smallest() {
		return smallest(root);
	}

	private TNode<A> smallest(TNode<A> node) {
		if (node != null) {
			if (!node.hasLeft())
				return node;
			return smallest(node.getLeft());
		}
		return null;
	}

	public int height() {
		return height(root);
	}

	private int height(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 1;
		int left = 0;
		int right = 0;
		if (node.hasLeft())
			left = height(node.getLeft());
		if (node.hasRight())
			right = height(node.getRight());
		return (left > right) ? (left + 1) : (right + 1);
	}

	public void traverseInOrder() {
		traverseInOrder(root);
	}

	private void traverseInOrder(TNode<A> node) {
		if (node != null) {
			if (node.getLeft() != null)
				traverseInOrder(node.getLeft());
			System.out.print(node + " ");
			if (node.getRight() != null)
				traverseInOrder(node.getRight());
		}
	}

	public void traversePreOrder() {
		traversePreOrder(root);
	}

	private void traversePreOrder(TNode<A> node) {
		if (node != null) {
			System.out.print(node + " ");
			if (node.getLeft() != null)
				traversePreOrder(node.getLeft());
			if (node.getRight() != null)
				traversePreOrder(node.getRight());
		}
	}

	public void traversePostOrder() {
		traversePostOrder(root);
	}

	private void traversePostOrder(TNode<A> node) {
		if (node != null) {
			if (node.getLeft() != null)
				traversePostOrder(node.getLeft());
			if (node.getRight() != null)
				traversePostOrder(node.getRight());
			System.out.print(node + " ");
		}
	}

	public TNode<A> search(A data) {
		return search(data, root);
	}

	private TNode<A> search(A data, TNode<A> node) {
		if (node != null) {
			int comp = node.getData().compareTo(data);
			if (comp == 0)
				return node;
			else if (comp > 0)
				return search(data, node.getLeft());
			else
				return search(data, node.getRight());
		}
		return null;
	}

	public int countLeaves(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 1;
		return countLeaves(node.getLeft()) + countLeaves(node.getRight());
	}

	public int countParents(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 0;
		return 1 + countParents(node.getLeft()) + countParents(node.getRight());
	}

	int size() {
		return countLeaves(root) + countParents(root);
	}

	public A getNext() {
		if (nextStack.isEmpty()) {
			return null;
		}
		Node<A> node = new Node<>(nextStack.pop());

		prefStack.push(node.getData());
		return node.getData();
	}

	public A getprev() {
		if (prefStack.isEmpty()) {
			return null;
		}
		Node<A> node = new Node<>(prefStack.pop());

		nextStack.push(node.getData());
		return node.getData();
	}

	public void StackFilling() {
		nextStack.clear();
		prefStack.clear();
		StackFilling(root);

	}

	private void StackFilling(TNode<A> node) {
		if (node != null) {
			if (node.getLeft() != null)
				StackFilling(node.getLeft());
			nextStack.push(node.getData());

			if (node.getRight() != null)
				StackFilling(node.getRight());

		}

	}

	public A getNextlevl() {
		if (nextStack.isEmpty()) {
			return null;
		}
		A data = nextStack.pop();
		prefStack.push(data);
		return data;
	}

	public A getprevlevl() {
		if (prefStack.isEmpty()) {
			return null;
		}
		A data = prefStack.pop();
		nextStack.push(data);
		return data;
	}

	public void StackFillingLevelByLevel() {
	    nextStack.clear();
	    prefStack.clear();
	    stackFillingLevelByLevel(root);
	}

	public void stackFillingLevelByLevel(TNode<A> rootNode) {
	    if (rootNode == null)
	        return;

	    LinkedListQueue<TNode<A>> queue = new LinkedListQueue<>();
	    queue.enqueue(rootNode);
	    Stack<A> tempStack = new Stack<>();

	    while (!queue.isEmpty()) {
	        TNode<A> currentNode = queue.dequeue();

	        if (currentNode.getLeft() != null) {
	            queue.enqueue(currentNode.getLeft());
	        }
	        if (currentNode.getRight() != null) {
	            queue.enqueue(currentNode.getRight());
	        }

	        tempStack.push(currentNode.getData());
	    }

	    while (!tempStack.isEmpty()) {
	        nextStack.push(tempStack.pop());
	    }
	}

	public void printLevelByLevel(TNode<A> root) {
        if (root == null) {
            System.out.println("The tree is empty");
            return;
        }

        LinkedListQueue<TNode<A>> queue = new LinkedListQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); 

            for (int i = 0; i < levelSize; i++) {
                TNode currentNode = queue.dequeue(); 

                System.out.print(currentNode.getData() + " ");

               
                if (currentNode.getLeft() != null) {
                    queue.enqueue(currentNode.getLeft());
                }
                if (currentNode.getRight() != null) {
                    queue.enqueue(currentNode.getRight());
                }
            }

            System.out.println();  
        }
    }
}
