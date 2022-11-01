import java.io.*;
import java.net.*;

import SocketApplication.*;

public class Peer{
	
	private Server server;
	private Client client;
	
	//====================================== Server Side =============================================================//
	
	public void createServer() throws UnknownHostException, IOException {
		ServerSocket serverSocket = new ServerSocket(25565);
		Socket clientSocket = serverSocket.accept();
		server = new Server(clientSocket);
		
		serverSocket.close();
	}
	
	public void receiveRequest() throws IOException {
		server.receiveRequest();
	}
	
	//====================================== Client Side ============================================================//
	
	public void connect() throws UnknownHostException, IOException {
		Socket clientSocket = new Socket("localhost",25565);
		client = new Client(clientSocket);
	}
	
	public void sendRequest() throws IOException {
		client.sendRequest();
	}
	public static void main(String[] args) throws IOException {
			Peer peer = new Peer();
			if(args[0].equals("-s")) {
				peer.createServer();
				//peer.sendListDirectory();
				while (true) peer.receiveRequest();
				//File file = new File("teste.txt");
				//FileSender fileSender = new FileSender(peer.clientSocket);
				//fileSender.sendFile(file);
			}
			else if(args[0].equals("-c")) {
				peer.connect();
				//peer.sendRequest();
				System.out.println("HELLO");
				//peer.receiveListDirectory();
				while(true) peer.sendRequest();
				
				//peer.receiveFiles();
				//FileReceiver fileReceiver = new FileReceiver(peer.clientSocket);
				//fileReceiver.receiveFile();
			}
	}
}
