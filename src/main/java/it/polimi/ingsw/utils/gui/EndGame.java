package it.polimi.ingsw.utils.gui;

import it.polimi.ingsw.client.GUI;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static it.polimi.ingsw.client.GUI.currNode;

public class EndGame {
    public Label winLabel;
    private final GUI gui = new GUI();

    public void toMainMenu(MouseEvent mouseEvent) throws IOException {
        currNode = winLabel;
        this.gui.changeScene("MainMenu");
    }
}
