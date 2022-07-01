package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.view.gui.GuiView;
import javafx.scene.input.MouseEvent;

public class Quit {
    private final GuiView guiView = new GuiView();

    /**
     * This method is used to exit the program.
     */
    public void yes() {
        System.exit(0);
    }

    /**
     * This method just closes the "Quit" window.
     * @param mouseEvent the event to close the window.
     */
    public void no(MouseEvent mouseEvent) {
        this.guiView.closeWindow(mouseEvent);
    }
}
