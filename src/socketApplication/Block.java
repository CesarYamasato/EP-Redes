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

    public static void main(String[] args) throws IOException{
        Block block = new Block();
        String teste = "aaabbb";
        String teste2 = "bbbcc";
        block.append(teste.getBytes());
        block.append(teste2.getBytes());
        block.append(teste.getBytes());

        byte[] to_print = block.getBlock();
        byte[] size = Arrays.copyOfRange(to_print, 0, Integer.BYTES);
        byte[] rest = Arrays.copyOfRange(to_print, Integer.BYTES, to_print.length);
        int sizeInt = ByteBuffer.wrap(size).getInt();
        int size_of_string = teste.getBytes().length + teste2.getBytes().length + teste.getBytes().length;
        System.out.println("String size: " + size_of_string);
        System.out.println(sizeInt);
        System.out.println("Size of Int: " + Integer.BYTES);
        System.out.print(new String(rest));
    }

    public byte[] getBlock() {
        byte[] lenght = ByteBuffer.allocate(Integer.BYTES).putInt(size+Integer.BYTES).array();
        byte[] return_block = new byte[size];

        int index = 0;
        for(byte[] byte_array: block){
            for(byte b : byte_array){
                return_block[index] = b;
                index++;
            }
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write( lenght );
            outputStream.write( return_block );

            return_block = outputStream.toByteArray( );
        } catch (IOException e) {
            System.out.println("Unnable to produce block");
        }

        return return_block;
    }

    public void append(byte[] byte_array){
        block.add(byte_array);
        size += byte_array.length;
    }

}
