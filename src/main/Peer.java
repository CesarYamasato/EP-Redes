package main;

import java.io.*;
import java.net.*;

import socketApplication.*;

public class Peer extends Thread {

    private Server server;
    private Client client;

    // ====================================== Server Side =============================================================//

    private String clientAddress;
    private int port;
    
    public void createServer(int port) throws UnknownHostException, IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        server = new Server(clientSocket);
        clientAddress = clientSocket.getInetAddress().toString();
        clientAddress = clientAddress.substring(1);
        serverSocket.close();
    }

    public void receiveRequest() throws IOException {
        if(!this.isClosed()) server.receiveRequest();
        else this.close();
    }

    public int waitAwake() throws IOException {
        return port = server.waitAwake();
    }

    public String getAddress() {
        return clientAddress;
    }
    public int getPort() {
        return port;
    }       
    
    public void run() {
        try {
            while (!this.isClosed()) receiveRequest();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ====================================== Client Side ============================================================//

    public void connect(String IP, int port) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(IP, port);
        client = new Client(clientSocket);
        clientAddress = IP;
        this.port = port;
    }

    public void sendRequest() throws IOException {
        if(!this.isClosed())client.sendRequest();
        else this.close();
    }

    public void sendAwake(int port) throws IOException {
        client.sendAwake(port);
    }

    public boolean isClosed() {
        return server.isClosed() || client.isClosed() ? true : false;
    }
    
    public void close() {
        if(!server.isClosed()) server.close();
        if(!client.isClosed()) client.close();
        System.exit(0);
    }
    
    private static void printHelp() {
        System.out.println( "Usage:" + System.lineSeparator() +
                            "Available options:" + System.lineSeparator() +
                            "-w [port] -> Creates a server socket on the specified port"+ System.lineSeparator() +
                            "-c [port] [address] [port to connect] -> Connects to the specified socket on the specified address and creates a server socket on the specified port");
    }
    
    public static void main(String[] args) throws IOException {
        Peer peer = new Peer();
        if(args.length > 0) {
            if (args[0].equals("-w") && args.length == 2) {
                peer.createServer(Integer.parseInt(args[1]));
                peer.waitAwake();
                peer.connect(peer.getAddress(),peer.getPort());
            } else if (args[0].equals("-c")  && args.length == 4) {
                peer.connect(args[2], Integer.parseInt(args[3]));
                peer.sendAwake(Integer.parseInt(args[1]));
                peer.createServer(Integer.parseInt(args[1]));
            }
            else {
                printHelp();
                return;
            }
            peer.start();
            while (true && !peer.isClosed()) peer.sendRequest();
            peer.close();
        }
        else {
            printHelp();
            return;
        }
    }
}
