package main;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import Encryption.EncryptionException.NoKeyException;
import socketApplication.*;

public class Peer extends Thread {
    
    //each peer is able to send and ask for files, so each must have both a server and a client side
    private Server server;
    private Client client;

    // ====================================== Server Side =============================================================//

    //Effectively never used. Might be used in the future for Peer paring server.
    private String clientAddress;
    private int port;
    
    //In order to create the server object, we must provide it with the socket which the other peer will be listening to
    public void createServer(int port) throws UnknownHostException, IOException, NoSuchAlgorithmException {
        //creating socket for the other peer to connect
        ServerSocket serverSocket = new ServerSocket(port);
        //accepting connection and creating the socket
        Socket clientSocket = serverSocket.accept();
        //creating server object
        server = new Server(clientSocket);

        //effectively not used
        clientAddress = clientSocket.getInetAddress().toString();
        clientAddress = clientAddress.substring(1);

        //closing server socket to avoid resource taking space
        serverSocket.close();
    }

    //calls the server's methods to listen to other peer's requests
    public void receiveRequest() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if(!this.isClosed()) server.receiveRequest();
        else this.close();
    }

    //waits for awake messaging confirming connection
    public int waitAwake() throws IOException {
        return port = server.waitAwake();
    }

    //effectively not used.
    public String getAddress() {
        return clientAddress;
    }

    //effectively not used.
    public int getPort() {
        return port;
    }       
    
    //TODO: fix this (treat exceptions in encryption lib)
    //creates another thread to listen for other peer's requests
    public void run() {
        try {
            while (!this.isClosed()) receiveRequest();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ====================================== Client Side ============================================================//

    //The client object needs a socket to which it will send the requests 
    public void connect(String IP, int port) throws UnknownHostException, IOException, NoSuchAlgorithmException {
        //creating socket
        Socket clientSocket = new Socket(IP, port);
        //creating client object
        client = new Client(clientSocket);

        //not used
        clientAddress = IP;
        this.port = port;
    }

    //sends request to the server side of the other peer
    public void sendRequest() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
        if(!this.isClosed())client.sendRequest();
        else this.close();
    }

    //sends awake message confirming connection
    public void sendAwake(int port) throws IOException {
        client.sendAwake(port);
    }

    //used when connection is closed
    public boolean isClosed() {
        return server.isClosed() || client.isClosed() ? true : false;
    }
    
    //closes connection and ends the program
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
    
    public static void main(String[] args) throws IOException, NumberFormatException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
        Security.addProvider(new BouncyCastleProvider());
        Peer peer = new Peer();
        if(args.length > 0) {
            if (args[0].equals("-w") && args.length == 2) {
                peer.createServer(Integer.parseInt(args[1]));
                peer.waitAwake();
                peer.connect(peer.getAddress(),peer.getPort());
                System.out.println("Connected");

                //The peer that first waits for the communication will be the one providing the RC6 key to encrypt the transferred
                //the RC6 key must first be encrypted with connecting peer's RSA public key, to be decrypted upon arrival

                //receiving other peer's public key
                peer.server.receiveRSAKey(); 

                //sending RSA encrypted RC6 key
                peer.server.sendRC6Key(); 

            } else if (args[0].equals("-c")  && args.length == 4) {
                peer.connect(args[2], Integer.parseInt(args[3]));
                peer.sendAwake(Integer.parseInt(args[1]));
                peer.createServer(Integer.parseInt(args[1]));
                System.out.println("Connected");

                //The peer that first waits for the communication will be the one providing the RC6 key to encrypt the transferred
                //the RC6 key must first be encrypted with connecting peer's RSA public key, to be decrypted upon arrival

                //sending RSA public key to encrypt RC6 key
                peer.client.sendRSAKey();

                //receiving RSA encrypted RC6 key and decrypting it
                peer.client.receiveRC6Key();
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
