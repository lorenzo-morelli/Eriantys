package it.polimi.ingsw.server.model;

import java.util.ArrayList;

public class ConnectionInfo {
    private ArrayList<IpAndNickame> ipAndNickames;
    private String newIp;
    private String newNickname;

    public ConnectionInfo(){
        ipAndNickames = new ArrayList<>();
    }

    public ArrayList<IpAndNickame> getIpAndNickames() {
        return ipAndNickames;
    }

    public void setIpAndNickames(ArrayList<IpAndNickame> ipAndNickames) {
        this.ipAndNickames = ipAndNickames;
    }

    public String getNewIp() {
        return newIp;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public void setNewIp(String newIp) {
        this.newIp = newIp;
    }

    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }
}
