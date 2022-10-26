import java.io.*;
import java.net.*;
import java.util.Scanner;

import FileManager.*;

public class Peer {
	private Socket clientSocket;
	private ServerSocket serverSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private String[] otherDirectory;
	private String[] myDirectory;
	private int count;
	
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
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Connects to another peer's serverSocket
	public boolean connect(){
		try {
			clientSocket = new Socket("localhost", 25565);
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	//Sends the List of items in the folder on which the .class file is on
	public void sendListDirectory() throws IOException {
		File Directory = new File(System.getProperty("user.dir"));
		String contents[] = Directory.list();
		
		out.writeInt(contents.length);
		
	      for(int i=0; i<contents.length; i++) {
	    	  out.writeInt(contents[i].length());
	    	  out.writeBytes(contents[i]);
	      }
	      this.myDirectory = contents;
	}
	
	//Receives the List of items the other peer can send over
	public void receiveListDirectory() throws IOException {
		System.out.println("List of files and directories that can be sent:");
		int numOfFiles = in.readInt();
		String[] directory = new String[numOfFiles]; 
		for(int i=0; i<numOfFiles; i++) {
			int size = in.readInt();
			directory[i] = new String(in.readNBytes(size));
			System.out.println(i + ":" + directory[i]);
		}
		count = 0;
		this.otherDirectory = directory;
	}
	
	//Prints the directory of the other Peer
	public void printDirectory() throws IOException {
		
		if(otherDirectory != null) {
			System.out.println("(outdated by" + count + "downloads) List of files and directories that can be sent:");
			for (int i=0; i < otherDirectory.length;i++) {
				System.out.println(i + ":" + otherDirectory[i]);
			}
		}
		else{
			sendRequest(1);
		}
	}
	
	public void selectFile() throws IOException {
		printDirectory();
		Scanner systemIn = new Scanner(System.in);
		int requestFile = systemIn.nextInt();
		out.writeInt(requestFile);
	}
	
	//Sends a request to the other peer
	public void sendRequest() throws IOException {
		Scanner systemIn = new Scanner(System.in);
		int request = systemIn.nextInt();
		out.writeInt(request);
		
		//Requests: 
		
		switch (request) {
		case 1:
			receiveListDirectory();
			break;
		case 2:
			selectFile();
			break;
		}
	}
	
	private void sendRequest(int request) throws IOException {
		out.writeInt(request);
		
		//Requests: 
		
		switch (request) {
		case 1:
			receiveListDirectory();
			break;
		case 2:
			
		}
	}
	
	//Processes client requests for files
	public void receiveRequest() {
		
		
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
				peer.sendRequest();
				System.out.println("HELLO");
				peer.receiveListDirectory();
				FileReceiver fileReceiver = new FileReceiver(peer.clientSocket);
				fileReceiver.receiveFile();
			}
			peer.close();
	}
}
