import java.io.*;
import java.net.*;
import java.util.Scanner;

import FileManager.*;

class Directory{
	private String directory;
	private boolean isFolder;
	
	public Directory(String directory, boolean isFolder) {
		this.directory = directory;
		this.isFolder = isFolder;
	}
	public boolean getFolder() {
		return isFolder;
	}
	public String getList() {
		return directory;
	}
}


class Server {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Directory[] myDirectory;
	private String myPath;
	private int count; //Number of that have been selected but not downloaded
	
	
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
		//Sends the List of items in the folder on which the .class file is on
		public void sendListDirectory() throws IOException {
			File Directory = new File(myPath);
			String contents[] = Directory.list();
			boolean isFolder;
			myDirectory = new Directory[contents.length];
			
			out.writeInt(contents.length);
			
		      for(int i=0; i<contents.length; i++) {
		    	  out.writeInt(contents[i].length());
		    	  isFolder = new File(contents[i]).isDirectory();
		    	  out.writeBoolean(isFolder);
		    	  out.writeBytes(contents[i]);
		    	  myDirectory[i] = new Directory(contents[i],isFolder);
		      }
		}
		
		//Sends files to the client Peer
		public void sendFile() throws IOException {
			int request = in.readInt();
			FileSender fileSender = new FileSender(clientSocket);
			File file = new File(myPath + "/" + myDirectory[request].getList());
			count = countFiles(file);
			//System.out.println(count);
			out.writeInt(count);
			fileSender.sendFile(file);
			//out.writeChar('f');
		}
		
		private int countFiles(File file) {
			int count = 0;
			if(file.isDirectory()) {
				String[] directory = file.list();
				for (int i = 0; i < directory.length; i ++) {
					File fileToTest = new File(file.getAbsolutePath() + "/" + directory[i]);
					if(fileToTest.isDirectory()) count += countFiles(fileToTest);
					else count++;
				}
				return count;
			}
			return 1;
		}

		//Navigates to a folder on the server peer
		public void navigate() throws IOException {
			int requestFolder = in.readInt();
			myPath +=  "/"+ myDirectory[requestFolder].getList();
			String[] directory = new File(myPath).list();
			for(int i = 0; i < directory.length; i++) myDirectory[i]= new Directory(directory[i], new File(directory[i]).isDirectory());
		
		}
		public void receiveRequest() throws IOException {
			int request = in.readInt();
			out.writeInt(9999);
			switch (request) {
			case 1:
				sendListDirectory();
				break;
			case 2:
				sendFile();
				break;
			case 3:
				navigate();
				break;
			}
		}
		
}

class Client{
	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Directory[] otherDirectory;
	private int count;
	
	//Connects to another peer's serverSocket
		public boolean connect(){
			try {
				clientSocket = new Socket("25.68.101.177", 25565);
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
		
		//Receives the List of items the other peer can send over
		public void receiveListDirectory() throws IOException {
			System.out.println("List of files and directories that can be sent:");
			int numOfFiles = in.readInt();
			String[] directory = new String[numOfFiles];
			boolean isFolder;
			otherDirectory = new Directory[directory.length];
			
			for(int i=0; i<numOfFiles; i++) {
				int size = in.readInt();
				isFolder = in.readBoolean();
				directory[i] = new String(in.readNBytes(size));
				if(isFolder) System.out.println(i + ":" + directory[i] + "  Folder");
				else System.out.println(i + ":" + directory[i] + "  File");
				otherDirectory[i] = new Directory(directory[i], isFolder);
			}
		}
		
		//Prints the directory of the other Peer
		public void printDirectory() throws IOException {
			
			if(otherDirectory != null) {
				System.out.println("(outdated by " + count + " downloads) List of files and directories that can be sent:");
				for (int i=0; i < otherDirectory.length;i++) {
					if(otherDirectory[i].getFolder()) System.out.println(i + ":" +  otherDirectory[i].getList() + "  Folder");
					else System.out.println(i + ":" + otherDirectory[i].getList() + "  File");
				}
			}
			else{
				out.writeInt(1);;
			}
		}
		
		//Receives all files that are on the in
		public void receiveFiles() throws IOException{
			count = in.readInt();
			FileReceiver fileReceiver = new FileReceiver(clientSocket);
			for(int i = 0; i < count; i++) {
				fileReceiver.receiveFile();
			}
			count = 0;
		}
		
		//Selects a file to be downloaded from the server Peer
		public void selectFile() throws IOException {
			printDirectory();
			Scanner systemIn = new Scanner(System.in);
			int requestFile = systemIn.nextInt();
			out.writeInt(requestFile);
			//count++;
		}
		
		//Selects a folder to navigate to on the server peer
		public void selectFolder() throws IOException {
			printDirectory();
			Scanner systemIn = new Scanner(System.in);
			int requestFolder = systemIn.nextInt();
			out.writeInt(requestFolder);
			systemIn.close();
		}
		
		//Sends a request to the other peer
		public void sendRequest() throws IOException {
			System.out.println(
					"/////////////////////////////////////////"+ System.lineSeparator() +
					"// 1: Update directory list            //"+ System.lineSeparator() +
					"// 2: Select a file/folder to download //"+ System.lineSeparator() +
					"// 3: Navigate to folder               //"+ System.lineSeparator() +
					"/////////////////////////////////////////"
					);
			Scanner systemIn = new Scanner(System.in);
			int request = systemIn.nextInt();
			out.writeInt(request);
			int answer = in.readInt();
			//Requests: 
			if(answer == 9999) {
				switch (request) {
				case 1:
					receiveListDirectory();
					break;
				case 2:
					selectFile();
					break;
				case 3:
					selectFolder();
					break;
				}
			}
			systemIn.close();
		}
}

public class Peer implements Runnable{
	private Socket clientSocket;
	private ServerSocket serverSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Directory[] otherDirectory;
	private Directory[] myDirectory;
	private int count; //Number of that have been selected but not downloaded
	private String myPath; //Path to folder from which .class was executed
	private String otherPath; //Path to folder from which .class was executed on other peer
	
	public Peer(){
		try {
			serverSocket = new ServerSocket(25565);
			myPath = System.getProperty("user.dir");
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
			clientSocket = new Socket("25.8.38.70", 25565);
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
		File Directory = new File(myPath);
		String contents[] = Directory.list();
		boolean isFolder;
		myDirectory = new Directory[contents.length];
		
		out.writeInt(contents.length);
		
	      for(int i=0; i<contents.length; i++) {
	    	  out.writeInt(contents[i].length());
	    	  isFolder = new File(contents[i]).isDirectory();
	    	  out.writeBoolean(isFolder);
	    	  out.writeBytes(contents[i]);
	    	  myDirectory[i] = new Directory(contents[i],isFolder);
	      }
	}
	
	//Receives the List of items the other peer can send over
	public void receiveListDirectory() throws IOException {
		System.out.println("List of files and directories that can be sent:");
		int numOfFiles = in.readInt();
		String[] directory = new String[numOfFiles];
		boolean isFolder;
		otherDirectory = new Directory[directory.length];
		
		for(int i=0; i<numOfFiles; i++) {
			int size = in.readInt();
			isFolder = in.readBoolean();
			directory[i] = new String(in.readNBytes(size));
			if(isFolder) System.out.println(i + ":" + directory[i] + "  Folder");
			else System.out.println(i + ":" + directory[i] + "  File");
			otherDirectory[i] = new Directory(directory[i], isFolder);
		}
	}
	
	//Sends path of current directory
	public void sendPath() throws IOException {
		int count = myPath.length();
		byte[] pathtoSend = new byte[count];
		out.write(pathtoSend);
	}
	
	//Receives path of current directory
		public void receivePath() throws IOException {
			int count = in.readInt();
			otherPath = new String (in.readNBytes(count));
		}
	//Prints the directory of the other Peer
	public void printDirectory() throws IOException {
		
		if(otherDirectory != null) {
			System.out.println("(outdated by " + count + " downloads) List of files and directories that can be sent:");
			for (int i=0; i < otherDirectory.length;i++) {
				if(otherDirectory[i].getFolder()) System.out.println(i + ":" +  otherDirectory[i].getList() + "  Folder");
				else System.out.println(i + ":" + otherDirectory[i].getList() + "  File");
			}
		}
		else{
			out.writeInt(1);;
		}
	}
	
	//Sends files to the client Peer
	public void sendFile() throws IOException {
		int request = in.readInt();
		FileSender fileSender = new FileSender(clientSocket);
		File file = new File(myPath + "/" + myDirectory[request].getList());
		count = countFiles(file);
		//System.out.println(count);
		out.writeInt(count);
		fileSender.sendFile(file);
		//out.writeChar('f');
	}
	
	private int countFiles(File file) {
		int count = 0;
		if(file.isDirectory()) {
			String[] directory = file.list();
			for (int i = 0; i < directory.length; i ++) {
				File fileToTest = new File(file.getAbsolutePath() + "/" + directory[i]);
				if(fileToTest.isDirectory()) count += countFiles(fileToTest);
				else count++;
			}
			return count;
		}
		return 1;
	}
	
	//Receives all files that are on the in
	public void receiveFiles() throws IOException{
		count = in.readInt();
		FileReceiver fileReceiver = new FileReceiver(clientSocket);
		for(int i = 0; i < count; i++) {
			fileReceiver.receiveFile();
		}
		count = 0;
	}
	
	//Selects a file to be downloaded from the server Peer
	public void selectFile() throws IOException {
		printDirectory();
		Scanner systemIn = new Scanner(System.in);
		int requestFile = systemIn.nextInt();
		out.writeInt(requestFile);
		//count++;
	}
	
	//Selects a folder to navigate to on the server peer
	public void selectFolder() throws IOException {
		printDirectory();
		Scanner systemIn = new Scanner(System.in);
		int requestFolder = systemIn.nextInt();
		out.writeInt(requestFolder);
		systemIn.close();
	}
	
	//Navigates to a folder on the server peer
	public void navigate() throws IOException {
		int requestFolder = in.readInt();
		myPath +=  "/"+ myDirectory[requestFolder].getList();
		String[] directory = new File(myPath).list();
		for(int i = 0; i < directory.length; i++) myDirectory[i]= new Directory(directory[i], new File(directory[i]).isDirectory());
	
	}
	
	//Sends a request to the other peer
	public void sendRequest() throws IOException {
		System.out.println(
				"/////////////////////////////////////////"+ System.lineSeparator() +
				"// 1: Update directory list            //"+ System.lineSeparator() +
				"// 2: Select a file/folder to download //"+ System.lineSeparator() +
				"// 3: Navigate to folder               //"+ System.lineSeparator() +
				"/////////////////////////////////////////"
				);
		Scanner systemIn = new Scanner(System.in);
		int request = systemIn.nextInt();
		out.writeInt(request);
		int answer = in.readInt();
		//Requests: 
		if(answer == 9999) {
			switch (request) {
			case 1:
				receiveListDirectory();
				break;
			case 2:
				selectFile();
				receiveFiles();
				break;
			case 3:
				selectFolder();
				break;
			}
		}
		//systemIn.close();
	}
	
	//Processes client requests for files
	public void receiveRequest() throws IOException {
		int request = in.readInt();
		out.writeInt(9999);
		switch (request) {
		case 1:
			sendListDirectory();
			break;
		case 2:
			sendFile();
			break;
		case 3:
			navigate();
			break;
		}
	}
	
	public void run() {
		try {
			receiveRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				while (true) peer.receiveRequest();
				//File file = new File("teste.txt");
				//FileSender fileSender = new FileSender(peer.clientSocket);
				//fileSender.sendFile(file);
			}
			else if(args[0].equals("-c")) {
				peer.connect();
				//peer.sendRequest();
				System.out.println("HELLO");
				peer.receiveListDirectory();
				while(true) peer.sendRequest();
				
				//peer.receiveFiles();
				//FileReceiver fileReceiver = new FileReceiver(peer.clientSocket);
				//fileReceiver.receiveFile();
			}
			peer.close();
	}
}
