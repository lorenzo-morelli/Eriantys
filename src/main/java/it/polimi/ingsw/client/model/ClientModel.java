package it.polimi.ingsw.client.model;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dati del client minimali che possono facilmente essere trasferiti via network
 */

public class ClientModel {
    final transient Random rand = new Random();

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
    // Un messaggio o è una richiesta (del server a un client di avere degli inputs)
    // o è una risposta di un client al server

    // isResponse --> è una risposta di un client al server
    // !isResponse --> è una richiesta del server al client
    // isResponse == null, non è ne una richiesta ne una risposta, probabilmente un messaggio di quelli iniziali
    private Boolean isResponse = false;
    private boolean isPingMessage; //differenzia request di ping da request di gioco

    private Boolean gameStarted = false;
    public boolean kicked = false, reply = true;
    private String typeOfRequest;

    private PeopleColor choosedColor;
    private float cardChoosedValue;
    private int choosedMoves;
    private Cloud cloudChoosed;
    private int choosedIsland;

    private String gameWinner;

    private Model serverModel;
    private List<AssistantCard> deck;

    // where to put data that comes from terminal
    private ArrayList<String> fromTerminal;

    private final ArrayList<String> nicknames;

    private String nickname;

    // server IP and port
    private String ip;
    private String port;

    // my IP address
    private String myIp;

    // Number of players
    private int numOfPlayer;


    // Game mode (PRINCIPIANT or EXPERT)
    private String gameMode;
    private ArrayList<PeopleColor> colors1;
    private ArrayList<PeopleColor> colors2;


    public ClientModel() {
        fromTerminal = new ArrayList<>();
        nicknames = new ArrayList<>();
        clientIdentity = rand.nextInt(2147483647);
    }


    // Getters and Setters

    public int getNumOfPlayers() {
        return numOfPlayer;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setNumofplayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ArrayList<String> getFromTerminal() {
        return this.fromTerminal;
    }

    public void setFromTerminal(ArrayList<String> fromTerminal) {
        this.fromTerminal = fromTerminal;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
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

    public void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public Boolean isGameStarted() {
        return gameStarted;
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

    public PeopleColor getChoosedColor() {
        return choosedColor;
    }

    public void setChoosedColor(PeopleColor choosedColor) {
        this.choosedColor = choosedColor;
    }

    public int getChoosedIsland() {
        return choosedIsland;
    }

    public void setChoosedIsland(int choosedIsland) {
        this.choosedIsland = choosedIsland;
    }

    public Model getServermodel() {
        return serverModel;
    }

    public void setServermodel(Model serverModel) {
        this.serverModel = serverModel;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public int getChoosedMoves() {
        return choosedMoves;
    }

    public void setChoosedMoves(int choosedMoves) {
        this.choosedMoves = choosedMoves;
    }

    public Cloud getCloudChoosed() {
        return cloudChoosed;
    }

    public void setCloudChoosed(Cloud cloudChoosed) {
        this.cloudChoosed = cloudChoosed;
    }

    public String getGameWinner() {
        return gameWinner;
    }

    public void setGameWinner(String gameWinner) {
        this.gameWinner = gameWinner;
    }

    public float getCardChoosedValue() {
        return cardChoosedValue;
    }

    public void setCardChoosedValue(float cardChoosedValue) {
        this.cardChoosedValue = cardChoosedValue;
    }

    public ArrayList<PeopleColor> getColors1() {
        return colors1;
    }

    public void setColors1(ArrayList<PeopleColor> colors1) {
        this.colors1 = colors1;
    }

    public ArrayList<PeopleColor> getColors2() {
        return colors2;
    }

    public void setColors2(ArrayList<PeopleColor> colors2) {
        this.colors2 = colors2;
    }

    public boolean isNotKicked() {
        return !kicked;
    }

    public void setKicked(boolean kicked) {
        this.kicked = kicked;
    }

    public boolean isPingMessage() {
        return isPingMessage;
    }

    public void setPingMessage(boolean pingMessage) {
        isPingMessage = pingMessage;
    }

    public void setReply(boolean p) {
        reply = p;
    }

    public boolean getReply() {
        return reply;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setClientIdentity(int clientIdentity) {
        this.clientIdentity = clientIdentity;
    }
}
