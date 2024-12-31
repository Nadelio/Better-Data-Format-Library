package BDF;

public class Node<T> {
    private T value; // can be Field<String>, Field<Int>, Field<Boolean>, Constant Object, or Instance

    private Node root;
    private Node last;
    private Node next;
}
