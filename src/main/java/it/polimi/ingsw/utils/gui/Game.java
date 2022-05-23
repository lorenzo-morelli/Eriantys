package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Game implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    public Label phaseLabel;
    public Label turnLabel;
    private ClientModel clientModel;
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
    public ImageView school1;
    public ImageView school2;
    public ImageView school3;
    public ImageView school4;
    public Label playerName1;
    public Label playerName2;
    public Label playerName3;
    public Label playerName4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientModel = this.gui.getClientModel();
        clientModel.setNumofplayer(3);
        List<Label> playerNames = Arrays.asList(playerName1, playerName2, playerName3, playerName4);

        //testing...
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("ue");
        nomi.add("ao");
        nomi.add("eskere");
        clientModel.setNicknames(nomi);



        if (clientModel.getNumofplayer() < 4) {
            school4.setVisible(false);
            playerName4.setVisible(false);

        }
        if (clientModel.getNumofplayer() < 3) {
            school3.setVisible(false);
            playerName3.setVisible(false);
        }

        

        //players name set
        for (int i = 0; i < clientModel.getNumofplayer(); i++) {
            playerNames.get(i).setText(clientModel.getNicknames().get(i));
        }

        //if planning -> phaseLabel.set planning, else -> set action
        //turn player...


    }

    public void quit() throws IOException {
        System.exit(0); //todo: this.gui.openNewWindow("Quit");
    }

    public void setOnSchool() throws IOException {
        this.gui.openNewWindow("MoveToSchool");
    }

    public void choosed1(MouseEvent mouseEvent) {
        assistantCard1.setVisible(false);
        clientModel.setCardChoosedValue(1); //todo: invio?

    }
}