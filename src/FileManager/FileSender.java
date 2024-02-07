package FileManager;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Encryption.Symmetric.SymmetricEncryptor;

//TODO: add more comments

public class FileSender {
    //TODO: add encryption
    SymmetricEncryptor encryptor;
    DataOutputStream out;
    Socket socket;

    // Creates a FileSender object to send files to the specified Socket
    public FileSender(Socket socket, SymmetricEncryptor encryptor) {
        this.socket = socket;
        this.encryptor = encryptor;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates a FileSender object and attatches to it a Socket with the specified
    // external IP address and port to send files
    public FileSender(int port, InetAddress IP, SymmetricEncryptor encryptor) {
        try {
            this.socket = new Socket(IP, port);
            this.encryptor = encryptor;
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileName(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        byte[] fileName = file.getName().getBytes();
        byte [] encryptedFileName = encryptor.encrypt(fileName);
        out.writeInt(encryptedFileName.length);
        out.write(encryptedFileName);
    }

    private void sendFileContents(File file)throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());

        byte[] fileContents = new byte[(int) file.length()];

        fileIn.read(fileContents);

        byte[] encryptedFileContents = encryptor.encrypt(fileContents);

        out.writeInt(encryptedFileContents.length);
        out.write(encryptedFileContents);

        fileIn.close();
    }
    
    // Sends a file to the socket specified upen creation of the FileSender
    public void sendFile(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        out.writeChar('F');
        sendFileName(file);
        sendFileContents(file);
    }

    //Used to send a file with non-default flags
    private void sendFile(File file, char flag) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        out.writeChar(flag);
        //sendFileName(file);
        //TODO: check if this condition is ever met
        // if(file.isDirectory()) {
        //     out.writeInt(0);
        //     return;
        // }
        sendFileName(file);
        sendFileContents(file);
    }
    
    // Sends a folder to the socket specified upon creation of the FileSender
    public void sendFolder(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        out.writeChar('D');
        sendFileName(file);
        String[] directory = file.list();
        for (int i = 0; i < directory.length; i++) {
            File fileToSend = new File(file.getAbsolutePath() + "/" + directory[i]);
            if (fileToSend.isDirectory()) 
                sendFolder(fileToSend);
            else 
                sendFile(fileToSend, 'N');     
        }
        out.writeChar('E');
    }
}
