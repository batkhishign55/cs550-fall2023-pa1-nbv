package src.cis;

// A Java program for a Server
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	ArrayList<PeerClientEntity> peers = new ArrayList<>();

	// constructor with port
	public Server(int port) {

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
				socket = server.accept();
				System.out.println("Client accepted");
				System.out.println(socket.getInetAddress().getHostName());

				// takes input from the client socket
				in = new DataInputStream(
						new BufferedInputStream(socket.getInputStream()));

				// first message is client id
				String id = in.readUTF();

				PeerClientEntity peer = null;

				// check if peer is already saved
				for (PeerClientEntity savedPeer : peers) {
					if (savedPeer.getPeerId().equals(id)) {
						peer = savedPeer;
						peers.remove(peer);
						break;
					}
				}

				// if peer is not found, register new one
				if (peer == null) {
					peer = new PeerClientEntity(id);
					peer.setHostname(socket.getInetAddress().getHostName());

					ArrayList<String> fileNames = new ArrayList<>();
					String fileName = "";
					// reads file names from client until "end" is sent
					while (!fileName.equals("end")) {
						fileName = in.readUTF();
						fileNames.add(fileName);
						System.out.println(fileName);
					}
					peer.setFileNames(fileNames);

					peers.add(peer);
					System.out.println("Registered new peer and files!");
				}
				// if peer is found, update file names
				else {
					peer.setHostname(socket.getInetAddress().getHostName());

					ArrayList<String> fileNames = peer.getFileNames();
					String fileName = "";
					// reads file names from client until "end" is sent
					while (!fileName.equals("end")) {
						fileName = in.readUTF();
						if (!fileNames.contains(fileName)) {
							fileNames.add(fileName);
							System.out.println(fileName);
						}
					}
					peers.add(peer);
					System.out.println("Updated peer files!");
				}

				System.out.println("Closing connection");

				// close connection
				socket.close();
				in.close();
			} catch (IOException i) {
				System.out.println(i);
			}
		}
	}

	public static void main(String args[]) {
		new Server(8080);
	}
}
