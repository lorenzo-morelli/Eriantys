package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.CLIcontroller.ClientController;
import it.polimi.ingsw.client.view.cli.CliView;

/**
 * Launcher for CLI View
 */
public class CLI {
    public static void main(String[] args) throws Exception {
        new ClientController(new CliView());
    }
}
