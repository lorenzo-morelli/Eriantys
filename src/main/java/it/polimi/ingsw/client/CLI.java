package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.CLIcontroller.ClientController;
import it.polimi.ingsw.client.view.cli.CliView;

// lancia il gioco lato client
public class CLI {
    public static void main(String[] args) throws Exception {
        new ClientController(new CliView());
    }
}
