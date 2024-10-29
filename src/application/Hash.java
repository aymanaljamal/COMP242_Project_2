package application;

import java.util.Arrays;

public class Hash<A extends Comparable<A>> implements Comparable<A> {
    static final char EMPTY = 'E';
    static final char FILLED = 'F';
     static final char DELETED = 'D';

    private HNode<A>[] hashTable;
    private int size;
    private int elementCount;

    public Hash(int n) {
        this.size = nextPrime(n * 2);
        this.hashTable = (HNode<A>[]) new HNode[this.size];
        this.elementCount = 0;
    }

    public void add(MartyrDate data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null && hashTable[index].getFlag() == FILLED
                && !hashTable[index].getData().equals(data)) {
            index = probe(index, ++i);
        }

        if (hashTable[index] == null || hashTable[index].getFlag() == EMPTY || hashTable[index].getFlag() == DELETED) {
            hashTable[index] = new HNode<>((A) data);
            hashTable[index].setFlag(FILLED);
            elementCount++;
            if ((double) elementCount / hashTable.length >= 0.5) {
                rehash();
            }
        }
    }

    public void add(Martyr data) {
        MartyrDate martyrDate = new MartyrDate(data.getDate());
        int index = hash(martyrDate);
        int i = 0;

        while (hashTable[index] != null && hashTable[index].getFlag() == FILLED
                && !hashTable[index].getData().equals(martyrDate)) {
            index = probe(index, ++i);
        }

        if (hashTable[index] == null || hashTable[index].getFlag() == EMPTY || hashTable[index].getFlag() == DELETED) {
            hashTable[index] = new HNode<>((A) martyrDate);
            hashTable[index].setFlag(FILLED);
            elementCount++;
            if ((double) elementCount / hashTable.length >= 0.5) {
                rehash();
            }
        }
        ((MartyrDate) hashTable[index].getData()).getMartyrsAVL().insert(data);
    }

    public TNode<Martyr> findMartyr(Martyr data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() == FILLED && hashTable[index].getData().equals(new MartyrDate(data.getDate()))) {
                return ((MartyrDate) hashTable[index].getData()).getMartyrsAVL().find(data);
            }
            index = probe(index, ++i);
        }
        return null;
    }

    public MartyrDate findMartyrDate(MartyrDate data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() == FILLED && hashTable[index].getData().equals(data)) {
                return (MartyrDate) hashTable[index].getData();
            }
            index = probe(index, ++i);
        }
        return null;
    }

    public boolean delete(Martyr data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() == FILLED && hashTable[index].getData().equals(new MartyrDate(data.getDate()))) {
                if (((MartyrDate) hashTable[index].getData()).getMartyrsAVL().delete(data)) {
                    if (((MartyrDate) hashTable[index].getData()).getMartyrsAVL().isEmpty()) {
                        hashTable[index].setFlag(DELETED);
                        hashTable[index].setData(null);
                    }
                    return true;
                }
            }
            index = probe(index, ++i);
        }
        return false;
    }

    public boolean delete(MartyrDate data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() == FILLED && hashTable[index].getData().equals(data)) {
                hashTable[index].setFlag(DELETED);
                hashTable[index].setData(null);
                return true;
            }
            index = probe(index, ++i);
        }
        return false;
    }

    private void rehash() {
        HNode<A>[] oldTable = hashTable;
        int newSize = nextPrime(2 * oldTable.length);
        hashTable = (HNode<A>[]) new HNode[newSize];
        size = newSize;
        elementCount = 0;

        for (HNode<A> node : oldTable) {
            if (node != null && node.getFlag() == FILLED) {
                rehashTree(((MartyrDate) node.getData()).getMartyrsAVL().getRoot());
            }
        }
    }

    private void rehashTree(TNode<Martyr> node) {
        if (node == null) return;
        rehashTree(node.left);
        add(node.getData());
        rehashTree(node.right);
    }

    private int nextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "HashTable [hashTable=" + Arrays.toString(hashTable) + "]";
    }

    public void traverse() {
        for (HNode<A> node : hashTable) {
            if (node != null && node.getFlag() == FILLED) {
                traverseAVL(((MartyrDate) node.getData()).getMartyrsAVL().getRoot());
            }
        }
    }

    private void traverseAVL(TNode<Martyr> node) {
        if (node == null) return;
        traverseAVL(node.left);
        System.out.println(node.getData());
        traverseAVL(node.right);
    }

    private int hash(Martyr data) {
        return Math.abs(data.hashCode() % size);
    }

    private int hash(MartyrDate data) {
        return Math.abs(data.hashCode() % size);
    }

    private int probe(int hash, int i) {
        return (hash + i * i) % size;
    }

    public HNode<A>[] getHashTable() {
        return hashTable;
    }

    public void setHashTable(HNode<A>[] hashTable) {
        this.hashTable = hashTable;
    }

    @Override
    public int compareTo(A o) {
        // Implement the comparison logic as per your requirements.
        return 0;
    }
}
