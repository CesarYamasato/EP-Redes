import java.io.*;
import java.net.*;
import socketApplication.*;

class waitingPeer{
	private int port;
	private String nickname;
	private String address;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public waitingPeer(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		in = new DataInputStream(clientSocket.getInputStream());
		out  = new DataOutputStream(clientSocket.getOutputStream());
		port = in.readInt();
		int nicknameSize = in.readInt();
		nickname = new String(in.readNBytes(nicknameSize));
		address = clientSocket.getInetAddress().toString().substring(1);
	}
	
	public int getPort() {
		return port;
	}
	
	public String getAddress() {
		return address;
	}
}

class connectingPeers{
	private int port;
	private String nickname;
	private String address;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public connectingPeers(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		in = new DataInputStream(clientSocket.getInputStream());
		out  = new DataOutputStream(clientSocket.getOutputStream());
		port = in.readInt();
		int nicknameSize = in.readInt();
		nickname = new String(in.readNBytes(nicknameSize));
		address = clientSocket.getInetAddress().toString().substring(1);
	}
	public int getPort() {
		return port;
	}
	
	public String getAddress() {
		return address;
	}
}

public class Intermediate{
	private ServerSocket[] connectionSockets;
	private ServerSocket[] serverSockets;
	private waitingPeer[] waitingPeers;
	private int numberSockets;
	private int numberWaitPeers;
	
	public Intermediate(int min, int max) throws IOException {;
		serverSockets = new ServerSocket[min-max+1];
		connectionSockets = new ServerSocket[min-max+1];
		waitingPeers = new waitingPeer[serverSockets.length];
		numberWaitPeers = 0;
		numberSockets = serverSockets.length;
		for(int i = min; i < max; i++) serverSockets[i] = new ServerSocket(i);
	}
	public void receiveConnectionWait() throws IOException {
		for(int i = 0; i < numberSockets; i++) {
			if(!serverSockets[i].isBound()) {
				Socket clientSocket = serverSockets[i].accept();
				waitingPeers[numberWaitPeers] = new waitingPeer(clientSocket); 
				numberWaitPeers++;
			}
		}
	}
	public void receiveConnection() throws IOException {
		for(int i = 0; i < numberSockets; i++) {
			if(!connectionSockets[i].isBound()) {
				Socket clientSocket = connectionSockets[i].accept();
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				for(int j = 0; j < numberWaitPeers; j++) {
					out.writeChar('N');
					out.writeInt(waitingPeers[j].getPort());
					out.writeInt(waitingPeers[j].getAddress().length());
					out.writeBytes(waitingPeers[j].getAddress());
				}
				out.writeChar('E');
			}
		}
	}
}