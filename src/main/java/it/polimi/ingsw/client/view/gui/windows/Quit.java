package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.GUI;
import javafx.scene.input.MouseEvent;

public class Quit {
    private final GUI gui = new GUI();

    public void yes() {
        System.exit(0);
    }

    public void no(MouseEvent mouseEvent) {
        this.gui.closeWindow(mouseEvent);
    }
}
