import java.io.*;
import java.net.*;

import socketApplication.*;

public class Peer extends Thread {

    private Server server;
    private Client client;

    // ====================================== Server Side
    // =============================================================//

    public void createServer(int port) throws UnknownHostException, IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);

        serverSocket.close();
    }

    public void receiveRequest() throws IOException {
        server.receiveRequest();
    }

    public void waitAwake() throws IOException {
        server.waitAwake();
    }

    public void run() {
        try {
            while (true)
                receiveRequest();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ====================================== Client Side
    // ============================================================//

    public void connect(String IP, int port) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(IP, port);
        client = new Client(clientSocket);
    }

    public void sendRequest() throws IOException {
        client.sendRequest();
    }

    public void sendAwake() throws IOException {
        client.sendAwake();
    }

    public static void main(String[] args) throws IOException {
        Peer peer = new Peer();
        if (args[0].equals("-s")) {
            peer.createServer(Integer.parseInt(args[1]));
            peer.waitAwake();
            peer.connect(args[2], Integer.parseInt(args[3]));
        } else if (args[0].equals("-c")) {
            peer.connect(args[2], Integer.parseInt(args[3]));
            peer.sendAwake();
            peer.createServer(Integer.parseInt(args[1]));
        }
        peer.start();
        while (true)
            peer.sendRequest();
    }
}
