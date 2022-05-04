package it.polimi.ingsw.client;

import java.util.ArrayList;

/**
 * Dati del client minimali che possono facilmente essere trasferiti via network
 */

public class ClientModel {
    // Am I the first client connected to server?
    // amIfirst: true = Identificato come primo,
    // false = identificato come non primo,
    // null = non ancora identificato
    private Boolean amIfirst = null;

    // Gestione dei possibili messaggi, si veda CliView
    // Un messaggio o è una richiesta (del server ad un client di avere degli input)
    // o è una risposta di un client al server

    // isResponse --> è una risposta di un client al server
    // !isResponse --> è una richiesta del server al client
    // isresponse == null, non è ne una richiesta ne una risposta, probabilmente un messaggio di quelli iniziali
    private Boolean isResponse = false;

    private Boolean gameStarded = false;
    private String responseDescription;
    private String typeOfRequest;
    private String requestDescription;



    // where to put data that comes from terminal
    private ArrayList<String> fromTerminal;


    // My nickname
    private String nickname;


    // server IP and port
    private String Ip;
    private String Port;



    // my IP address
    private String myIp;

    // Number of players
    private int numofplayer;


    // Game mode (PRINCIPIANT or EXPERT)
    private String gamemode;



    private int CardChoosed;
    private int Student_in_entrance_Choosed;
    private int Island_Choosed;
    private int Mother_movement_Choosed;
    private int CloudChoosed;




    public ClientModel(){
        fromTerminal = new ArrayList<>();
    }


    // Getters and Setters

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
        return gamemode;
    }

    public void setCardChoosed(int cardChoosed) {
        CardChoosed = cardChoosed;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void setNumofplayer(int numofplayer) {
        this.numofplayer = numofplayer;
    }
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

    public String getTypeOfRequest() {
        return typeOfRequest;
    }

    public void setTypeOfRequest(String typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }

    public Boolean isResponse() {
        return isResponse;
    }

    public void setResponse(Boolean response) {
        isResponse = response;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public void setGameStarded(Boolean gameStarded) {
        this.gameStarded = gameStarded;
    }

    public Boolean isGameStarded() {
        return gameStarded;
    }
}
