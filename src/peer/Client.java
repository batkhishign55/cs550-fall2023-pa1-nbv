package src.peer;

// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private Scanner input = null;
    private DataOutputStream out = null;

    public Client() {
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
        System.out.println("Connecting to the master..");
        socket = new Socket("127.0.0.1", 8080);
        System.out.println("Connected!");

        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

        // send client id
        out.writeUTF("0");

        // send file names to master
        System.out.println("Registering file names...");
        for (File file : listOfFiles) {
            out.writeUTF(file.getName());
        }

        // send end message
        out.writeUTF("end");
        System.out.println("Success!");

        // close the connection
        out.close();
        socket.close();
    }

    private void download() {
        // TO-DO
    }

    public static void main(String args[]) {
        new Client();
    }
}