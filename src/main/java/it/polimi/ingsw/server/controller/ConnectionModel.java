package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.model.ClientModel;

import java.util.ArrayList;

/**
 * This class is needed to store the data sent from Clients over network.
 * Is the de-facto data model where the server has to store the information of
 * every player connected to the game and the current state of their view, their
 * connection, and other basic data as nickname and ip address.
 * @author Fernando Morea
 */

public class ConnectionModel {
    private ArrayList<ClientModel> clientsInfo;

    private boolean closeThread = false;

    public ConnectionModel() {
        clientsInfo = new ArrayList<>();
    }

    /**
     * @return the array that contain the Models of the clients
     */
    public ArrayList<ClientModel> getClientsInfo() {
        return clientsInfo;
    }

    /**
     * @return Number of Player
     */
    public int getNumOfPlayers() {
        return clientsInfo.toArray().length;
    }

    /**
     * @return Game Mode
     */
    public String getGameMode() {
        return clientsInfo.get(0).getGameMode();
    }

    /**
     * This method is able to retrive the Model of the player from the ConnectionModel by giving its nickname
     *
     * @param nickname of the player
     * @return Model of the client
     */
    public ClientModel findPlayer(String nickname) {
        for (ClientModel c : clientsInfo) {
            if (c.getNickname().equals(nickname)) {
                return c;
            }
        }
        return null;
    }

    /**
     * This method is able to replace old Model with new Model (useful for reconnection)
     *
     * @param target old Model
     * @param newModel  new Model
     */
    public void change(ClientModel target, ClientModel newModel) {
        clientsInfo.removeIf(c -> c.equals(target));
        clientsInfo.add(newModel);
    }

    /**
     * Set the array that contain the Models ot the clients
     */
    public void setClientsInfo(ArrayList<ClientModel> clientsInfo) {
        this.clientsInfo = clientsInfo;
    }

    /**
     * Reinitialize Connection Model as empty
     */
    public void empty() {
        clientsInfo.clear();
    }

    /**
     * Close ConnectionModel and reinitialize it
     */
    public void close() {
        empty();
        closeThread = true;
    }

    /**
     * @return true is Thread for connections during the game is closed (Thread in Create4PlayerGame for 4 players or in Create Game for 3/2 players
     */
    public boolean isCloseThread() {
        return closeThread;
    }

    /**
     * Set the flag to manage Thread for connections during the game (Thread in Create4PlayerGame for 4 players or in Create Game for 3/2 players
     */
    public void setCloseThread(boolean closeThread) {
        this.closeThread = closeThread;
    }
}
