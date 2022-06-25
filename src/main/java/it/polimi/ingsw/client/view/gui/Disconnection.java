package it.polimi.ingsw.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.GuiView.currNode;

public class Disconnection implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Label label = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = label;
    }

    /**
     * This event is used to navigate to the Main Menu.
     */
    @FXML
    private void go() throws IOException {
        this.guiView.changeScene("MainMenu");
    }
}
