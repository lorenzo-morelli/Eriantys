package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseAssistantCard implements Initializable {
    public ImageView assistantCard1 = new ImageView();
    public ImageView assistantCard2 = new ImageView();
    public ImageView assistantCard3;
    public ImageView assistantCard4;
    public ImageView assistantCard5;
    public ImageView assistantCard6;
    public ImageView assistantCard7;
    public ImageView assistantCard8;
    public ImageView assistantCard9;
    public ImageView assistantCard10;
    private final GUI gui = new GUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void setAssistantCard1(MouseEvent mouseEvent) throws InterruptedException {
        this.gui.getClientModel().setCardChoosedValue(1);
        this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
        this.gui.getClientModel().setPingMessage(false);
        Gson json = new Gson();
        Network.send(json.toJson(this.gui.getClientModel()));
    }
}
