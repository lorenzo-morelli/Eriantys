package it.polimi.ingsw.utils.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
        List<ImageView> cards = Arrays.asList(
                assistantCard1,
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
        List<AssistantCard> deck = this.gui.getClientModel().getDeck();
        cards.forEach((card) -> card.setVisible(false));
        cards.forEach((card) -> {
            boolean show = false;
            for (AssistantCard assistantCard : deck) {
                if (assistantCard.getValues() == cards.indexOf(card) + 1) {
                    show = true;
                }
            }
            if (show) {
                card.setVisible(true);
            }
        });
        cards.forEach((card) -> card.setOnMouseClicked((event) -> {
                    try {
                        setCard(cards.indexOf(card) + 1, event);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
        );
    }


    public void setCard(int value, MouseEvent mouseEvent) throws InterruptedException {
        this.gui.getClientModel().setCardChoosedValue(value);
        this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
        this.gui.getClientModel().setPingMessage(false);
        Gson json = new Gson();
        Network.send(json.toJson(this.gui.getClientModel()));
        this.gui.closeWindow(mouseEvent);
    }
}
