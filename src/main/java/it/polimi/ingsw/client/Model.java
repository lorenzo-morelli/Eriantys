package it.polimi.ingsw.client;

import java.util.ArrayList;

public class Model {
    private String nickname;
    private String Ip;
    private String Port;
    private int GameCodeNumber;
    private int numofplayer;
    private String Gamemode;
    private int CardChoosed;
    private int Student_in_entrance_Choosed;
    private int Island_Choosed;
    private ArrayList<String> fromTerminal;

    public int getIsland_Choosed() {
        return Island_Choosed;
    }

    public void setIsland_Choosed(int island_Choosed) {
        Island_Choosed = island_Choosed;
    }

    public int getStudent_in_entrance_Choosed() {
        return Student_in_entrance_Choosed;
    }

    public void setStudent_in_entrance_Choosed(int student_in_entrance_Choosed) {
        Student_in_entrance_Choosed = student_in_entrance_Choosed;
    }

    public int getCardChoosed() {
        return CardChoosed;
    }

    public String getGamemode() {
        return Gamemode;
    }

    public void setCardChoosed(int cardChoosed) {
        CardChoosed = cardChoosed;
    }

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
