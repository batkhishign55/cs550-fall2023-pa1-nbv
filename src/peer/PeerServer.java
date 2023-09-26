package src.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerServer extends Thread {

    private ServerSocket server = null;

    @Override
    public void run() {

        try {
            server = new ServerSocket(6000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // starts server and waits for a connection
        while (true) {
            try {
                System.out.println("[Server]: Waiting for a peer ...");
                Socket socket = server.accept();
                System.out.println("[Server]: Peer accepted");

                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String fileName = in.readUTF();
                out.writeUTF("found");

            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }
}
