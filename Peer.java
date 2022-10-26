import java.io.*;
import java.net.*;
import FileManager.*;

public class Peer {
	Socket clientSocket;
	ServerSocket serverSocket;
	
	public Peer(){
		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createServer(){
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void connect(){
		try {
			clientSocket = new Socket("localhost", 8000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void close() {
		try {
			this.serverSocket.close();
			this.clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
			Peer peer = new Peer();
			if(args[0].equals("-s")) {
				peer.createServer();
				File file = new File("teste.txt");
				FileSender fileSender = new FileSender(peer.clientSocket);
				fileSender.sendFile(file);
			}
			else if(args[0].equals("-c")) {
				peer.connect();
				FileReceiver fileReceiver = new FileReceiver(peer.clientSocket);
				fileReceiver.receiveFile();
			}
			peer.close();
	}
}