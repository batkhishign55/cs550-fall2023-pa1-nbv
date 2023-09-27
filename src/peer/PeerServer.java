package src.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                System.out.println(String.format("[Server]: Requested file name: %s", fileName));

                byte[] bytes = Files.readAllBytes(Paths.get("./files/", fileName));

                out.flush();
                out.write(bytes);

                socket.close();
                in.close();
                out.close();

            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }
}
