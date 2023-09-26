package src.cis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Socket socket;
    private Server server;

    // Constructor
    public ClientHandler(Server server, Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.server = server;
    }

    @Override
    public void run() {

        // first message is request type
        try {
            String reqType = in.readUTF();

            switch (reqType) {
                case "register":
                    this.handleRegister();
                    break;

                case "search":
                    this.handleSearch();
                    break;

                default:
                    out.writeUTF("Invalid request");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // closing resources
            this.in.close();
            this.out.close();
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleRegister() throws IOException {

        // read client id
        String id = in.readUTF();

        PeerClientEntity peer = null;
        ArrayList<PeerClientEntity> peers = server.getPeers();
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
            out.writeUTF("Registered new peer and files!");
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
            out.writeUTF("Updated peer files!");
        }
    }

    private void handleSearch() throws IOException {

        // read file to search
        String target = in.readUTF();

        ArrayList<PeerClientEntity> peers = server.getPeers();

        // search for the file in each peers
        for (PeerClientEntity savedPeer : peers) {
            for (String fileName : savedPeer.getFileNames()) {
                if (fileName.equals(target)) {
                    out.writeUTF(savedPeer.getPeerId());
                }
            }
        }
        out.writeUTF("end");
    }
}
