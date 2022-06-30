package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;

/**
 * This class is used to easily start a command line interface of the server,
 * its task is to instantiate a new ServerController
 */
public class SERVER {

    public static void main(String[] args) {
            try {
                new ServerController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
