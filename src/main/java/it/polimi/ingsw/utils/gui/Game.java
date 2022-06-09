package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.TowerColor;
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
    public Label phaseLabel;
    public Label turnLabel;
    public GridPane islandGrid;

    public GridPane entrance1Grid;
    public GridPane entrance2Grid;
    public GridPane entrance3Grid;
    public GridPane entrance4Grid;

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

        // OK! valori partita
        GameMode gameMode = this.gui.getClientModel().getServermodel().getGameMode();
        int motherNaturePos = this.gui.getClientModel().getServermodel().getTable().getMotherNaturePosition();
        int islandNumber = 1;con

        // OK! valore players
        List<Player> players = this.gui.getClientModel().getServermodel().getPlayers();
        List<String> playerNamesString = new ArrayList<>();
        players.forEach(player -> playerNamesString.add(player.getNickname()));

        List<StudentSet> dinnerTables = new ArrayList<>();
        players.forEach(player -> dinnerTables.add(player.getSchoolBoard().getDinnerTable()));
        List<StudentSet> entranceSpace = new ArrayList<>();
        players.forEach(player -> entranceSpace.add(player.getSchoolBoard().getEntranceSpace()));

        // valore assistant card scelte
        List<Integer> assistantCardsValues = new ArrayList<>();
        players.forEach(player -> assistantCardsValues.add((int) player.getChoosedCard().getValues()));

        // valore professori
        List<Professor> professors = this.gui.getClientModel().getServermodel().getTable().getProfessors();


        List<String> studentIslandString = new ArrayList<>();
        studentIslandString.add("rosso");
        studentIslandString.add("rosso");
        studentIslandString.add("blu");
        studentIslandString.add("verde");

        ArrayList<Label> playerNames = new ArrayList<>(Arrays.asList(playerName1, playerName2, playerName3, playerName4));
        ArrayList<ImageView> assistantCards = new ArrayList<>(Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4));
        ArrayList<ImageView> characterCards = new ArrayList<>(Arrays.asList(characterCard1, characterCard2, characterCard3));
        ArrayList<GridPane> professorGrids = new ArrayList<>(Arrays.asList(professor1Grid, professor2Grid, professor3Grid, professor4Grid));
        ArrayList<GridPane> entranceGrids = new ArrayList<>(Arrays.asList(entrance1Grid, entrance2Grid, entrance3Grid, entrance4Grid));
        ArrayList<GridPane> schoolGrids = new ArrayList<>(Arrays.asList(school1Grid, school2Grid, school3Grid, school4Grid));
        ArrayList<GridPane> towerGrids = new ArrayList<>(Arrays.asList(tower1Grid, tower2Grid, tower3Grid, tower4Grid));
        ArrayList<ImageView> schools = new ArrayList<>(Arrays.asList(school1, school2, school3, school4));
        ArrayList<ImageView> coins = new ArrayList<>(Arrays.asList(coin1, coin2, coin3, coin4));
        ArrayList<Label> coinLabels = new ArrayList<>(Arrays.asList(coin1Label, coin2Label, coin3Label, coin4Label));

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
            int towernnnn = 3;
            GridPane towers = new GridPane();
            towers.setAlignment(Pos.BOTTOM_CENTER);
            towers.setHgap(-15);
            for (int j = 0; j < towernnnn; j++) {
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

        // OK! STUDENT IN ENTRANCE
        entranceGrids.forEach(entrance -> {
            players.forEach(player -> {
                if (entranceGrids.indexOf(entrance) == players.indexOf(player)) {
                    int position = 1;
                    for (int i = 0; i < player.getSchoolBoard().getEntranceSpace().getNumOfGreenStudents(); i++) {
                        ImageView student = new ImageView("/graphics/pieces/students/student_green.png");
                        student.setFitHeight(30);
                        student.setFitWidth(30);
                        entrance.add(student, position % 2, position / 2);
                        position++;
                    }
                    for (int i = 0; i < player.getSchoolBoard().getEntranceSpace().getNumOfRedStudents(); i++) {
                        ImageView student = new ImageView("/graphics/pieces/students/student_red.png");
                        student.setFitHeight(30);
                        student.setFitWidth(30);
                        entrance.add(student, position % 2, position / 2);
                        position++;
                    }
                    for (int i = 0; i < player.getSchoolBoard().getEntranceSpace().getNumOfYellowStudents(); i++) {
                        ImageView student = new ImageView("/graphics/pieces/students/student_yellow.png");
                        student.setFitHeight(30);
                        student.setFitWidth(30);
                        entrance.add(student, position % 2, position / 2);
                        position++;
                    }
                    for (int i = 0; i < player.getSchoolBoard().getEntranceSpace().getNumOfPinkStudents(); i++) {
                        ImageView student = new ImageView("/graphics/pieces/students/student_pink.png");
                        student.setFitHeight(30);
                        student.setFitWidth(30);
                        entrance.add(student, position % 2, position / 2);
                        position++;
                    }
                    for (int i = 0; i < player.getSchoolBoard().getEntranceSpace().getNumOfBlueStudents(); i++) {
                        ImageView student = new ImageView("/graphics/pieces/students/student_blue.png");
                        student.setFitHeight(30);
                        student.setFitWidth(30);
                        entrance.add(student, position % 2, position / 2);
                        position++;
                    }
                }
            });
        });

        // OK! STUDENT IN SCHOOL
        schoolGrids.forEach(school -> {
            school.setAlignment(Pos.CENTER);
            players.forEach(player -> {
                if (schoolGrids.indexOf(school) == players.indexOf(player)) {
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfBlueStudents(); i++) {
                        ImageView studentBlue = new ImageView("/graphics/pieces/students/student_blue.png");
                        school1Grid.add(studentBlue, i, getColorPlace("blue"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfRedStudents(); i++) {
                        ImageView studentRed = new ImageView("/graphics/pieces/students/student_red.png");
                        school1Grid.add(studentRed, i, getColorPlace("red"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfGreenStudents(); i++) {
                        ImageView studentGreen = new ImageView("/graphics/pieces/students/student_green.png");
                        school1Grid.add(studentGreen, i, getColorPlace("green"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfPinkStudents(); i++) {
                        ImageView studentPink = new ImageView("/graphics/pieces/students/student_pink.png");
                        school1Grid.add(studentPink, i, getColorPlace("pink"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfYellowStudents(); i++) {
                        ImageView studentYellow = new ImageView("/graphics/pieces/students/student_yellow.png");
                        school1Grid.add(studentYellow, i, getColorPlace("yellow"));
                    }
                }
            });
        });

        //PROFESSORI
        professors.forEach(professor -> {
            players.forEach(player -> {
                if (professor.getHeldBy() == player) {
                    ImageView profImage = null;
                    switch (professor.getColor()) {
                        case BLUE: profImage = new ImageView("graphics/pieces/professors/teacher_blue.png"); break;
                        case RED: profImage = new ImageView("graphics/pieces/professors/teacher_red.png"); break;
                        case PINK: profImage = new ImageView("graphics/pieces/professors/teacher_pink.png"); break;
                        case GREEN: profImage = new ImageView("graphics/pieces/professors/teacher_green.png"); break;
                        case YELLOW: profImage = new ImageView("graphics/pieces/professors/teacher_yellow.png"); break;
                    }
                    profImage.setFitWidth(30);
                    profImage.setFitHeight(30);
                    ImageView finalProfImage = profImage;
                    professorGrids.forEach(professorGrid -> {
                        professorGrid.setAlignment(Pos.CENTER);
                        if (professorGrids.indexOf(professorGrid) == players.indexOf(player)) {
                            professorGrid.add(finalProfImage, 0, getColorPlace("blue"));
                        }
                    });
                }
            });
        });


        // OK! TORRI
        players.forEach(player -> {
            towerGrids.forEach(tower -> {
                tower.setAlignment(Pos.CENTER);
                if (players.indexOf(player) == towerGrids.indexOf(tower)) {
                    System.out.println("torri del player " + player.getNickname());
                    for (int i = 0; i < player.getSchoolBoard().getNumOfTowers(); i++) {
                        ImageView towerImage = null;
                        switch (player.getSchoolBoard().getTowerColor()) {
                            case BLACK: towerImage = new ImageView("graphics/pieces/towers/black_tower.png"); break;
                            case WHITE: towerImage = new ImageView("graphics/pieces/towers/white_tower.png"); break;
                            case GREY: towerImage = new ImageView("graphics/pieces/towers/grey_tower.png"); break;
                        }
                        towerImage.setFitHeight(50);
                        towerImage.setFitWidth(50);
                        tower.setHgap(-15);
                        tower.add(towerImage, i % 2, i / 2);
                    }
                }
            });
        });

        // OK! GAMEMODE
        if (gameMode.equals(GameMode.PRINCIPIANT)) {
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
            List<CharacterCard> characters = this.gui.getClientModel().getServermodel().getTable().getCharachter();
            List<String> characterCardsString = new ArrayList<>();
            characters.forEach(character -> characterCardsString.add(character.getName()));
            toImageCharacters(characterCardsString, characterCards);
        }

        // OK! NOMI E NUMERO GIOCATORI
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

        // OK! ASSISTANT CARDS
        toImageAssistants(assistantCardsValues, assistantCards);


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
