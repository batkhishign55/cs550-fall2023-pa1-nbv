package src.benchmark;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class TestSearch {

    private static final String[] peers = { "peer1", "peer2" };
    private static final String[] sizes = { "1KB", "1MB", "1GB" };
    private static final int[] sizeLimits = { 5, 5, 1 };
    private static final Random random = new Random();

    public static String getRandomFilename() {
        String size = sizes[random.nextInt(sizes.length)];
        int limit = sizeLimits[random.nextInt(sizeLimits.length)];
        String value = String.format("%06d", random.nextInt(limit));
        String extension = (size.equals("1KB") || size.equals("1MB")) ? ".txt" : ".bin";
        String peer = peers[random.nextInt(peers.length)];
        return size + "_" + value + "_" + peer + extension;
    }

    public TestSearch() {
    }

    public static void main(String args[]) {
        // establish a connection
        for (int i = 0; i < 1000; i++) {
            Socket socket = null;
            DataOutputStream out = null;
            DataInputStream in = null;
            try {
                socket = new Socket("127.0.0.1", 8080);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // send request type
                out.writeUTF("search");

                // send fileName
                out.writeUTF(getRandomFilename());
                String msg = in.readUTF();
                // reads peers from cis until "end" is sent
                while (!msg.equals("end")) {
                    System.out.println(msg);
                    msg = in.readUTF();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
