package socketApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public abstract class SocketApplication{
	protected DataOutputStream out;
	protected DataInputStream in;
	protected Socket clientSocket;
	protected Scanner systemIn;
	protected boolean closed;
	
	public SocketApplication(Socket clientSocket) {
		try {
			this.clientSocket = clientSocket;
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			systemIn = new Scanner(System.in);
			closed = false;
		}
		catch(UnknownHostException e) {
			System.out.println("Unnable to find Peer with specified address and port.");
			System.exit(1);
		}
		catch(IOException e) {
			try {
				System.out.println("Unnable to access other Peer's I/O stream, ending connection.");
				clientSocket.close();
				System.exit(2);
			} catch (IOException e1) {
				System.out.println("Unnable to close connection");
				System.exit(1);
			}
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void close(){
		try {
			clientSocket.close();
			out.close();
			in.close();
			systemIn.close();
			closed = true;
		}
		catch(IOException e) {
			System.out.println("Connection already closed");
		}
	}
}