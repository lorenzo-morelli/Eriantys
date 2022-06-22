package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.utils.network.Network;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class Quit {
    private final GUI gui = new GUI();

    public void yes() throws InterruptedException {
        Network.disconnect();
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }

    public void no(MouseEvent mouseEvent) {
        this.gui.closeWindow(mouseEvent);
    }
}
