package socketApplication;

import java.util.LinkedList;

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
