package src.peer;

// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerMain {
    // initialize socket and input output streams
    private Socket socket = null;
    private Scanner input = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public PeerMain() {
        System.out.println("Client started");
        input = new Scanner(System.in);
        while (true) {
            System.out.println("What do you want to do?\n\t[0] - Register\n\t[1] - Download a file");
            String inp = input.nextLine();
            switch (inp) {
                case "0":
                    try {
                        register();
                    } catch (IOException e) {
                        System.out.println("Encountered an error while registering!");
                        e.printStackTrace();
                    }
                    break;
                case "1":
                    try {
                        download();
                    } catch (IOException e) {
                        System.out.println("Encountered an error while downloading!");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Unknown option!");
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
        System.out.println("Connected!");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("register");
        // send client id
        out.writeUTF("0");

        // send file names to master
        System.out.println("Registering file names...");
        for (File file : listOfFiles) {
            out.writeUTF(file.getName());
        }

        // send end message
        out.writeUTF("end");

        System.out.println(in.readUTF());

        // close the connection
        out.close();
        socket.close();
    }

    private void download() throws UnknownHostException, IOException {
        System.out.print("File name:");
        String inp = input.nextLine();
        if (inp == null || inp == "") {
            return;
        }

        // establish a connection
        socket = new Socket("127.0.0.1", 8080);
        System.out.println("Connected!");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

        // send request type
        out.writeUTF("search");

        // send fileName
        out.writeUTF(inp);
        // takes input from the client socket
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        ArrayList<String> peers = new ArrayList<>();
        String msg = in.readUTF();
        // reads peers from client until "end" is sent
        int idx = 0;
        while (!msg.equals("end")) {
            peers.add(msg);
            System.out.println(String.format("[%d]: %s", idx, msg));
            msg = in.readUTF();
        }

        if (peers.isEmpty()) {
            System.out.println("Your requested file was not found!");
            return;
        }
        System.out.print("Which server do you want to download the file from: ");
        inp = input.nextLine();

        // TO-DO connect to peer to download
    }

    public static void main(String args[]) {
        new PeerMain();
    }
}