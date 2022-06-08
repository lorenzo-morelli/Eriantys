package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;
import static it.polimi.ingsw.client.GUI.gameState;
import static it.polimi.ingsw.utils.gui.Position.*;

public class Game implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    public Label phaseLabel;
    public Label turnLabel;
    public GridPane islandGrid;
    public ImageView motherNature;
    private ClientModel clientModel;
    public ImageView assistantCard1;
    public ImageView assistantCard2;
    public ImageView assistantCard3;
    public ImageView assistantCard4;
    public ImageView school1;
    public ImageView school2;
    public ImageView school3;
    public ImageView school4;
    public Label playerName1;
    public Label playerName2;
    public Label playerName3;
    public Label playerName4;

    private ParametersFromNetwork response;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = phaseLabel;
        phaseLabel.setText(gameState);
//        this.gui.getClientModel().getServermodel().getPlayers().forEach((player) -> System.out.println(player.getNickname()));
//        System.out.println("giocatori connessi: " + this.gui.getClientModel().getServermodel().getPlayers().size());
//        System.out.println(response.getParameter(0));

        List<Label> playerNames = Arrays.asList(playerName1, playerName2, playerName3, playerName4);
        List<ImageView> assistantCards = Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4);

        //INIZIALIZZO LE ISOLE
        islandGrid.setGridLinesVisible(true);
        islandGrid.setAlignment(Pos.CENTER);
        int motherNaturePos = 4; //todo testing
        int towerNumbers = 5; //todo testing
        for (int i = 0; i < 12; i++) {
            StackPane tile = new StackPane();
            ImageView island = new ImageView("/graphics/pieces/islands/island1.png");
            island.setFitHeight(180);
            island.setFitWidth(180);
            tile.getChildren().add(island);

            if (motherNaturePos == i) {
                tile.getChildren().add(motherNature);
                StackPane.setAlignment(motherNature, Pos.TOP_CENTER);
            }
//            tile.getChildren().add(new Label("" + i));
            GridPane towers = new GridPane();
//            towers.setGridLinesVisible(true);
            towers.setAlignment(Pos.BOTTOM_CENTER);
            towers.setHgap(-15);
            //AGGIUNGERE LE TORRI ALLE ISOLE
            for (int j = 0; j < towerNumbers; j++) {
                ImageView tower = new ImageView("graphics/pieces/towers/black_tower.png");
                tower.setFitWidth(40);
                tower.setFitHeight(40);
                towers.addRow(1, tower);
            }
            tile.getChildren().add(towers);
            islandGrid.add(tile, islandX(i), islandY(i));
        }

        //INIZIALIZZO LE NUVOLE
        int cloudNumber = 2;
        int numStudCloud = 3;
        for (int i = 0; i < cloudNumber; i++) {
            StackPane tile = new StackPane();
            ImageView cloud = new ImageView("/graphics/pieces/clouds/cloud_card.png");
            cloud.setFitHeight(130);
            cloud.setFitWidth(130);
            tile.getChildren().add(cloud);
            GridPane studentCloud = new GridPane();
            studentCloud.setAlignment(Pos.CENTER);
            studentCloud.setGridLinesVisible(true);
            //AGGIUNGERE STUDENTI ALLE NUVOLE
            for (int j = 0; j < numStudCloud; j++) {
                studentCloud.addRow(1, new ImageView("/graphics/pieces/students/student_blue.png"));
            }
            tile.getChildren().add(studentCloud);
            islandGrid.add(tile, cloudX(i), cloudY(i));
        }

        //settare i constraints per partite <4 e <3
//        if (clientModel.getNumofplayer() < 4) {
//            school4.setVisible(false);
//            playerName4.setVisible(false);
//
//        }
//        if (clientModel.getNumofplayer() < 3) {
//            school3.setVisible(false);
//            playerName3.setVisible(false);
//        }

        //setto i nomi
//        for (int i = 0; i < this.gui.getClientModel().getServermodel().getPlayers().size(); i++) {
//            System.out.println("giocatore " + i + ": " + this.gui.getClientModel().getServermodel().getPlayers().get(i).getNickname());
//            playerNames.get(i).setText(this.gui.getClientModel().getServermodel().getPlayers().get(i).getNickname());
//        }

        //if planning -> phaseLabel.set planning, else -> set action
        //turn player...

        //setto le torri

        //setto gli studenti

        //setto le carte personaggio


    }

    public void quit() throws IOException {
        this.gui.openNewWindow("Quit");
    }


    public void setOnIsland(MouseEvent mouseEvent) {

    }

    public void setOnSchool() throws IOException {
        this.gui.openNewWindow("MoveToSchool");
    }
}
