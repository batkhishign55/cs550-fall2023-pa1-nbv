package src.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerClient extends Thread {

    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private Scanner input = null;
    private ArrayList<String> peers = new ArrayList<>();
    private String fileToObtain = "";
    private String cisAddress = "";

    public PeerClient(String cisAddress) {
        this.cisAddress = cisAddress;
    }

    @Override
    public void run() {

        input = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "[Client]: What do you want to do?\n\t[0] - Register\n\t[1] - Search a file\n\t[2] - Obtain a file");
            String inp = input.nextLine();
            switch (inp) {
                case "0":
                    try {
                        register();
                    } catch (IOException e) {
                        System.out.println("[Client]: Encountered an error while registering!");
                        e.printStackTrace();
                    }
                    break;
                case "1":
                    try {
                        search();
                    } catch (IOException e) {
                        System.out.println("[Client]: Encountered an error while searching!");
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        obtain();
                    } catch (IOException e) {
                        System.out.println("[Client]: Encountered an error while obtaining!");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("[Client]: Unknown option!");
                    break;
            }
        }
    }

    private void register() throws IOException {

        // get all file names to register
        File folder = new File("./files");
        File[] listOfFiles = folder.listFiles();

        // establish a connection
        socket = new Socket(cisAddress, 8080);
        System.out.println("[Client]: Connected!");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // ask peer id
        System.out.print("[Client]: Peer ID:");
        String peerId = input.nextLine();

        // send request type
        out.writeUTF("register");
        // send client id
        out.writeUTF(peerId);

        // send file names to master
        for (File file : listOfFiles) {
            out.writeUTF(file.getName());
        }

        // send end message
        out.writeUTF("end");

        System.out.println("[Client]: " + in.readUTF());

        // close the connection
        out.close();
        socket.close();
    }

    private void search() throws UnknownHostException, IOException {

        // record start time
        long startTime = System.nanoTime();

        System.out.print("[Client]: File name:");
        String fileName = input.nextLine();
        if (fileName == null || fileName == "") {
            return;
        }

        // establish a connection
        socket = new Socket(cisAddress, 8080);
        System.out.println("[Client]: Connected!");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("search");

        // send fileName
        out.writeUTF(fileName);

        String msg = in.readUTF();
        // reads peers from cis until "end" is sent

        this.fileToObtain = fileName;
        this.peers.clear();
        while (!msg.equals("end")) {
            System.out.println(String.format("[Client]: %s", msg));
            this.peers.add(msg);
            msg = in.readUTF();
        }

        // record end time
        long endTime = System.nanoTime();

        // calculating the time in milliseconds
        long elapsedTimeMillis = (endTime - startTime) / 1000000;

        System.out.println("Time Taken: " + elapsedTimeMillis + "ms");

    }

    private void obtain() throws UnknownHostException, IOException {

        // record start time
        long startTime = System.nanoTime();

        if (this.fileToObtain == "") {
            System.out.println("[Client]: Please search a file first!");
            return;
        }

        String peer = "";
        if (peers.size() == 0) {
            System.out.println("[Client]: Your requested file is not found!");
            return;
        } else if (peers.size() == 1) {
            peer = peers.get(0);
        } else {
            for (String speer : this.peers) {
                System.out.println(String.format("[Client]: %s", speer));
            }
            System.out.print("[Client]: Choose a server from above: ");
            String peerAddr = input.nextLine();

            for (String speer : this.peers) {
                System.out.println(speer.split("\\s+")[0]);
                if (speer.split("\\s+")[0].equals(peerAddr)) {
                    peer = speer;
                }
            }
        }

        System.out.println(peer);

        if (peer == "") {
            System.out.print("[Client]: Invalid option!");
        }

        // establish a connection
        String[] splited = peer.split("\\s+");
        socket = new Socket(splited[1], 6000);
        System.out.println("[Client]: Connected!");

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // send fileName
        out.writeUTF(fileToObtain);

        int bytesRead;
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(String.format("./%s", fileToObtain))) {
            while ((bytesRead = in.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        // record end time
        long endTime = System.nanoTime();

        // calculating the time in milliseconds
        long elapsedTimeMillis = (endTime - startTime) / 1000000;

        System.out.println("Time Taken: " + elapsedTimeMillis + "ms");

    }
}
