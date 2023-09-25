package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import POJO.PeerInformation;

/**
 * @author: NBV
 * @since: 	09/23/2023
 * Allows user to 1>Register with CIS  2>Look up with CIS  3>Download from peer
 */

public class PeerClient {

    String CIS_HOSTNAME;
    String PEER_HOSTNAME;
    String PEER_REG_DATA;
    Socket connection;
    ObjectOutputStream out;
    ObjectInputStream in ;
    String choice;

    Scanner scan = new Scanner(System.in);

    public PeerClient() {
    	//This method is responsible for starting a thread to handle peer download requests
        peerServerThread();

        //code enters an infinite loop, prompting the user for their choice of action
        while (true) {
            System.out.println("\nEnter The Option :\n 1. Registering the File \n \n2. Searching On CentralIndxServer \n \n3. Downloading From Peer Server \n \n4. Exit\n");
            choice = scan.nextLine();
            if (choice.equals("1")) {
                RegisterWithCIS(); //Register Method call
            }
            if (choice.equals("2")) {
                SearchWithCIS(); //Search Method call
            }
            if (choice.equals("3")) {
                DownloadFromPeer(); //Download Method call 
            }
            if (choice.equals("4")) {
                System.out.println("Exiting.");
                System.exit(0);
            }
        }
    }


    //responsible for registering a file with the Central Index Server (CIS).
    public void RegisterWithCIS() {
        try {
            System.out.println("\nPlease enter CIS hostname: \n");
            CIS_HOSTNAME = scan.nextLine();
            //socket connection is established with the CIS server using the hostname and port number (2002).
            connection = new Socket(CIS_HOSTNAME, 2002);
            System.out.println("\n-----> Connected to Register with CIS on port 2002 <-----\n");
            System.out.println("\nPlease enter 4 digit Peer ID, File path and file name, separated by space:\n");
            PEER_REG_DATA = scan.nextLine();
            //registration data is written to the output stream and sent to the server.
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            out.writeObject(PEER_REG_DATA);
            out.flush();
            System.out.println("Registered Successfully!!\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method allows the user to search for a specific file on the Central Index Server (CIS)
    public void SearchWithCIS() {
        try {
            System.out.println("\nPlease enter CIS hostname: \n");
            CIS_HOSTNAME = scan.nextLine();
            //socket connection is established with the CIS server using the provided hostname and port number (2001)
            connection = new Socket(CIS_HOSTNAME, 2001);
            System.out.println("\nConnected to Search with CIS on port 2001\n");
            System.out.println("\nWhich file you want to search? \n");
            String searchFileName = scan.nextLine();
            //created to send data to the CIS server
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            out.writeObject(searchFileName);
            out.flush(); 
            //created to receive data from the CIS server
            in = new ObjectInputStream(connection.getInputStream());
            ArrayList < PeerInformation > searchResultList = (ArrayList < PeerInformation > ) in .readObject();
            //iterator it is created to iterate through the elements of searchResultList, which contains information about peers that have the requested file
            Iterator it = searchResultList.iterator();

            //If it is empty, it means that the requested file is not registered with any peer
            if (searchResultList.isEmpty()) {
                System.out.println("\n This file is not registered with any peer! \n");
                System.out.println("\n Please check the spelling of filename or try again with different name!\n");
            } else {
                System.out.println("Above file is found on following peers:");
                System.out.println("-----------------------------------------------------------------------------------");
                System.out.println("PeerId\t|Hostname\t\t|Location\t\t\t|FileName");
                System.out.println("-----------------------------------------------------------------------------------");
                while (it.hasNext()) {
                    PeerInformation peer = (PeerInformation) it.next();
                    System.out.println(peer.getPeerID() + "\t|" + peer.getPeerHostname() + "\t|" + peer.getFilePath() + "\t\t|" + searchFileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void DownloadFromPeer() {
        System.out.println("Enter Peer hostname: \n");
        PEER_HOSTNAME = scan.nextLine();
        //array will be used to store the bytes of the downloaded file
        byte[] byteArray = new byte[1024 * 64];
        try {
        	////socket connection is established with the CIS server using the provided hostname and port number (2000)
            connection = new Socket(PEER_HOSTNAME, 2000);
            System.out.println("\nConnected to peer " + PEER_HOSTNAME + " on port 2000\n");
            System.out.println("Enter filepath and filename for download : \n");
            String filePathAndName = scan.nextLine();
            //split into parts using a space as the delimiter.
            String[] str = filePathAndName.split(" ");
            String fileName = str[1];
            //created to send data to the peer server
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            out.writeObject(filePathAndName);
            out.flush(); 
            //created to receive data from the peer server
            in = new ObjectInputStream(connection.getInputStream()); 
            in .read(byteArray, 0, 64000); //reading a contents of size upto 64KB
            String home = System.getProperty("user.home"); //typically where downloaded files are stored
            FileOutputStream fos = new FileOutputStream(home + "/Downloads/" + fileName + ".txt");
            fos.write(byteArray);
            fos.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PeerClient mainFrame2 = new PeerClient();
    }

    public void peerServerThread() {
        //Peer download Request Thread
        Thread sthread = new Thread(new PeerServer(2000));
        sthread.setName("Listen For Peer Download");
        sthread.start();

    }
}