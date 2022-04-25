package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.CliView;
import it.polimi.ingsw.client.view.gui.GuiView;

// lancia il gioco lato client
public class ClientLauncher {
    public static void main(String[] args) throws Exception {
        new ClientController(new CliView());
    }
}
