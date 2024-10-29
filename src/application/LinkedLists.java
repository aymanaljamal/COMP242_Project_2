package application;

public class LinkedLists<A extends Comparable<A>> {
    private Node<A> head;
    private Node<A> tail;

    public Node<A> getHead() {
		return head;
	}

	public void setHead(Node<A> head) {
		this.head = head;
	}

	public Node<A> getTail() {
		return tail;
	}

	public void setTail(Node<A> tail) {
		this.tail = tail;
	}

	public void insert(A data) {
        Node<A> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            Node<A> prev = null, curr = head;
            for (; curr != null && curr.getData().compareTo(data) < 0; prev = curr, curr = curr.getNext());

            if (prev == null) {
                newNode.setNext(head);
                head = newNode;
            } else {
                newNode.setNext(curr);
                prev.setNext(newNode);
                if (curr == null) {
                    tail = newNode; 
                }
            }
        }
    }

    public int length() {
        Node<A> current = head;
        int count = 0;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    public Node<A> delete(A data) {
        if (head == null) return null;

        Node<A> prev = null, curr = head;
        while (curr != null && curr.getData().compareTo(data) < 0) {
            prev = curr;
            curr = curr.getNext();
        }

        if (curr != null && curr.getData().compareTo(data) == 0) {
            if (prev == null) {
                head = curr.getNext();
            } else {
                prev.setNext(curr.getNext());
            }
            if (curr == tail) {
                tail = prev;
            }
            return curr;
        }
        return null;
    }

    public Node<A> find(A data) {
        Node<A> curr = head;
       
        while (curr != null && !curr.getData().equals(data)) {
            curr = curr.getNext();
        }

       
        return curr;
    }

    public void traverse() {
        Node<A> curr = head;
        System.out.print("head --> ");
        while (curr != null) {
            System.out.print(curr.getData() + " --> ");
            curr = curr.getNext();
        }
        System.out.println("null");
    }

   
    @Override
    public String toString() {
        StringBuilder toStringLinked = new StringBuilder("head -> ");
        Node<A> current = head;
        while (current != null) {
            toStringLinked.append(current.getData());
            if (current.getNext() != null) {
                toStringLinked.append(" -> ");
            }
            current = current.getNext();
        }
        return toStringLinked.append(" -> null").toString();
    }
}
