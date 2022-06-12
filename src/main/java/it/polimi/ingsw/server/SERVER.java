package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;

public class SERVER { public static void main(String[] args) {
    try {
        new ServerController();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
