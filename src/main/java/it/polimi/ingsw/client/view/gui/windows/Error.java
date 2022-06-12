package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.GUI;
import javafx.scene.input.MouseEvent;

public class Error {
    public final GUI gui = new GUI();

    public void ok(MouseEvent mouseEvent) {
        this.gui.closeWindow(mouseEvent);
    }
}
