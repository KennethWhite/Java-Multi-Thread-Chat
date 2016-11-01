
/**
 * Created by Casey White on 6/21/2016.
 * i am syncing with the client
 *   more changes
 */
//

import java.io.*;
import java.util.*;

public class LinkedList<E> implements List<E> , Serializable , Cloneable{
    private class Node<E>{
        private Node<E> next;
        private E data;

        private Node(final E data, final Node<E> next){
            this.next = next;
            this.data = data;
        }

        private Node(final E data){
            this.next = null;
            this.data = data;
        }

        private Node(){
            this.next = null;
            this.data = null;
        }
    }

    private int size;
    private Node<E> head;

//ctor linked list
    public LinkedList(){
        this.head = new Node<E>();
        this.size = 0;
    }

//ctor collection
    public LinkedList(Collection<? extends E> c){
        this.head = new Node<E>();
        Iterator<? extends E> itr = c.iterator();
        while(itr.hasNext()){
            add(itr.next());
        }
    }

//add
    public boolean add(final E e)throws NullPointerException {
        if (e == null)
            throw new NullPointerException("Item to be added is null");
        if (isEmpty())
            addFirst(e);
        else {
            Node<E> cur = this.head.next;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = new Node<E>(e);
            this.size++;
        }
        return true;
    }

//add index
    public void add(final int index, final E e)throws NullPointerException, IndexOutOfBoundsException{
        if (e == null)
            throw new NullPointerException("Item to be added is null");
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("The index entered is not valid");
        if (index == 0)
            addFirst(e);
        else{
            Node<E> cur = this.head.next;
            Node<E> prev = this.head;
            for(int i = 0; i < index; i++){
                prev = cur;
                cur= cur.next;
            }
            prev.next = new Node<E>(e,cur);
            this.size++;
        }
    }

//add All
    public boolean addAll(Collection<? extends E> c)throws NullPointerException {
        if (c == null)
            throw new NullPointerException("Collection to be added is null");
        boolean changed = false;
        Node<E> cur = this.head;
        Iterator<? extends E> itr = c.iterator();
        while (cur.next != null) {
            cur = cur.next;
        }
        while(itr.hasNext()){
            add(itr.next());
            changed = true;
        }
        return changed;
    }


//add all  index
    public boolean addAll(final int index, Collection<? extends E> c)throws NullPointerException, IndexOutOfBoundsException{
        if(c == null)
            throw new NullPointerException("Collection to be added is null");
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("The index entered is not valid");
        Iterator<? extends E> itr = c.iterator();
        Node<E> cur = this.head.next;
        Node<E> prev = this.head;
        for(int i = 0; i < index; i++){
            prev = cur;
            cur = cur.next;
        }
        while(itr.hasNext()){
            prev.next = new Node<E>(itr.next());
            prev = prev.next;
            size++;
        }
        prev.next = cur;
        return true;
    }

//add first
    public void addFirst(final E e)throws NullPointerException{
        if(e == null)
            throw new NullPointerException("Item to be added is null");
        this.head.next = new Node<E>(e,this.head.next);
        this.size++;
    }

//add last
    public void addLast(final E e)throws NullPointerException{
        add(e);
    }

//clear
    public void clear(){
        this.head.next = null;
        this.size = 0;
    }

//contains
    public boolean contains(final Object e)throws NullPointerException{
        if (e == null)
            throw new NullPointerException("Item to be added is null");
        if(isEmpty())
            return false;
        Node<E> cur = this.head;
        while(cur.next != null){
            cur = cur.next;
            if(cur.data.equals(e))
                return true;
        }
        return false;
    }

//contains all
    public boolean containsAll(final Collection<?> c){
        Iterator<?> itr = c.iterator();
        boolean contained = true;
        while(itr.hasNext()){
            if(!contains(itr.next()))
                contained = false;
        }
        return contained;
    }

//equals
    @Override
    @SuppressWarnings("unchecked")      //unchecked cast suppression
    public boolean equals(final Object o){
        if(this == o)
            return true;
        if(o instanceof LinkedList<?>) {
            LinkedList<E> list2 = (LinkedList<E>) o;
            if(list2.size() != this.size())
                return false;
            Node<?> cur2 = list2.head;   //THIS IS WHERE THE UNCHECKED EXCEPTION IS
            Node<E> cur = this.head;
            while (cur.next != null) {
                cur = cur.next;
                cur2 = cur2.next;
                if (!cur.data.equals(cur2.data))
                    return false;
            }
            return true;
        }
        return false;
    }

//get index
    public E get(final int index)throws IndexOutOfBoundsException{
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("The index entered is not valid");
        Node<E> cur = this.head.next;
        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        return cur.data;
    }

//hash
    @Override
    public int hashCode(){
        int prime = 7;
        int hash = 1;
        Node<E> cur = this.head.next;
        while(cur != null){
            hash *= prime*cur.data.hashCode();
            cur = cur.next;
        }
        return hash;
    }

//index Of
    public int indexOf(final Object e)throws NullPointerException{
        if (e == null)
            throw new NullPointerException("Item to be found is null");
        int i = 0;
        for(Node<E> cur = this.head.next; cur != null; cur = cur.next){
            if(cur.data.equals(e))
                return i;
            i++;
        }
        return -1;
    }

//Last index Of
    public int lastIndexOf(final Object e)throws NullPointerException{
        if (e == null)
            throw new NullPointerException("Item to be found is null");
        int i = -1;
        int index = 0;
        for(Node<E> cur = this.head.next; cur != null; cur = cur.next){
            if(cur.data.equals(e))
                i = index;
            index++;
        }
        return i;
    }

//isEmpty
    public boolean isEmpty(){
        return (this.head.next == null);
    }

//remove index
    public E remove(final int index)throws IndexOutOfBoundsException{
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("The index entered is not valid");
        Node<E> cur = this.head.next;
        Node<E> prev = this.head;
        for(int i = 0; i < index; i++){
            prev = cur;
            cur=cur.next;
        }
        prev.next = cur.next;
        this.size--;
        return cur.data;
    }

//remove E
    public boolean remove(final Object e){
        if (e == null)
            return false;
        Node<E>  prev = this.head;
        for(Node<E> cur = this.head.next; cur != null; prev = cur, cur = cur.next){
            if(cur.data.equals(e)){
                prev.next = cur.next;
                this.size--;
                return true;
            }
        }
        return false;
    }

//remove all
    public boolean removeAll(final Collection<?> c){
        Iterator<?> itr = c.iterator();
        Boolean isDeleted = false;
        while (itr.hasNext()){
                remove(itr.next());
                isDeleted = true;
        }
        return isDeleted;
    }

//retain all
    public boolean retainAll(final Collection<?> c){
        Node<E> prev = this.head;
        for(Node<E> cur = this.head.next; cur != null;){
            if(!c.contains(cur.data)) {
                prev=cur;
                cur=cur.next;
                remove(prev.data);
            }
            else{
                prev= cur;
                cur = cur.next;
            }
        }
        return false;
    }

//set
    public E set(final int index, final E e)throws NullPointerException, IndexOutOfBoundsException{
        if(e == null)
            throw new NullPointerException("Item that replaces is null");
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("The index entered is not valid");
        Node<E> cur = this.head.next;
        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        E ret = cur.data;
        cur.data = e;
        return ret;
    }

//get size
    public int size(){
        return this.size;
    }
//sublist
    public List<E> subList(final int fromIndex, final int toIndex){////////////////////////////////////////////////////
        if(fromIndex >= this.size || fromIndex < 0 && toIndex >= this.size || toIndex < 0 && fromIndex != toIndex)
            throw new IndexOutOfBoundsException("One of the indices entered is not valid");
        LinkedList<E> list = new LinkedList<>();
        Node<E> cur = this.head.next;
        Node<E> cur2 = list.head;
        for(int i =  0; i < this.size; i++){
            if(i >= fromIndex && i < toIndex) {
                cur2.next = cur;
                list.size++;
                cur2 = cur2.next;
            }
            cur = cur.next;

        }
        return list;
    }

//toString
    public String toString(){
        if(isEmpty())
            return "Empty";
        String ret = "{";
        int i =0;
        for(Node<E> cur = this.head.next; cur != null && i <= size-1; cur = cur.next, i++){
            if(cur.next == null || i >= this.size-1)
                ret += cur.data;
            else
                ret += cur.data + ", ";
        }
        return ret + "}";
    }

//iterator
    public Iterator<E> iterator(){
        return new MyLinkedListIterator(this.head.next);
    }

//list iterator
    public ListIterator<E> listIterator(){
        throw new UnsupportedOperationException("Not implemented");
    }

//list iterator
    public ListIterator<E> listIterator(final int i){
        throw new UnsupportedOperationException("Not implemented");
    }

//toArray
    public Object[] toArray(){
        throw new UnsupportedOperationException("Not implemented");
    }

//toArray
    public <T> T[] toArray(final T[] a){
        throw new UnsupportedOperationException("Not implemented");
    }

//descendingIterator
    public Iterator descendingIterator(){
        throw new UnsupportedOperationException("Not implemented");
    }



//MyLinkedListIterator
    public class MyLinkedListIterator implements Iterator<E>{
        private Node<E> cur;
        private int index;


     //ctor
        public MyLinkedListIterator(Node<E> start){
            this.cur = start;
        }

    //hasNext
        public boolean hasNext(){
            return (cur != null);
        }

     //next
        public E next(){
            if(hasNext()){
                E data = cur.data;
                cur = cur.next;
                return data;
            }
            throw new UnsupportedOperationException("Should not have been reached.");
        }

     //remove
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}//end class
