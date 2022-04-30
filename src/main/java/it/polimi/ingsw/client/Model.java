package it.polimi.ingsw.client;

import java.util.ArrayList;

public class Model {
    private String nickname;
    private String Ip;
    private String Port;

    private int GameCodeNumber;



    private ArrayList<String> fromTerminal;

    public void setIp(String ip) {
        Ip = ip;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return Ip;
    }

    public String getPort() {
        return Port;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<String> getFromTerminal() {
        return fromTerminal;
    }

    public void setFromTerminal(ArrayList<String> fromTerminal) {
        this.fromTerminal = fromTerminal;
    }
}
