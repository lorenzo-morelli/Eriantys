package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;

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
        currNode = assistantCard1;
        List<ImageView> cards = Arrays.asList(assistantCard1,
                assistantCard2,
                assistantCard3,
                assistantCard4,
                assistantCard5,
                assistantCard6,
                assistantCard7,
                assistantCard8,
                assistantCard9,
                assistantCard10
        );

        for (int i = 0; i < 10; i++) {
            cards.get(i).setVisible(false);
        }

        for (AssistantCard a : gui.getClientModel().getDeck()){
            System.out.println("valore: " + (int)a.getValues() + "  mosse: " + a.getMoves());
            switch ((int) a.getValues()) {
                case 1:
                    assistantCard1.setVisible(true);
                case 2:
                    assistantCard2.setVisible(true);
                case 3:
                    assistantCard3.setVisible(true);
                case 4:
                    assistantCard4.setVisible(true);
                case 5:
                    assistantCard5.setVisible(true);
                case 6:
                    assistantCard6.setVisible(true);
                case 7:
                    assistantCard7.setVisible(true);
                case 8:
                    assistantCard8.setVisible(true);
                case 9:
                    assistantCard9.setVisible(true);
                case 10:
                    assistantCard10.setVisible(true);
            }
        }
    }


    public void setAssistantCard1(MouseEvent mouseEvent) throws InterruptedException {
        this.gui.getClientModel().setCardChoosedValue(1);
        this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
        this.gui.getClientModel().setPingMessage(false);
        Gson json = new Gson();
        Network.send(json.toJson(this.gui.getClientModel()));
    }
}
