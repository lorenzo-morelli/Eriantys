package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.model.StudentSet;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;
import static it.polimi.ingsw.client.GUI.gameState;
import static it.polimi.ingsw.utils.gui.Converter.toImage;
import static it.polimi.ingsw.utils.gui.Position.*;

public class Game implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    public Label phaseLabel;
    public Label turnLabel;
    public GridPane islandGrid;

    public GridPane students1Grid;
    public GridPane students2Grid;
    public GridPane students3Grid;
    public GridPane students4Grid;

    public GridPane school1Grid;
    public GridPane school2Grid;
    public GridPane school3Grid;
    public GridPane school4Grid;

    public GridPane professor1Grid;
    public GridPane professor2Grid;
    public GridPane professor3Grid;
    public GridPane professor4Grid;

    public ImageView motherNature;

    public ImageView assistantCard1;
    public ImageView assistantCard2;
    public ImageView assistantCard3;
    public ImageView assistantCard4;

    public ImageView characterCard1;
    public ImageView characterCard2;
    public ImageView characterCard3;

    public ImageView coin1;
    public ImageView coin2;
    public ImageView coin3;
    public ImageView coin4;

    public Label coin1Label;
    public Label coin2Label;
    public Label coin3Label;
    public Label coin4Label;

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


        String gameMode = "PRINCIPIANT";
        int motherNaturePos = 4; //todo testing
        int towerNumbers = 2; //todo testing
        int islandNumber = 11;
        List<String> characterCardsString = new ArrayList<>();
        characterCardsString.add("MONK");
        characterCardsString.add("PRINCESS");
        characterCardsString.add("HERALD");

        List<String> studentIslandString = new ArrayList<>();
        studentIslandString.add("rosso");
        studentIslandString.add("rosso");
        studentIslandString.add("blu");
        studentIslandString.add("verde");

        List<Label> playerNames = Arrays.asList(playerName1, playerName2, playerName3, playerName4);
        List<ImageView> assistantCards = Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4);
        List<ImageView> characterCards = Arrays.asList(characterCard1, characterCard2, characterCard3);
        List<GridPane> professors = Arrays.asList(professor1Grid, professor2Grid, professor3Grid, professor4Grid);
        List<GridPane> schools = Arrays.asList(school1Grid, school2Grid, school3Grid, school4Grid);
        List<ImageView> coins = Arrays.asList(coin1, coin2, coin3, coin4);
        List<Label> coinLabels = Arrays.asList(coin1Label, coin2Label, coin3Label, coin4Label);

        //ISOLE
        islandGrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < islandNumber; i++) {
            StackPane tile = new StackPane();

            //INIZIALIZZO LE ISOLE
            ImageView island = new ImageView();
            switch (i % 3) {
                case 0: island = new ImageView("/graphics/pieces/islands/island1.png"); break;
                case 1: island = new ImageView("/graphics/pieces/islands/island2.png"); break;
                case 2: island = new ImageView("/graphics/pieces/islands/island3.png"); break;
            }
            island.setFitHeight(180);
            island.setFitWidth(180);
            tile.getChildren().add(island);

            //INIZIALIZZO GLI STUDENTI NELLE ISOLE
            GridPane studentsIsland = new GridPane();
            studentsIsland.setAlignment(Pos.CENTER);
            List<ImageView> studentsIslandImages;
            studentsIslandImages = toImage(studentIslandString);

            for (int j = 0; j < studentsIslandImages.size(); j++) {
                studentsIslandImages.get(j).setFitHeight(20);
                studentsIslandImages.get(j).setFitWidth(20);
                studentsIsland.add(studentsIslandImages.get(j), j % 3, j / 3);
            }
            tile.getChildren().add(studentsIsland);

            //INIZIALIZZO LE TORRI NELLE ISOLE
            GridPane towers = new GridPane();
            towers.setAlignment(Pos.BOTTOM_CENTER);
            towers.setHgap(-15);
            for (int j = 0; j < towerNumbers; j++) {
                ImageView tower = new ImageView("graphics/pieces/towers/black_tower.png");
                tower.setFitWidth(40);
                tower.setFitHeight(40);
                towers.addRow(1, tower);
            }
            tile.getChildren().add(towers);

            //INIZIALIZZO MADRE NATURA
            if (i == motherNaturePos) {
                tile.getChildren().add(motherNature);
                StackPane.setAlignment(motherNature, Pos.TOP_CENTER);
            }
            islandGrid.add(tile, islandX(i), islandY(i));
        }

        //NUVOLE
        int cloudNumber = 2;
        int numStudCloud = 3;
        for (int i = 0; i < cloudNumber; i++) {
            StackPane tile = new StackPane();
            ImageView cloud = new ImageView("/graphics/pieces/clouds/cloud_card.png");
            cloud.setFitHeight(130);
            cloud.setFitWidth(130);
            tile.getChildren().add(cloud);
            GridPane studentsCloudGrid = new GridPane();
            studentsCloudGrid.setAlignment(Pos.CENTER);
            studentsCloudGrid.setHgap(10);

            //INIZIALIZZO STUDENTI ALLE NUVOLE
            for (int j = 0; j < numStudCloud; j++) {
                ImageView student = new ImageView("/graphics/pieces/students/student_blue.png");
                student.setFitHeight(30);
                student.setFitWidth(30);
                studentsCloudGrid.add(student, j % 2, j / 2);
            }
            tile.getChildren().add(studentsCloudGrid);
            islandGrid.add(tile, cloudX(i), cloudY(i));
        }



        // INIZIALIZZO LA GAMEMODE
        if (gameMode.equals("PRINCIPIANT")) {
            for (ImageView characterCard : characterCards) {
                characterCard.setVisible(false);
            }
            for (ImageView coin : coins) {
                coin.setVisible(false);
            }
            for (Label coinLabel : coinLabels) {
                coinLabel.setVisible(false);
            }

        } else {
            toImage(characterCardsString, characterCards);
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

    public void setOnSchool() throws IOException {
        this.gui.openNewWindow("MoveToSchool");
    }

    public void setOnIsland(MouseEvent mouseEvent) {
        this.gui.openNewWindow("MoveToIsland");
    }

    public void characterCard1(MouseEvent mouseEvent) {

    }

    public void characterCard2(MouseEvent mouseEvent) {

    }

    public void characterCard3(MouseEvent mouseEvent) {

    }
}
