package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
import static it.polimi.ingsw.utils.gui.Converter.*;
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

    public GridPane tower1Grid;
    public GridPane tower2Grid;
    public GridPane tower3Grid;
    public GridPane tower4Grid;

    public ImageView school1;
    public ImageView school2;
    public ImageView school3;
    public ImageView school4;

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
        List<String> playerNamesString = new ArrayList<>();
        playerNamesString.add("paolo");
        playerNamesString.add("pippo");
        playerNamesString.add("paperino");
        List<String> characterCardsString = new ArrayList<>();
        characterCardsString.add("MONK");
        characterCardsString.add("PRINCESS");
        characterCardsString.add("HERALD");
        List<String> studentIslandString = new ArrayList<>();
        studentIslandString.add("rosso");
        studentIslandString.add("rosso");
        studentIslandString.add("blu");
        studentIslandString.add("verde");
        List<Integer> assistantCardsString = new ArrayList<>();
        assistantCardsString.add(10);
        assistantCardsString.add(3);
        assistantCardsString.add(5);
        assistantCardsString.add(6);

        List<Label> playerNames = Arrays.asList(playerName1, playerName2, playerName3, playerName4);
        List<ImageView> assistantCards = Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4);
        List<ImageView> characterCards = Arrays.asList(characterCard1, characterCard2, characterCard3);
        List<GridPane> professorGrids = Arrays.asList(professor1Grid, professor2Grid, professor3Grid, professor4Grid);
        List<GridPane> schoolGrids = Arrays.asList(school1Grid, school2Grid, school3Grid, school4Grid);
        List<ImageView> schools = Arrays.asList(school1, school2, school3, school4);
        List<ImageView> coins = Arrays.asList(coin1, coin2, coin3, coin4);
        List<Label> coinLabels = Arrays.asList(coin1Label, coin2Label, coin3Label, coin4Label);

        //ImageView professorBlue = new ImageView("graphics/pieces/professors/teacher_blue.png");
        //ImageView professorRed = new ImageView("graphics/pieces/professors/teacher_red.png");

        //ISOLE
        islandGrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < islandNumber; i++) {
            StackPane tile = new StackPane();

            //INIZIALIZZO LE ISOLE
            ImageView island = new ImageView();
            switch (i % 3) {
                case 0:
                    island = new ImageView("/graphics/pieces/islands/island1.png");
                    break;
                case 1:
                    island = new ImageView("/graphics/pieces/islands/island2.png");
                    break;
                case 2:
                    island = new ImageView("/graphics/pieces/islands/island3.png");
                    break;
            }
            island.setFitHeight(180);
            island.setFitWidth(180);
            tile.getChildren().add(island);

            //INIZIALIZZO GLI STUDENTI NELLE ISOLE
            GridPane studentsIsland = new GridPane();
            studentsIsland.setAlignment(Pos.CENTER);
            List<ImageView> studentsIslandImages;
            studentsIslandImages = Converter.toImageCharacters(studentIslandString);

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

        // STUDENT IN ENTRANCE
        int numOfStudents = 5;
        for (int i = 1; i <= numOfStudents; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_blue.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            students1Grid.add(student, i % 2, i / 2);
        }

        // STUDENT IN SCHOOL
        int numOfRedStudents = 2;
        int numOfBlueStudents = 3;
        int numOfGreenStudents = 0;
        int numOfYellowStudents = 1;
        int numOfPinkStudents = 2;

        school1Grid.setAlignment(Pos.CENTER);
        for (int i = 0; i < numOfBlueStudents; i++) {
            ImageView studentBlue = new ImageView("/graphics/pieces/students/student_blue.png");
            school1Grid.add(studentBlue, i, getColorPlace("blue"));
        }
        for (int i = 0; i < numOfRedStudents; i++) {
            ImageView studentRed = new ImageView("/graphics/pieces/students/student_red.png");
            school1Grid.add(studentRed, i, getColorPlace("red"));
        }
        for (int i = 0; i < numOfGreenStudents; i++) {
            ImageView studentGreen = new ImageView("/graphics/pieces/students/student_green.png");
            school1Grid.add(studentGreen, i, getColorPlace("green"));
        }
        for (int i = 0; i < numOfPinkStudents; i++) {
            ImageView studentPink = new ImageView("/graphics/pieces/students/student_pink.png");
            school1Grid.add(studentPink, i, getColorPlace("pink"));
        }
        for (int i = 0; i < numOfYellowStudents; i++) {
            ImageView studentYellow = new ImageView("/graphics/pieces/students/student_yellow.png");
            school1Grid.add(studentYellow, i, getColorPlace("yellow"));
        }

        //PROFESSORI
        ImageView professorBlue = new ImageView("graphics/pieces/professors/teacher_blue.png");
        professorBlue.setFitWidth(30);
        professorBlue.setFitHeight(30);
        professorGrids.get(0).add(professorBlue, 0, getColorPlace("blue"));


        // TORRI
        for (int i = 0; i < 7; i++) {
            ImageView tower = new ImageView("graphics/pieces/towers/black_tower.png");
            tower.setFitHeight(50);
            tower.setFitWidth(50);
            tower1Grid.setHgap(-15);
            tower1Grid.add(tower, i % 2, i / 2);
        }


        // GAMEMODE
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
            toImageCharacters(characterCardsString, characterCards);
        }

        // NOMI E NUMERO GIOCATORI
        for (int i = 0; i < playerNamesString.size(); i++) {
            playerNames.get(i).setText(playerNamesString.get(i));
        }

        if (playerNamesString.size() < 4) {
            playerNames.get(3).setVisible(false);
            assistantCards.get(3).setVisible(false);
            schools.get(3).setVisible(false);
            coins.get(3).setVisible(false);
            coinLabels.get(3).setVisible(false);
        }
        if (playerNamesString.size() < 3) {
            playerNames.get(2).setVisible(false);
            assistantCards.get(2).setVisible(false);
            schools.get(2).setVisible(false);
            coins.get(2).setVisible(false);
            coinLabels.get(2).setVisible(false);
        }

        // ASSISTANT CARDS
        toImageAssistants(assistantCardsString, assistantCards);


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
