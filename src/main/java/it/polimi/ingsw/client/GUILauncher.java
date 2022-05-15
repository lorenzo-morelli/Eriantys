package it.polimi.ingsw.client;

//import it.polimi.ingsw.client.view.gui.GuiView;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.utils.gui.GUI;

// lancia il gioco lato client
public class GUILauncher {
    public static void main(String[] args) throws Exception {
        new ClientController(new GUI());
    }
}