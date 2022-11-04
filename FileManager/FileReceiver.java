package FileManager;

import java.io.*;
import java.net.*;


public class FileReceiver{
	
	DataInputStream in;
	Socket socket;
	String myPath;
	
	//Creates a FileReceiver object to receive files to the specified Socket
	public FileReceiver(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			myPath = System.getProperty("user.dir");
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
	
	private String receiveFileName() throws IOException {
		int fileNameLenght = in.readInt();
		String fileName = new String(in.readNBytes(fileNameLenght));
		return fileName;
	}
	
	private void receiveFolder() throws IOException {
		char flag = in.readChar();
		System.out.println(flag);
		if(flag == 'E') {
			myPath = myPath + "../";
			return;
		}
		else if(flag == 'N')receiveFile(flag);
		else if(flag == 'D') {
			String fileName = receiveFileName();
			myPath = myPath + "/" + fileName + "/";
			new File(myPath).mkdirs();
			receiveFolder();
		}
		receiveFolder();
	}
	
	private void receiveFile(char flag) throws IOException {
		//System.out.println(flag);
		String fileName = receiveFileName();
		System.out.println(fileName);

		new File (myPath + fileName).createNewFile();
		FileOutputStream fileOut = new FileOutputStream(myPath + fileName);
		
		int fileSize = in.readInt();
		byte[] fileContent = in.readNBytes(fileSize);
		
		fileOut.write(fileContent);
		
		fileOut.close();
		return;
	}
	
	//Sends a file to the socket specified upen creation of the FileSender
	public void receiveFile() throws IOException {
		char flag = in.readChar();
		System.out.println(flag);
		if(flag == 'E') {
			myPath = myPath + "../";
			return;
		}
		String fileName = receiveFileName();
		System.out.println(fileName);
		if(flag == 'D') {
			myPath = myPath + "/" + fileName + "/";
			new File(myPath).mkdirs();
			receiveFolder();
			myPath = System.getProperty("user.dir");
			return;
		}
		new File (myPath + fileName).createNewFile();
		FileOutputStream fileOut = new FileOutputStream(myPath + fileName);
		
		int fileSize = in.readInt();
		byte[] fileContent = in.readNBytes(fileSize);
		
		fileOut.write(fileContent);
		
		fileOut.close();
		if(flag == 'N') receiveFile();
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