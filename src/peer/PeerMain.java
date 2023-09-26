package src.peer;

public class PeerMain {
    // initialize socket and input output streams

    public PeerMain() {
        System.out.println("Peer started!");

        Thread clientThread = new PeerClient();
        clientThread.start();

        Thread serverThread = new PeerServer();
        serverThread.start();
    }

    public static void main(String args[]) {
        new PeerMain();
    }
}