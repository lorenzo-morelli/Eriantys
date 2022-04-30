package it.polimi.ingsw.client;

import java.util.ArrayList;

public class Model {
    private String nickname;
    private String Ip;
    private String Port;
    private int GameCodeNumber;
    private int numofplayer;
    private String Gamemode;
    private ArrayList<String> fromTerminal;

    public int getGameCodeNumber() {
        return GameCodeNumber;
    }

    public void setGamemode(String gamemode) {
        Gamemode = gamemode;
    }

    public void setNumofplayer(int numofplayer) {
        this.numofplayer = numofplayer;
    }

    public void setGameCodeNumber(int gameCodeNumber) {
        this.GameCodeNumber = gameCodeNumber;}

    public void setIp(String ip) {
        this.Ip = ip;
    }

    public void setPort(String port) {
        this.Port = port;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<String> getFromTerminal() {
        return this.fromTerminal;
    }

    public void setFromTerminal(ArrayList<String> fromTerminal) {
        this.fromTerminal = fromTerminal;
    }

    public String getIp() {
        return Ip;
    }

    public String getPort() {
        return Port;
    }


}
