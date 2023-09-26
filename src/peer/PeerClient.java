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
                        System.out.println("[Client]: Encountered an error while downloading!");
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        obtain();
                    } catch (IOException e) {
                        System.out.println("[Client]: Encountered an error while downloading!");
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
        socket = new Socket("127.0.0.1", 8080);
        System.out.println("[Client]: Connected!");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("register");
        // send client id
        out.writeUTF("0");

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
        System.out.print("[Client]: File name:");
        String fileName = input.nextLine();
        if (fileName == null || fileName == "") {
            return;
        }

        // establish a connection
        socket = new Socket("127.0.0.1", 8080);
        System.out.println("[Client]: Connected!");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("search");

        // send fileName
        out.writeUTF(fileName);

        ArrayList<String> peers = new ArrayList<>();
        String msg = in.readUTF();
        // reads peers from client until "end" is sent
        int idx = 0;
        while (!msg.equals("end")) {
            peers.add(msg);
            System.out.println(String.format("[Client]: [%d]: %s", idx, msg));
            msg = in.readUTF();
        }
    }

    private void obtain() throws UnknownHostException, IOException {
        System.out.print("[Client]: File name:");
        String fileName = input.nextLine();
        if (fileName == null || fileName == "") {
            return;
        }

        // establish a connection
        socket = new Socket("127.0.0.1", 8080);
        System.out.println("[Client]: Connected!");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("search");

        // send fileName
        out.writeUTF(fileName);

        ArrayList<String> peers = new ArrayList<>();
        String msg = in.readUTF();
        // reads peers from client until "end" is sent
        int idx = 0;
        while (!msg.equals("end")) {
            peers.add(msg);
            System.out.println(String.format("[Client]: [%d]: %s", idx, msg));
            msg = in.readUTF();
        }

        if (peers.isEmpty()) {
            System.out.println("[Client]: Your requested file was not found!");
            return;
        }
        System.out.print("[Client]: Which server do you want to download the file from: ");
        String server = input.nextLine();

        // establish a connection
        socket = new Socket("127.0.0.1", 6000);
        System.out.println("[Client]: Connected!");

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // send fileName
        out.writeUTF(fileName);

        int bytesRead;
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(String.format("./%s", fileName))) {
            while ((bytesRead = in.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}
