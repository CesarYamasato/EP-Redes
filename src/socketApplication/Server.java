package socketApplication;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import FileManager.FileSender;

public class Server extends SocketApplication{
	private Directory myDirectory;
	private String myPath;
	
	private Directory createDirectory(){
		Directory directory = new Directory();

		//Adding parent folder to directory
		directory.add_item("..", true);

		//retrieve item names in the cwd
		File currentWorkingDirectory = new File(myPath);
		String items[] = currentWorkingDirectory.list();

		//adding every item conteined on the cwd to the directory object
		for(String itemName : items){
			boolean isFolder = new File(myPath + "/" + itemName).isDirectory();
			directory.add_item(itemName, isFolder);
		}

		return directory;
	}

	//this program will await for connection from another peer on its serverSocket (read Peer class for more detail)
	public Server(Socket clientSocket) throws UnknownHostException, IOException{
		super(clientSocket);
		myPath = System.getProperty("user.dir");
		myDirectory = createDirectory();
		sendListDirectory();
	}

	//TODO: implement
	private void sendDecryptionKey(){

	}

	private void sendDirectoryItem(int index) throws IOException{
		/*
		The function to receive the directory items (on the other peer) expects to receive, in this order, the size of the name of
		the item; a boolean telling weather or not that item is a folder; the string containing the item's name
		*/
		Item itemToBeSent = myDirectory.getItem(index);
		//name size
		out.writeInt(itemToBeSent.toString().length());
		//weather or not it is a folder
		out.writeBoolean(itemToBeSent.isFolder());
		//name
		out.writeBytes(itemToBeSent.toString());
	}
	
	//Sends the List of items in the folder on which the .class file is on
	private void sendListDirectory() throws IOException {
		myDirectory = createDirectory();
		
		for(int i=0; i < myDirectory.length(); i++) {sendDirectoryItem(i);}
	}
	
	//Sends files to the client Peer
	private void sendFile() throws IOException {
		//reading which item the other peer wants
		int request = in.readInt();
		//checking if the requested item is withing a valid range
		if(request < myDirectory.length() && request >= 0) {
			FileSender fileSender = new FileSender(clientSocket);
			File file = new File(myPath + "/" + myDirectory.getItem(request).toString());
			if(file.isDirectory()) fileSender.sendFolder(file);
			else fileSender.sendFile(file);
		}
		else {
			/*
			This flag is sent to the other peer to indicate that the file receiving function must end
			it is sent here because the requested item is invalid
			*/
			out.writeChar('E');
		}
	}

	//Navigates to a folder on the server peer
	private void navigate() throws IOException {
		//reading which folder the other peer wants to access
		int requestedFolder = in.readInt();
		//checking if the requested item is withing a valid range
		if(requestedFolder < myDirectory.length() && requestedFolder >= 0) {
			/*
			checking if the requested folder is in fact a folder and if it is within the permited
			the folders the other peer is permitted to access (i.e. if it is not up the original parent folder)
			*/
			File tempFile = new File(myPath + "/" + myDirectory.getItem(requestedFolder).toString());
			String tempPath = tempFile.getCanonicalPath();
			String limitPath = new File(System.getProperty("user.dir")).getAbsolutePath();
			if(tempPath.contains(limitPath) && tempFile.isDirectory()) {
				myPath = tempPath;
				myDirectory = createDirectory();
				//the other peer will wait for a response on weather or not his request was accepted in order to give the user feedback
				out.writeBoolean(true);
			}
			else {
				System.out.println("Other Peer tried to access beyond permitted folders");
				out.writeBoolean(false);
			}
		}
		sendListDirectory();
		
	}
//============================================== Public Side =========================================================//

	
	//Waits for handshake
	public int waitAwake() throws IOException {
		//the program will await untill the other Peer has sent an integer
		while(in.available() == 0);
		return in.readInt();
	}
	
	public void receiveRequest() throws IOException{
			int request = in.readInt();
			//System.out.println(request);
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
			case 4:
				this.close();
				System.out.println("Other Peer ended connection");
				break;
			}
	}
	
}