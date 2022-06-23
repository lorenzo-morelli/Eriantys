package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.canOpenWindow;

public class ChooseAssistantCard implements Initializable {
    private final GUI gui = new GUI();
    @FXML
    private ImageView assistantCard1;
    @FXML
    private ImageView assistantCard2;
    @FXML
    private ImageView assistantCard3;
    @FXML
    private ImageView assistantCard4;
    @FXML
    private ImageView assistantCard5;
    @FXML
    private ImageView assistantCard6;
    @FXML
    private ImageView assistantCard7;
    @FXML
    private ImageView assistantCard8;
    @FXML
    private ImageView assistantCard9;
    @FXML
    private ImageView assistantCard10;

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
        cards.forEach((card) -> card.setOnMouseClicked((event) -> setCard(cards.indexOf(card) + 1, event))
        );
    }


    public void setCard(int value, MouseEvent mouseEvent) {
        this.gui.getClientModel().setCardChoosedValue(value);
        this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
        this.gui.getClientModel().setPingMessage(false);
        Gson json = new Gson();
        Network.send(json.toJson(this.gui.getClientModel()));
        this.gui.closeWindow(mouseEvent);
    }
}
