package FileManager;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Encryption.EncryptionException.NoKeyException;
import Encryption.Symmetric.SymmetrictDecryptor;


public class FileReceiver{
	//TODO: add decryption
	SymmetrictDecryptor decryptor;
	DataInputStream in;
	Socket socket;
	String myPath;
	
	//Creates a FileReceiver object to receive files to the specified Socket
	public FileReceiver(Socket socket, SymmetrictDecryptor decryptor) {
		this.socket = socket;
		this.decryptor = decryptor;
		try {
			in = new DataInputStream(socket.getInputStream());
			myPath = System.getProperty("user.dir");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Creates a FileReceiver object and attatches to it a Socket with the specified IP address and port to recieve
	public FileReceiver(int port, InetAddress IP, SymmetrictDecryptor decryptor) {
		try {
			this.socket = new Socket(IP, port);
			this.decryptor = decryptor;
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String receiveFileName() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
		int fileNameLenght = in.readInt();
		byte[] encryptedFileName = in.readNBytes(fileNameLenght);
		String fileName = new String(decryptor.decrypt(encryptedFileName));
		return fileName;
	}

	private void receiveFileContents(String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException{
		new File (myPath + "/" + fileName).createNewFile();
		FileOutputStream fileOut = new FileOutputStream(myPath + "/" + fileName);
		
		int fileSize = in.readInt();
		byte[] encryptedFileContents = in.readNBytes(fileSize);
		byte[] fileContents = decryptor.decrypt(encryptedFileContents);
		
		fileOut.write(fileContents);
		
		fileOut.close();
	}
	
	private void receiveFolder() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
		/*
		Recursively receive each item on a folder, flags determine weather or not all the files/folders on the original folder ware received
		E = end of folder
		N = new file to receive
		D = new folder to receive
		*/
		char flag = in.readChar();
		//System.out.println(flag);
		//TODO: make "myPath" get back to being absolute path after each folder is received to avoid string size limit and overflow
		if(flag == 'E') {
			myPath = myPath + "../";
			return;
		}
		else if(flag == 'N')ReceiveFileFromFolder(); //TODO: check if this can be 'ReceiveFile()'
		else if(flag == 'D') {
			String fileName = receiveFileName();
			myPath = myPath + "/" + fileName + "/";
			new File(myPath).mkdirs();
			receiveFolder();
		}
		receiveFolder();
	}
	
	//this method is only ever called when a file is to be received from a folder
	private void ReceiveFileFromFolder() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
		String fileName = receiveFileName();
		receiveFileContents(fileName);
		return;
	}
	
	//Sends a file to the socket specified upen creation of the FileSender
	public void receiveFile() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoKeyException {
		/*
		For information about flags used, read method "receiveFolder". In addition to those flags, this method may receive the flag
		F = File from the sender, which means that only one file was sent.
		*/
		char flag = in.readChar();
		//this flag is only ever received if the requested file was outside the valid range
		if(flag == 'E') {
			return;
		}

		String fileName = receiveFileName();

		//a folder is received, so it is necessary to create that folder and "navigate" to it
		if(flag == 'D') {
			//"navigating" to the folder to be received
			myPath = myPath + "/" + fileName + "/";
			//creating the folder
			new File(myPath).mkdirs();
			receiveFolder();
			myPath = System.getProperty("user.dir");
			return;
		}

		//IF the flag received was F (i.e. the item received was a single file)
		receiveFileContents(fileName);

		//this flag indicates there is another file coming after this one, so the function to receive the file must be called again
		if(flag == 'N') receiveFile();
	}
}