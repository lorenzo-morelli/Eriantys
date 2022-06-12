package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.GUI;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.messageToOthers;

public class Wait implements Initializable {
    public Label label = new Label();
    private final GUI gui = new GUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gui.currNode = label;
    }
}
