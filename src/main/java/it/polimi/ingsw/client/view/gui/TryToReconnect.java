package it.polimi.ingsw.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;

public class TryToReconnect implements Initializable {
    @FXML
    private final Label label = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = label;
    }
}
