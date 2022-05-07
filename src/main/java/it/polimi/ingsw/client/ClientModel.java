package it.polimi.ingsw.client;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dati del client minimali che possono facilmente essere trasferiti via network
 */

public class ClientModel {
    transient Random rand = new Random();

    // Numero univoco che rappresenta l'identità del client
    // Questo perché in fase di setup i client potrebbero proporre al server dei nickname identici
    // e potrebbero avere lo stesso indirizzo IP in fase di testing sullo stesso computer
    // si rende necessario un metodo di riconoscimento del client
    private int clientIdentity;


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
    private String typeOfRequest;

    private List<AssistantCard> deck;



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





    public ClientModel(){
        fromTerminal = new ArrayList<>();
        clientIdentity = rand.nextInt(2147483647);
    }


    // Getters and Setters

    public int getNumofplayer() {
        return numofplayer;
    }

    public String getGamemode() {
        return gamemode;
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

    public void setGameStarded(Boolean gameStarded) {
        this.gameStarded = gameStarded;
    }

    public Boolean isGameStarded() {
        return gameStarded;
    }

    public int getClientIdentity() {
        return clientIdentity;
    }

    public List<AssistantCard> getDeck() {
        return deck;
    }

    public void setDeck(List<AssistantCard> deck) {
        this.deck = deck;
    }
}
