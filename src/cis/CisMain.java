package src.cis;

// A Java program for a Server
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class CisMain {
	// initialize socket and input stream
	private ServerSocket server = null;
	ArrayList<PeerClientEntity> peers = new ArrayList<>();
	public static final Object lock = new Object();

	public ArrayList<PeerClientEntity> getPeers() {
		synchronized (lock) {
			return this.peers;
		}
	}

	public void setPeers(ArrayList<PeerClientEntity> peers) {
		synchronized (lock) {
			this.peers = peers;
		}
	}

	// constructor with port
	public CisMain(int port) {

		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Server started");
		// starts server and waits for a connection
		while (true) {
			try {
				System.out.println("Waiting for a client ...");
				Socket socket = server.accept();
				System.out.println("Client accepted");

				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				Thread clientThread = new ClientHandler(this, socket, in, out);
				// starting
				clientThread.start();

			} catch (IOException i) {
				System.out.println(i);
			}
		}
	}

	public static void main(String args[]) {
		new CisMain(8080);
	}
}
