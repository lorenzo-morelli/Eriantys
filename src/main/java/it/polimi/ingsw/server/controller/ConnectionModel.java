package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.model.ClientModel;

import java.util.ArrayList;

/**
 * This class is needed to store the data sent from Clients over network.
 * Is the de-facto data model that the server has to store the information of
 * every player connected to the game and the current state of their view, their
 * connection, and other basic data as nickname and ip address.
 */

public class ConnectionModel {
    private ArrayList<ClientModel> clientsInfo;

    private boolean closeThred=false;

    public ConnectionModel() {
        clientsInfo = new ArrayList<>();
    }

    public ArrayList<ClientModel> getClientsInfo() {
        return clientsInfo;
    }

    public int getNumOfPlayers() {
        return clientsInfo.toArray().length;
    }

    public String getGameMode() {
        return clientsInfo.get(0).getGameMode();
    }

    public ClientModel findPlayer(String nickname) {
        for (ClientModel c : clientsInfo) {
            if (c.getNickname().equals(nickname)) {
                return c;
            }
        }
        return null;
    }

    public void change(ClientModel target, ClientModel nuovo){
        clientsInfo.removeIf(c -> c.equals(target));
        clientsInfo.add(nuovo);
    }
    public void setClientsInfo(ArrayList<ClientModel> clientsInfo) {
        this.clientsInfo = clientsInfo;
    }

    public void empty(){
        clientsInfo.clear();
    }

    public void close(){
        empty();
        closeThred=true;
    }


    public boolean isCloseThred() {
        return closeThred;
    }

    public void setCloseThred(boolean closeThred) {
        this.closeThred = closeThred;
    }
}
