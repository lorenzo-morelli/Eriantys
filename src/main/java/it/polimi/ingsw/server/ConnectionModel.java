package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientModel;
import java.util.ArrayList;

/**
 * This class is needed to store the data sent from Clients over network
 */

public class ConnectionModel {
    ArrayList<ClientModel> clientsInfo;

    public ConnectionModel(){
        clientsInfo = new ArrayList<>();
    }

    public ArrayList<ClientModel> getClientsInfo() {
        return clientsInfo;
    }

}
