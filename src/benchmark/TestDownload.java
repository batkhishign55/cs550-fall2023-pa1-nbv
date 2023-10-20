
package src.benchmark;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class TestDownload {

    private String[] peers = new String[15];
    private static final String[] sizes = { "10KB", "100MB" };
    private static final int[] sizeLimits = { 100000, 10 };
    private static final Random random = new Random();
    List<Long> timeList = new ArrayList<>();
    Properties prop;
    private int testType;

    private String getRandomFilename() {
        String size = sizes[this.testType];
        int limit = sizeLimits[this.testType];
        String value = String.format("%06d", random.nextInt(limit));
        String extension = ".txt";
        String peer = peers[random.nextInt(peers.length)];
        return size + "_" + value + "_" + peer + extension;
    }

    public TestDownload(String testType) {
        this.testType = Integer.parseInt(testType);

        prop = new Properties();
        try (FileInputStream fis = new FileInputStream("app.config")) {
            prop.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int idx = 0;
        for (int i = 0; i < 16; i++) {
            String peerId = String.format("peer%d", i + 1);
            if (!prop.getProperty("id").equals(peerId)) {
                this.peers[idx] = peerId;
                idx += 1;
                System.out.println(peerId);
            }
        }

        // record start time
        long startTime = System.nanoTime();

        // establish a connection
        for (int i = 0; i < 10000; i++) {
            this.sendRequest();
        }
        // record end time
        long endTime = System.nanoTime();

        // calculating the time in milliseconds
        long elapsedTimeMillis = (endTime - startTime) / 1000000;

        System.out.println("Total Time Taken: " + elapsedTimeMillis + "ms");
        System.out.println("Time Taken Per Request: " + this.timeList);

    }

    private void sendRequest() {

        // record start time
        long startTime = System.nanoTime();

        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;

        try {

            socket = new Socket(this.prop.getProperty("cis_address"), 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // send request type
            out.writeUTF("search");

            // send fileName
            String fileName = getRandomFilename();
            System.out.println(fileName);
            out.writeUTF(fileName);
            String peer_addr = in.readUTF();
            String msg = "";

            // reads peers from cis until "end" is sent
            while (!msg.equals("end")) {
                System.out.println(msg);
                msg = in.readUTF();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            socket.close();

            socket = new Socket(peer_addr.split("\\s+")[1], 6000);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // send fileName
            out.writeUTF(fileName);

            int bytesRead;
            byte[] buffer = new byte[1024];
            try (FileOutputStream fos = new FileOutputStream(String.format("./files/%s", fileName))) {
                while ((bytesRead = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, bytesRead);
                }
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

        // calculating the time in milliseconds
        long elapsedTimeMillis = (System.nanoTime() - startTime) / 1000000;
        this.timeList.add(elapsedTimeMillis);

    }

    public static void main(String args[]) {
        new TestDownload(args[0]);
    }
}
