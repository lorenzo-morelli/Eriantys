package it.polimi.ingsw.utils.gui.windows;

import it.polimi.ingsw.client.GUI;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Quit {
    private final GUI gui = new GUI();

    public void yes() {
        System.exit(0);
    }

    public void no(MouseEvent mouseEvent) {
        this.gui.closeWindow(mouseEvent);
    }
}
