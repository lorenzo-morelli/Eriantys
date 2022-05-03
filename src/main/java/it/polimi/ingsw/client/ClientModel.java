package it.polimi.ingsw.client;

import java.util.ArrayList;

public class ClientModel {
    private String nickname;

    // server Ip and port
    private String Ip;
    private String Port;
    //
    private String myIp;
    private int GameCodeNumber;
    private int numofplayer;
    private String Gamemode;
    private int CardChoosed;
    private int Student_in_entrance_Choosed;
    private int Island_Choosed;
    private int Mother_movement_Choosed;
    private int CloudChoosed;

    private Boolean amIfirst = null;
    private ArrayList<String> fromTerminal;

    public ClientModel(){
        fromTerminal = new ArrayList<>();
    }

    public int getCloudChoosed() {
        return CloudChoosed;
    }

    public int getNumofplayer() {
        return numofplayer;
    }

    public void setCloudChoosed(int cloudChoosed) {
        CloudChoosed = cloudChoosed;
    }

    public int getMother_movement_Choosed() {
        return Mother_movement_Choosed;
    }

    public void setMother_movement_Choosed(int mother_movement_Choosed) {
        Mother_movement_Choosed = mother_movement_Choosed;
    }

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

    public Boolean getAmIfirst() {
        return amIfirst;
    }

    public void setAmIfirst(Boolean amIfirst) {
        this.amIfirst = amIfirst;
    }

    public String getMyIp() {
        return myIp;
    }

    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }
}
