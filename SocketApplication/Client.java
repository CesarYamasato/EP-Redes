package SocketApplication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import FileManager.FileReceiver;

public class Client extends SocketApplication{
	private Directory[] otherDirectory;
	private int count;
	
	//Connects to another peer's serverSocket
		public Client(Socket clientSocket) throws UnknownHostException, IOException{
				super(clientSocket);
		}
		
		//Receives the List of items the other peer can send over
		private void receiveListDirectory() throws IOException {
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
		private void printDirectory() throws IOException {
			
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
		private void receiveFiles() throws IOException{
			count = in.readInt();
			FileReceiver fileReceiver = new FileReceiver(clientSocket);
			for(int i = 0; i < count; i++) {
				fileReceiver.receiveFile();
			}
			count = 0;
		}
		
		//Selects a file to be downloaded from the server Peer
		private void selectFile() throws IOException {
			printDirectory();
			int requestFile = systemIn.nextInt();
			out.writeInt(requestFile);
			//count++;
		}
		
		//Selects a folder to navigate to on the server peer
		private void selectFolder() throws IOException {
			printDirectory();
			int requestFolder = systemIn.nextInt();
			out.writeInt(requestFolder);
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
			int request = systemIn.nextInt();
			out.writeInt(request);
			while (in.available() == 0);
			int answer= in.readInt();
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
		}
}