package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Game implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //assistantCard1
    }

    public void quit() throws IOException {
        System.exit(0); //todo: this.gui.openNewWindow("Quit");
    }

    public void setOnSchool() throws IOException {
        this.gui.openNewWindow("MoveToSchool");
    }

    public void choosed1(MouseEvent mouseEvent) {
        assistantCard1.setVisible(false);
        gui.getClientModel().setCardChoosedValue(1);
        System.out.println(gui.getClientModel().getCardChoosedValue());
    }
}
