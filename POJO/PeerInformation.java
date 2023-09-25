package POJO;

import java.io.Serializable;
import java.util.ArrayList;

/**
* @author: NBV
* @since: 	09/23/2023
* A POJO that stores information of registered peer
*/

public class PeerInformation implements Serializable{
String peer_Host_Name;
String file_Path;
ArrayList<String> file_Names; 
long peer_ID;


// getter function to get the PeerID
public long getPeerID() {
	return peer_ID;
}

// setter function to set the PeerID
public void setPeerID(long peer_ID) {
	this.peer_ID = peer_ID;
}

// getter function to get the FileName
public ArrayList<String> getFileNames() {
	return file_Names;
}
// setter function to set the FileName
public void setFileNames(ArrayList<String> file_Names) {
	this.file_Names = file_Names;
}

// getter function to get the Peer_Host_Name
public String getPeerHostname() {
	return peer_Host_Name;
}

// setter function to set the Peer_Host_Name
public void setPeerHostname(String peer_Host_Name) {
	this.peer_Host_Name = peer_Host_Name;
}

//getter function to get the File_Path
public String getFilePath() {
	return file_Path;
}

//setter function to set the File_Path
public void setFilePath(String file_Path) {
	this.file_Path = file_Path;
}



}