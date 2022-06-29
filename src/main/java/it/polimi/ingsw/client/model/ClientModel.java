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
    private int clientIdentity;
    private Boolean amFirst = null;
    private Boolean isResponse = false;
    private boolean isPingMessage;

    private Boolean gameStarted = false;
    public boolean kicked = false, reply = true;
    private String typeOfRequest;

    private PeopleColor chosenColor;
    private float cardChosenValue;
    private int chosenMoves;
    private Cloud cloudChosen;
    private int chosenIsland;

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

    public void setNumOfPlayers(int numOfPlayer) {
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

    public Boolean getAmFirst() {
        return amFirst;
    }

    public void setAmFirst(Boolean amFirst) {
        this.amFirst = amFirst;
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

    public PeopleColor getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(PeopleColor chosenColor) {
        this.chosenColor = chosenColor;
    }

    public int getChosenIsland() {
        return chosenIsland;
    }

    public void setChosenIsland(int chosenIsland) {
        this.chosenIsland = chosenIsland;
    }

    public Model getServerModel() {
        return serverModel;
    }

    public void setServerModel(Model serverModel) {
        this.serverModel = serverModel;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public int getChosenMoves() {
        return chosenMoves;
    }

    public void setChosenMoves(int chosenMoves) {
        this.chosenMoves = chosenMoves;
    }

    public Cloud getCloudChosen() {
        return cloudChosen;
    }

    public void setCloudChosen(Cloud cloudChosen) {
        this.cloudChosen = cloudChosen;
    }

    public String getGameWinner() {
        return gameWinner;
    }

    public void setGameWinner(String gameWinner) {
        this.gameWinner = gameWinner;
    }

    public float getCardChosenValue() {
        return cardChosenValue;
    }

    public void setCardChosenValue(float cardChosenValue) {
        this.cardChosenValue = cardChosenValue;
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
