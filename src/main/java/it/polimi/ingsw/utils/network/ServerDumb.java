package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.network.Network;

public class ServerDumb {
    public static void main(String args[]){
        Network network = new Network();
        network.setupServer("1234");
    }
}
