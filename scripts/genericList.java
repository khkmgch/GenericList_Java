import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

interface Queue<E> {
    public abstract E peekFront(); // リストの先頭にある要素を返します。
    public abstract E popFront(); // リストの先頭の要素を削除し、削除した要素を返します。
    public abstract void pushBack(E data); // リストの末尾に要素を追加します。
}
interface Stack<E> {
    public abstract E peekBack(); // リストの末尾にある要素を返します。
    public abstract E popBack(); // リストの末尾の要素を削除し、削除した要素を返します。
    public abstract void pushBack(E data); // リストの末尾に要素を追加します。
}
interface Deque<E> extends Stack<E>, Queue<E>{
    public abstract void pushFront(E data); // リストの先頭に要素を追加します。
}
abstract class GenericAbstractList<E> implements Deque<E>{
    private E[] initialList;

    // 汎用的なリストを使ってAbstractListを開始することも、空のリストを使って開始することもできます。
    public GenericAbstractList(){}

    public GenericAbstractList(E[] arr){
        this.initialList = arr;
    }

    public E[] getOriginalList(){
        return initialList;
    }

    // GenericAbstractListが実装しなければならないメソッド
    public abstract E get(int index);
    public abstract void add(E element);
    public abstract void add(E[] elements);
    public abstract E pop();
    public abstract void addAt(int index, E element);
    public abstract void addAt(int index, E[] elements);
    public abstract E removeAt(int index);
    public abstract void removeAllAt(int start);
    public abstract void removeAllAt(int start, int end);
    public abstract GenericAbstractList<E> subList(int start);
    public abstract GenericAbstractList<E> subList(int start, int end);
}
class Node<E>{
    public E data;
    public Node<E> prev;
    public Node<E> next;

    public Node(E data){
        this.data = data;
    }
}
class GenericLinkedList<E> extends GenericAbstractList<E>{
    private Node<E> head;
    private Node<E> tail;
    
    public GenericLinkedList(){
        super();
    }

    public String toString(){
        StringBuilder str = new StringBuilder("[");
        Node<E> iterator = this.head;
        while(iterator != null){
            str.append(iterator.data + ", ");
            iterator = iterator.next;
        }
        str.append("]");
        return str.toString();
    }
    public E peekFront(){
        if(this.head == null)return null;
        return this.head.data;
    }
    public E popFront(){
        if(this.head == null)return null;
        Node<E> temp = this.head;
        this.head = this.head.next;
        if(this.head != null)this.head.prev = null;
        else this.tail = null;

        return temp.data;
    }
    public void pushBack(E data){
        Node<E> node = new Node<E>(data);
        if(this.peekBack() == null){
            this.head = node;
            this.tail = this.head;
        }else{
            node.prev = this.tail;
            this.tail.next = node;
            this.tail = this.tail.next;
        }
    }
    public E peekBack(){
        if(this.tail == null)return this.peekFront();
        return this.tail.data;
    }
    public E popBack(){
        if(this.tail == null)return null;
        Node<E> temp = this.tail;
        this.tail = this.tail.prev;
        if(this.tail != null)this.tail.next = null;
        else this.head = null;

        return temp.data;
    }
    public void pushFront(E data){
        Node<E> node = new Node<E>(data);
        if(this.peekFront() == null){
            this.head = node;
            this.tail = this.head;
        }else{
            node.next = this.head;
            this.head.prev = node;
            this.head = this.head.prev;
        }
    }
    public Node<E> at(int index){
        if(index < 0)return null;
        Node<E> iterator = this.head;
        for(int i = 0; i < index; i++){
            iterator = iterator.next;
            if(iterator == null)return null;
        }
        return iterator;
    }
    public int size(){
        if(this.head == null)return 0;
        int size = 0;
        Node<E> iterator = this.head;
        while(iterator != null){
            size++;
            iterator = iterator.next;
        }
        return size;
    }
    // GenericAbstractListが実装しなければならないメソッド
    public E get(int index){
        Node<E> found = this.at(index);
        if(found == null)return null;
        return found.data;
    }
    public void add(E element){
        this.pushBack(element);
    }
    public void add(E[] elements){
        for(int i = 0; i < elements.length; i++){
            this.pushBack(elements[i]);
        }
    }
    public E pop(){
        return this.popBack();
    }
    public void addAt(int index, E element){
        if(index < 0)return;
        if(index == this.size()){
            this.pushBack(element);
            return;
        }
        Node<E> targetNode = this.at(index);
        if(targetNode == this.head){
            this.pushFront(element);
            return;
        }
        Node<E> node = new Node<E>(element);
        node.prev = targetNode.prev;
        node.next = targetNode;
        node.prev.next = node;
        targetNode.prev = node;
    }
    public void addAt(int index, E[] elements){
        if(index < 0)return;
        if(index == this.size()){
            this.add(elements);
            return;
        }
        Node<E> targetNode = this.at(index);
        if(targetNode == this.head){
            for(int i = elements.length-1; i >= 0; i++){
                this.pushFront(elements[i]);
            }
            return;
        }
        for(int i = 0; i < elements.length; i++){
            Node<E> node = new Node<E>(elements[i]);
            node.prev = targetNode.prev;
            node.next = targetNode;
            node.prev.next = node;
            targetNode.prev = node;
        }
    }
    public E removeAt(int index){
        Node<E> deleteNode = this.at(index);
        if(deleteNode == null)return null;
        if(deleteNode == this.head)this.popFront();
        else if(deleteNode == this.tail)this.popBack();
        else{
            deleteNode.prev.next = deleteNode.next;
            deleteNode.next.prev = deleteNode.prev;
        }
        return deleteNode.data;
    }
    public void removeAllAt(int start){
        Node<E> deleteNode = this.at(start);
        if(deleteNode == null)return;
        
        this.tail = deleteNode.prev;
        if(this.tail != null)this.tail.next = null;
        else this.head = null;
    }
    public void removeAllAt(int start, int end){
        Node<E> startNode = this.at(start);
        Node<E> endNode = this.at(end);
        if(startNode == null)return;
        else if(endNode == null){
            this.tail = startNode.prev;
            if(this.tail != null)this.tail.next = null;
            else this.head = null;
        }else{
            startNode.prev.next = endNode;
            endNode.prev = startNode.prev;
        }
    }
    public GenericAbstractList<E> subList(int start){
        Node<E> iterator = this.at(start);
        GenericAbstractList<E> deepCopy = new GenericLinkedList<E>();
        while(iterator != null){
            deepCopy.pushBack(iterator.data);
            iterator = iterator.next;
        }
        return deepCopy;
    }
    public GenericAbstractList<E> subList(int start, int end){
        Node<E> iterator = this.at(start);
        Node<E> endNode = this.at(end);
        GenericAbstractList<E> deepCopy = new GenericLinkedList<E>();
        while(iterator != null && iterator != endNode){
            deepCopy.pushBack(iterator.data);
            iterator = iterator.next;
        }
        return deepCopy;
    }

}


class GenericArrayList<E> extends GenericAbstractList<E>{
    private ArrayList<E> arraylist;

    public GenericArrayList(){
        super();
        this.arraylist = new ArrayList<E>();
    }
    public GenericArrayList(E[] arr){
        super(arr);
        this.arraylist = new ArrayList<E>(arr.length);
        Arrays.stream(arr).forEach(x -> this.arraylist.add(x));
    }

    public String toString(){
        StringBuilder str = new StringBuilder("[");
        for(int i = 0; i < this.arraylist.size(); i++){
            str.append(this.arraylist.get(i) + ", ");
        }
        str.append("]");
        return str.toString();
    }
    public E peekFront(){
        return this.arraylist.get(0);
    }
    public E popFront(){
        E data = this.arraylist.get(0);
        this.arraylist.remove(0);
        return data;
    }
    public void pushBack(E data){
        this.arraylist.add(data);
    }
    public E peekBack(){
        return this.arraylist.get(this.arraylist.size()-1);
    }
    public E popBack(){
        E data = this.arraylist.get(this.arraylist.size()-1);
        this.arraylist.remove(this.arraylist.size()-1);
        return data;
    }
    public void pushFront(E data){
        this.arraylist.set(0, data);
    }

    // GenericAbstractListが実装しなければならないメソッド
    public E get(int index){
        return this.arraylist.get(index);
    }
    public void add(E element){
        this.pushBack(element);
    }
    public void add(E[] elements){
        ArrayList<E> curr = this.arraylist;
        this.arraylist = new ArrayList<E>(curr.size() + elements.length);
        this.arraylist.addAll(curr);
        for(int i = 0; i < elements.length; i++){
            this.arraylist.add(elements[i]);
        }
    }
    public E pop(){
        return this.popBack();
    }
    public void addAt(int index, E element){
        ArrayList<E> curr = this.arraylist;
        this.arraylist = new ArrayList<E>(curr.size() + 1);
        for(int i = 0; i < this.arraylist.size(); i++){
            if(i == index){
                this.arraylist.add(element);
            }
            this.arraylist.add(curr.get(i));
        }
    }
    public void addAt(int index, E[] elements){
        ArrayList<E> curr = this.arraylist;
        this.arraylist = new ArrayList<E>(curr.size() + elements.length);
        for(int i = 0; i < this.arraylist.size(); i++){
            if(i == index){
                for(int j = 0; i < elements.length; i++){
                    this.arraylist.add(elements[i]);
                }
            }
            this.arraylist.add(curr.get(i));
        }
    }
    public E removeAt(int index){
        E data = this.arraylist.get(index);
        this.arraylist.set(index, null);
        return data;
    }
    public void removeAllAt(int start){
        for(int i = start; i < this.arraylist.size(); i++){
            this.arraylist.set(i, null);
        }
    }
    public void removeAllAt(int start, int end){
        for(int i = start; i < end; i++){
            this.arraylist.set(i, null);
        }
    }
    public GenericAbstractList<E> subList(int start){
        GenericAbstractList<E> list = new GenericArrayList<E>();
        for(int i = start; i < this.arraylist.size(); i++){
            list.add(this.arraylist.get(i));
        }
        return list;
    }
    public GenericAbstractList<E> subList(int start, int end){
        GenericAbstractList<E> list = new GenericArrayList<E>();
        for(int i = start; i < end; i++){
            list.add(this.arraylist.get(i));
        }
        return list;
    }
}

class Main{
    public static void main(String[] args){
        GenericLinkedList<Integer> integerLinkedList = new GenericLinkedList<Integer>();
        integerLinkedList.add(1);
        integerLinkedList.add(2);
        integerLinkedList.add(3);
        integerLinkedList.add(4);
        integerLinkedList.add(5);
        integerLinkedList.add(6);
        integerLinkedList.add(7);
        System.out.println(integerLinkedList);
        System.out.println(integerLinkedList.peekFront());
        System.out.println(integerLinkedList.peekBack());
        System.out.println(integerLinkedList.popFront());
        System.out.println(integerLinkedList.popBack());
        integerLinkedList.pushFront(8);
        System.out.println(integerLinkedList);
        System.out.println(integerLinkedList.subList(2, 4));
        integerLinkedList.removeAllAt(1);
        System.out.println(integerLinkedList);


        GenericArrayList<Character> characterArrayList = new GenericArrayList<Character>();
        characterArrayList.add('a');
        characterArrayList.add('b');
        characterArrayList.add('c');
        characterArrayList.add('d');
        characterArrayList.add('e');
        characterArrayList.add('f');
        characterArrayList.add('g');
        System.out.println(characterArrayList);
        System.out.println(characterArrayList.peekFront());
        System.out.println(characterArrayList.peekBack());
        System.out.println(characterArrayList.popFront());
        System.out.println(characterArrayList.popBack());
        characterArrayList.pushFront('h');
        System.out.println(characterArrayList.subList(2, 4));
        characterArrayList.removeAllAt(2, 4);
        System.out.println(characterArrayList);

        
    }
}
