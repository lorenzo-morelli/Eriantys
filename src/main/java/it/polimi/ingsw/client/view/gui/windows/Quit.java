package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.view.gui.GuiView;
import javafx.scene.input.MouseEvent;

public class Quit {
    private final GuiView guiView = new GuiView();

    public void yes() {
        System.exit(0);
    }

    public void no(MouseEvent mouseEvent) {
        this.guiView.closeWindow(mouseEvent);
    }
}
