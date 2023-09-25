package src.cis;
import java.util.ArrayList;

class PeerClientEntity {

    private final String peerId;
    private String hostname;
    ArrayList<String> fileNames;

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    public PeerClientEntity(String peerId) {
        this.peerId = peerId;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPeerId() {
        return this.peerId;
    }
}