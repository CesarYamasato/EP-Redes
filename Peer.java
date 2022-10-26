import java.io.*;
import java.net.*;
import FileManager.*;

public class Peer {
	Socket clientSocket;
	ServerSocket serverSocket;
	
	public Peer(){
		try {
			serverSocket = new ServerSocket(25565);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Makes the Peer wait for connection from another peer on its serverSocket
	public void createServer(){
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Connects to another peer's serverSocket
	public void connect(){
		try {
			clientSocket = new Socket("localhost", 25565);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Sends the List of items in the folder on which the .class file is on
	public void sendListDirectory() throws IOException {
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		
		File Directory = new File(System.getProperty("user.dir"));
		String contents[] = Directory.list();
		
		out.writeInt(contents.length);
		
	      for(int i=0; i<contents.length; i++) {
	    	  out.writeInt(contents[i].length());
	    	  out.writeBytes(contents[i]);
	      }
	}
	
	//Receives the List of items the other peer can send over
	public void receiveListDirectory() throws IOException {
		DataInputStream in = new DataInputStream(clientSocket.getInputStream());
		System.out.println("List of files and directories that can be sent:");
		int numOfFiles = in.readInt();
		for(int i=0; i<numOfFiles; i++) {
			int size = in.readInt();
			System.out.println(new String(in.readNBytes(size)));
		}
	}
	
	//Closes the connection with the other peer
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
				peer.sendListDirectory();
				File file = new File("teste.txt");
				FileSender fileSender = new FileSender(peer.clientSocket);
				fileSender.sendFile(file);
			}
			else if(args[0].equals("-c")) {
				peer.connect();
				System.out.println("HELLO");
				peer.receiveListDirectory();
				FileReceiver fileReceiver = new FileReceiver(peer.clientSocket);
				fileReceiver.receiveFile();
			}
			peer.close();
	}
}