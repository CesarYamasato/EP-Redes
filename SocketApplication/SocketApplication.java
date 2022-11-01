package SocketApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

abstract class SocketApplication{
	protected DataOutputStream out;
	protected DataInputStream in;
	protected Socket clientSocket;
	protected Scanner systemIn;
	
	public SocketApplication(Socket clientSocket) throws UnknownHostException, IOException {
		this.clientSocket = clientSocket;
		out = new DataOutputStream(clientSocket.getOutputStream());
		in = new DataInputStream(clientSocket.getInputStream());
		systemIn = new Scanner(System.in);
	}
	
	public void close() throws IOException {
		clientSocket.close();
		out.close();
		in.close();
		systemIn.close();
	}
}