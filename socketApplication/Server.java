package socketApplication;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import FileManager.FileSender;

public class Server extends SocketApplication{
	private Directory[] myDirectory;
	private String myPath;
	private int count; //Number of that have been selected but not downloaded
	
	
	//Makes the Peer wait for connection from another peer on its serverSocket
		public Server(Socket clientSocket) throws UnknownHostException, IOException{
			super(clientSocket);
			myPath = System.getProperty("user.dir");
			sendListDirectory();
		}
		//Sends the List of items in the folder on which the .class file is on
		private void sendListDirectory() throws IOException {
			File Directory = new File(myPath);
			String contents[] = Directory.list();
			boolean isFolder;
			myDirectory = new Directory[contents.length + 1];
			myDirectory[0] = new Directory("..", true);
			
			out.writeInt(myDirectory.length);
			
			out.writeInt(myDirectory[0].getList().length());
			out.writeBoolean(true);
			out.writeBytes(myDirectory[0].getList());
			
		      for(int i=1; i<myDirectory.length; i++) {
		    	  out.writeInt(contents[i-1].length());
		    	  isFolder = new File(contents[i-1]).isDirectory();
		    	  out.writeBoolean(isFolder);
		    	  out.writeBytes(contents[i-1]);
		    	  myDirectory[i] = new Directory(contents[i-1],isFolder);
		      }
		}
		
		//Sends files to the client Peer
		private void sendFile() throws IOException {
			int request = in.readInt();
			FileSender fileSender = new FileSender(clientSocket);
			File file = new File(myPath + "/" + myDirectory[request].getList());
			//count = countFiles(file);
			//System.out.println(count);
			//out.writeInt(count);
			if(file.isDirectory()) fileSender.sendFolder(file);
			else fileSender.sendFile(file);
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
		private void navigate() throws IOException {
			int requestFolder = in.readInt();
			myPath +=  "/"+ myDirectory[requestFolder].getList();
			String[] directory = new File(myPath).list();
			myDirectory = new Directory[directory.length+1];
			for(int i = 1; i < myDirectory.length; i++) myDirectory[i]= new Directory(directory[i-1], new File(directory[i-1]).isDirectory());
		
		}
		
		//Waits for handshake
		public void waitAwake() throws IOException {
			while(in.available() == 0);
			in.read();
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