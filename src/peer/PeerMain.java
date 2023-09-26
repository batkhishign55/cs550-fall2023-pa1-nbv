package src.peer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PeerMain {
    // initialize socket and input output streams

    public PeerMain() {
        System.out.println("Peer started!");

        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("app.config")) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        Thread clientThread = new PeerClient(prop.getProperty("cis_address"));
        clientThread.start();
        Thread serverThread = new PeerServer();
        serverThread.start();
    }

    public static void main(String args[]) {
        new PeerMain();
    }
}