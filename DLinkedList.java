import java.util.Comparator;

/**
 * Created by Casey on 8/10/2016.
 */

public class DLinkedList {

    private class Node {
        private Object data;
        private Node next, prev;
        private Node(Object data, Node prev, Node next)
        {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public DLinkedList() {
        this.head = new Node(null, null, null );
        this.head.next = this.head;
        this.head.prev=this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.head.next;
    }

    public void insertionaSort(){
        Node lastSorted, sortedWalker;
        Object firstUnsorted;
        for(lastSorted = this.head.next; lastSorted != this.head.prev; lastSorted = lastSorted.next){
            firstUnsorted = lastSorted.next.data;
            for(sortedWalker = lastSorted; sortedWalker != this.head && ((Comparable) sortedWalker.data).compareTo((Comparable)firstUnsorted) > 0; sortedWalker = sortedWalker.prev){
                sortedWalker.next.data = sortedWalker.data;
            }
            sortedWalker.next.data = firstUnsorted;
        }
    }

    public void add( Object data, int index) {
        if(index < 0 || index > this.size || data ==null)
            throw new IllegalArgumentException("Illegal argument error");

        Node cur;
        int i;
        for( i = 0, cur = this.head; i < index; i ++ ) {
            cur = cur.next;
        }
        Node newNode = new Node(data, cur, cur.next);
        cur.next.prev = newNode;
        cur.next = newNode;
        this.size ++;
    }

    public boolean remove( Object data ) {

        Node cur = this.head.next;

        while( cur != this.head && ! cur.data.equals(data) ) {
            cur =  cur.next;
        }
        if( cur == head )
            return false;
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        this.size --;
        return true;
    }


    public void insertionSort() {
        Node lastSorted, sortedWalker;
        Comparable firstUnsortedData;
        for(lastSorted=this.head.next; lastSorted != this.head.prev; lastSorted = lastSorted.next ) {
            firstUnsortedData = (Comparable)lastSorted.next.data;
            for(sortedWalker=lastSorted; sortedWalker != head && ((Comparable)sortedWalker.data).compareTo(firstUnsortedData) > 0; sortedWalker = sortedWalker.prev){
                sortedWalker.next.data = sortedWalker.data;
            }
            sortedWalker.next.data = firstUnsortedData;
        }
    }

    @Override
    public String toString() {
        String result = "( ";
        for (Node node = this.head.next; node != this.head; node = node.next) {
            result += node.data + ",";
        }
        return result + ")";
    }
}
			