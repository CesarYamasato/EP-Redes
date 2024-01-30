package socketApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;

class DoublyLinkedList<T>{
    Node<T> head = null;
    int lenght = 0;

    public void appendEnding(Node<T> node){
        if(head == null){
            head = node;
            head.next = head;
            head.before = head;
            lenght++;
            return;
        }
        Node<T> temp = head.before;
        head.before = node;
        node.next = head;
        temp.next = node;
        lenght++;
    }

    public void appendBeggining(Node<T> node){
        if(head == null){
            head = node;
            head.next = head;
            head.before = head;
            lenght++;
            return;
        }
        Node<T> temp = head.before;
        node.next = head;
        node.before = head.before;
        temp.next = node;
        head.before = node;
        head = node;
        lenght++;
    }

    public void print(){
        Node<T> node = head;
        for(int i = 0; i < lenght; i++){
            System.out.println("i: " + i + " " + node.data);
            node = node.next;
        }
    }

}

class Node<T>{
    T data;

    Node<T> next;

    Node<T> before;

    public Node(T data){
        this.data = data;
        this.next = null;
        this.before = null;
    }

    public T getData(){
        return data;
    }
}

public class Block {
    LinkedList<byte[]> block = new LinkedList<byte[]>();
    int size = 0;

    public byte[] getBlock() {
        byte[] return_block = new byte[size];

        int index = 0;
        for(byte[] byte_array: block){
            for(byte b : byte_array){
                return_block[index] = b;
                index++;
            }
        }
        return return_block;
    }

    public void append(byte[] byte_array){
        block.add(byte_array);
        size += byte_array.length;
    }

}
