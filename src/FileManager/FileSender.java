package FileManager;

import java.io.*;
import java.net.*;

//TODO: add more comments

public class FileSender {
    //TODO: add encryption
    DataOutputStream out;
    Socket socket;

    // Creates a FileSender object to send files to the specified Socket
    public FileSender(Socket socket) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates a FileSender object and attatches to it a Socket with the specified
    // external IP address and port to send files
    public FileSender(int port, InetAddress IP) {
        try {
            this.socket = new Socket(IP, port);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileName(File file) throws IOException{
        byte[] fileName = file.getName().getBytes();
        out.writeInt(fileName.length);
        out.write(fileName);
    }
    
    // Sends a file to the socket specified upen creation of the FileSender
    public void sendFile(File file) throws IOException {
        out.writeChar('F');
        FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());

        sendFileName(file);

        byte[] fileContents = new byte[(int) file.length()];

        fileIn.read(fileContents);
        out.writeInt(fileContents.length);
        out.write(fileContents);

        fileIn.close();
    }

    //Used to send a file with non-default flags
    private void sendFile(File file, char flag) throws IOException {
        out.writeChar(flag);
        sendFileName(file);
        if(file.isDirectory()) {
            out.writeInt(0);
            return;
        }
        FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
        byte[] fileContents = new byte[(int) file.length()];

        fileIn.read(fileContents);
        out.writeInt(fileContents.length);
        out.write(fileContents);

        fileIn.close();
    }
    
    // Sends a folder to the socket specified upon creation of the FileSender
    public void sendFolder(File file) throws IOException {
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
