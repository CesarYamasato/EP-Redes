package FileManager;

import java.io.*;
import java.net.*;

public class FileSender {

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
    // IP address and port to send files
    public FileSender(int port, InetAddress IP) {
        try {
            this.socket = new Socket(IP, port);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends a file to the socket specified upen creation of the FileSender
    public void sendFile(File file) throws IOException {
        if (file.isDirectory()) {
            sendFolder(file);
            return;
        }
        FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());

        byte[] fileName = file.getName().getBytes();
        out.writeInt(fileName.length);
        out.write(fileName);

        byte[] fileContents = new byte[(int) file.length()];

        fileIn.read(fileContents);
        out.writeInt(fileContents.length);
        out.write(fileContents);

        fileIn.close();
    }

    // Sends a folder to the socket specified upon creation of the FileSender
    private void sendFolder(File file) throws IOException {
        int count = 0;
        String[] directory = file.list();
        for (int i = 0; i < directory.length; i++) {
            File fileToSend = new File(file.getAbsolutePath() + "/" + directory[i]);
            if (fileToSend.isDirectory())
                sendFolder(fileToSend);
            else
                sendFile(fileToSend);
        }
    }

    // Closes the OutputStream created with the FileSender
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
