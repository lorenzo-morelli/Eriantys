package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.model.ClientModel;

import java.util.ArrayList;

/**
 * This class is needed to store the data sent from Clients over network
 */

public class ConnectionModel {
    ArrayList<ClientModel> clientsInfo;

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

    public void setClientsInfo(ArrayList<ClientModel> clientsInfo) {
        this.clientsInfo = clientsInfo;
    }
}
