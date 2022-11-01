package FileManager;

import java.io.*;
import java.net.*;


public class FileReceiver{
	
	DataInputStream in;
	Socket socket;
	
	//Creates a FileReceiver object to receive files to the specified Socket
	public FileReceiver(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Creates a FileReceiver object and attatches to it a Socket with the specified IP address and port to recieve
	public FileReceiver(int port, InetAddress IP) {
		try {
			this.socket = new Socket(IP, port);
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Sends a file to the socket specified upen creation of the FileSender
	public void receiveFile() throws IOException {
		
		int fileNameLenght = in.readInt();
		String fileName = new String(in.readNBytes(fileNameLenght));
		
		FileOutputStream fileOut = new FileOutputStream(fileName);
		
		int fileSize = in.readInt();
		byte[] fileContent = in.readNBytes(fileSize);
		
		fileOut.write(fileContent);
		
		fileOut.close();
	}
	
	//Closes the OutputStream created with the FileSender
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
