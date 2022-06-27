package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.cliController.ClientController;
import it.polimi.ingsw.client.view.cli.CliView;

/**
 * This class is used to easily start a command line interface of the client,
 * its task is to instantiate a new ClientController and give it and as a view use the CliView
 */
public class CLI {
    public static void main(String[] args) throws Exception {
        new ClientController(new CliView());
    }
}
