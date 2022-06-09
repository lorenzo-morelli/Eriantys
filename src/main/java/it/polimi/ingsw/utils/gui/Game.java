package it.polimi.ingsw.utils.gui;

import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

    public Button setOnSchoolBtn;
    public Button setOnIslandBtn;
    public Button moveBtn;


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
        turnLabel.setText("è il turno di: " + this.gui.getClientModel().getServermodel().getcurrentPlayer().getNickname());
        if (this.gui.getClientModel().getTypeOfRequest().equals("CHOOSEWHERETOMOVESTUDENTS")) {
            setOnSchoolBtn.setVisible(true);
            setOnIslandBtn.setVisible(true);
            moveBtn.setVisible(false);
        } else if (this.gui.getClientModel().getTypeOfRequest().equals("CHOOSEWHERETOMOVEMOTHER")) {
            setOnSchoolBtn.setVisible(false);
            setOnIslandBtn.setVisible(false);
            moveBtn.setVisible(true);
        }

        // OK! valori partita
        GameMode gameMode = this.gui.getClientModel().getServermodel().getGameMode();
        int motherNaturePos = this.gui.getClientModel().getServermodel().getTable().getMotherNaturePosition();
        ArrayList<Island> islands = this.gui.getClientModel().getServermodel().getTable().getIslands();
        ArrayList<Player> players = this.gui.getClientModel().getServermodel().getPlayers();
        ArrayList<StudentSet> dinnerTables = new ArrayList<>();
        players.forEach(player -> dinnerTables.add(player.getSchoolBoard().getDinnerTable()));
        ArrayList<StudentSet> entranceSpace = new ArrayList<>();
        players.forEach(player -> entranceSpace.add(player.getSchoolBoard().getEntranceSpace()));
        ArrayList<Integer> assistantCardsValues = new ArrayList<>();
        players.forEach(player -> assistantCardsValues.add((int) player.getChoosedCard().getValues()));
        ArrayList<Professor> professors = this.gui.getClientModel().getServermodel().getTable().getProfessors();
        ArrayList<Cloud> clouds = this.gui.getClientModel().getServermodel().getTable().getClouds();

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

        // OK! ISOLE
        islandGrid.setAlignment(Pos.CENTER);

        islands.forEach(island -> {
            StackPane tile = new StackPane();
            ImageView islandImage = new ImageView();
            switch (islands.indexOf(island) % 3) {
                case 0:
                    islandImage = new ImageView("/graphics/pieces/islands/island1.png");
                    break;
                case 1:
                    islandImage = new ImageView("/graphics/pieces/islands/island2.png");
                    break;
                case 2:
                    islandImage = new ImageView("/graphics/pieces/islands/island3.png");
                    break;
            }
            islandImage.setFitHeight(180);
            islandImage.setFitWidth(180);
            tile.getChildren().add(islandImage);

            //INIZIALIZZO GLI INHABITANTS
            GridPane students = new GridPane();
            students.setAlignment(Pos.CENTER);
            int green = island.getInhabitants().getNumOfGreenStudents();
            int red = island.getInhabitants().getNumOfRedStudents();
            int blue = island.getInhabitants().getNumOfBlueStudents();
            int yellow = island.getInhabitants().getNumOfYellowStudents();
            int pink = island.getInhabitants().getNumOfPinkStudents();
            populateGrid(students, 0, 3, green, red, blue, pink, yellow);
            tile.getChildren().add(students);

            //INIZIALIZZO LE TORRI NELLE ISOLE
            GridPane towers = new GridPane();
            towers.setAlignment(Pos.BOTTOM_CENTER);
            towers.setHgap(-15);
            for (int j = 0; j < island.getNumberOfTowers(); j++) {
                ImageView towerImage = null;
                switch (island.getTowerColor()) {
                    case BLACK:
                        towerImage = new ImageView("graphics/pieces/towers/black_tower.png");
                    case GREY:
                        towerImage = new ImageView("graphics/pieces/towers/grey_tower.png");
                    case WHITE:
                        towerImage = new ImageView("graphics/pieces/towers/white_tower.png");
                }
                towerImage.setFitWidth(40);
                towerImage.setFitHeight(40);
                towers.addRow(1, towerImage);
            }
            tile.getChildren().add(towers);

            //INIZIALIZZO MADRE NATURA
            if (islands.indexOf(island) == motherNaturePos) {
                tile.getChildren().add(motherNature);
                StackPane.setAlignment(motherNature, Pos.TOP_CENTER);
            }
            islandGrid.add(tile, islandX(islands.indexOf(island)), islandY(islands.indexOf(island)));

        });

        //NUVOLE
        clouds.forEach(cloud -> {
            StackPane tile = new StackPane();
            ImageView cloudImage = new ImageView("/graphics/pieces/clouds/cloud_card.png");
            cloudImage.setFitHeight(130);
            cloudImage.setFitWidth(130);
            tile.getChildren().add(cloudImage);

            GridPane studentsCloudGrid = new GridPane();
            studentsCloudGrid.setAlignment(Pos.CENTER);
            studentsCloudGrid.setHgap(10);

            int green = cloud.getStudentsAccumulator().getNumOfGreenStudents();
            int blue = cloud.getStudentsAccumulator().getNumOfBlueStudents();
            int red = cloud.getStudentsAccumulator().getNumOfRedStudents();
            int yellow = cloud.getStudentsAccumulator().getNumOfYellowStudents();
            int pink = cloud.getStudentsAccumulator().getNumOfPinkStudents();
            populateGrid(studentsCloudGrid, 0, 2, green, red, blue, pink, yellow);
        });

        // OK! STUDENT IN ENTRANCE
        entranceGrids.forEach(entrance -> {
            players.forEach(player -> {
                if (entranceGrids.indexOf(entrance) == players.indexOf(player)) {
                    int green = player.getSchoolBoard().getEntranceSpace().getNumOfGreenStudents();
                    int blue = player.getSchoolBoard().getEntranceSpace().getNumOfBlueStudents();
                    int red = player.getSchoolBoard().getEntranceSpace().getNumOfRedStudents();
                    int yellow = player.getSchoolBoard().getEntranceSpace().getNumOfYellowStudents();
                    int pink = player.getSchoolBoard().getEntranceSpace().getNumOfPinkStudents();
                    populateGrid(entrance, 1, 2, green, red, blue, pink, yellow);
                }
            });
        });

        // STUDENT IN SCHOOL
        schoolGrids.forEach(school -> {
            if (school != null) school.setAlignment(Pos.CENTER);
            players.forEach(player -> {
                if (schoolGrids.indexOf(school) == players.indexOf(player)) {
                    assert school != null;
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfBlueStudents(); i++) {
                        ImageView studentBlue = new ImageView("/graphics/pieces/students/student_blue.png");
                        school.add(studentBlue, i, getColorPlace("blue"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfRedStudents(); i++) {
                        ImageView studentRed = new ImageView("/graphics/pieces/students/student_red.png");
                        school.add(studentRed, i, getColorPlace("red"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfGreenStudents(); i++) {
                        ImageView studentGreen = new ImageView("/graphics/pieces/students/student_green.png");
                        school.add(studentGreen, i, getColorPlace("green"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfPinkStudents(); i++) {
                        ImageView studentPink = new ImageView("/graphics/pieces/students/student_pink.png");
                        school.add(studentPink, i, getColorPlace("pink"));
                    }
                    for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfYellowStudents(); i++) {
                        ImageView studentYellow = new ImageView("/graphics/pieces/students/student_yellow.png");
                        school.add(studentYellow, i, getColorPlace("yellow"));
                    }
                }
            });
        });

        //PROFESSORI
        professors.forEach(professor -> {
            players.forEach(player -> {
                if (professor.getHeldBy() == player) {
                    ImageView profImage = null;
                    String color = "";
                    switch (professor.getColor()) {
                        case BLUE:
                            profImage = new ImageView("graphics/pieces/professors/teacher_blue.png");
                            color = "blue";
                            break;
                        case RED:
                            profImage = new ImageView("graphics/pieces/professors/teacher_red.png");
                            color = "red";
                            break;
                        case PINK:
                            profImage = new ImageView("graphics/pieces/professors/teacher_pink.png");
                            color = "pink";
                            break;
                        case GREEN:
                            profImage = new ImageView("graphics/pieces/professors/teacher_green.png");
                            color = "green";
                            break;
                        case YELLOW:
                            profImage = new ImageView("graphics/pieces/professors/teacher_yellow.png");
                            color = "yellow";
                            break;
                    }
                    profImage.setFitWidth(30);
                    profImage.setFitHeight(30);
                    professorGrids.get(players.indexOf(player)).add(profImage, 0, getColorPlace(color));
                }
            });
        });


        // OK! TORRI
        players.forEach(player -> {
            towerGrids.forEach(tower -> {
                tower.setAlignment(Pos.CENTER);
                if (players.indexOf(player) == towerGrids.indexOf(tower)) {
                    for (int i = 0; i < player.getSchoolBoard().getNumOfTowers(); i++) {
                        ImageView towerImage = null;
                        switch (player.getSchoolBoard().getTowerColor()) {
                            case BLACK:
                                towerImage = new ImageView("graphics/pieces/towers/black_tower.png");
                                break;
                            case WHITE:
                                towerImage = new ImageView("graphics/pieces/towers/white_tower.png");
                                break;
                            case GREY:
                                towerImage = new ImageView("graphics/pieces/towers/grey_tower.png");
                                break;
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
            characterCards.forEach(card -> card.setVisible(false));
            coins.forEach(coin -> coin.setVisible(false));
            coinLabels.forEach(coinLabel -> coinLabel.setVisible(false));

        } else {
            List<CharacterCard> characters = this.gui.getClientModel().getServermodel().getTable().getCharacters();
            List<String> characterCardsString = new ArrayList<>();
            characters.forEach(character -> characterCardsString.add(character.getName()));
            toImageCharacters(characterCardsString, characterCards);
            players.forEach(player -> {
                coinLabels.forEach(coinLabel -> {
                    if (coinLabels.indexOf(coinLabel) == players.indexOf(player)) {
                        coinLabel.setText("" + player.getCoins());
                    }
                });
            });
        }

        // OK! NOMI E NUMERO GIOCATORI
        players.forEach(player -> {
            playerNames.forEach(playerLabel -> {
                if (players.indexOf(player) == playerNames.indexOf(playerLabel)) {
                    playerLabel.setText(player.getNickname());
                }
            });
        });

        if (players.size() < 4) {
            playerNames.get(3).setVisible(false);
            assistantCards.get(3).setVisible(false);
            schools.get(3).setVisible(false);
            coins.get(3).setVisible(false);
            coinLabels.get(3).setVisible(false);
        }
        if (players.size() < 3) {
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

    public void setOnIsland() {
        this.gui.openNewWindow("MoveToIsland");
    }

    public void characterCard1(MouseEvent mouseEvent) {

    }

    public void characterCard2(MouseEvent mouseEvent) {

    }

    public void characterCard3(MouseEvent mouseEvent) {

    }

    public void move(MouseEvent mouseEvent) {
        this.gui.openNewWindow("MoveMotherNature");
    }

    public static void populateGrid(GridPane grid, int init, int cols, int green, int red, int blue, int pink, int yellow) {
        int position = init;
        for (int i = 0; i < green; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_green.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < red; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_red.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < yellow; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_yellow.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < pink; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_pink.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < blue; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_blue.png");
            student.setFitHeight(30);
            student.setFitWidth(30);
            grid.add(student, position % cols, position / cols);
            position++;
        }
    }
}
