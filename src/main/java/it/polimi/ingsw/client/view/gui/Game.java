package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.server.model.enums.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.GuiView.*;

public class Game implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Label phaseLabel;
    @FXML
    private Label turnLabel;

    @FXML
    private TextArea log;

    @FXML
    private Button assistantCardBtn;
    @FXML
    private Button setOnSchoolBtn;
    @FXML
    private Button setOnIslandBtn;
    @FXML
    private Button moveBtn;
    @FXML
    private Button cloudBtn;

    @FXML
    private GridPane islandGrid;

    @FXML
    private GridPane entrance1Grid;
    @FXML
    private GridPane entrance2Grid;
    @FXML
    private GridPane entrance3Grid;
    @FXML
    private GridPane entrance4Grid;

    @FXML
    private GridPane school1Grid;
    @FXML
    private GridPane school2Grid;
    @FXML
    private GridPane school3Grid;
    @FXML
    private GridPane school4Grid;

    @FXML
    private GridPane tower1Grid;
    @FXML
    private GridPane tower2Grid;
    @FXML
    private GridPane tower3Grid;
    @FXML
    private GridPane tower4Grid;

    @FXML
    private ImageView school1;
    @FXML
    private ImageView school2;
    @FXML
    private ImageView school3;
    @FXML
    private ImageView school4;

    @FXML
    private GridPane professor1Grid;
    @FXML
    private GridPane professor2Grid;
    @FXML
    private GridPane professor3Grid;
    @FXML
    private GridPane professor4Grid;

    @FXML
    private ImageView motherNature;

    @FXML
    private ImageView assistantCard1;
    @FXML
    private ImageView assistantCard2;
    @FXML
    private ImageView assistantCard3;
    @FXML
    private ImageView assistantCard4;

    @FXML
    private ImageView characterCard1;
    @FXML
    private ImageView characterCard2;
    @FXML
    private ImageView characterCard3;

    @FXML
    private GridPane character1Grid;
    @FXML
    private GridPane character2Grid;
    @FXML
    private GridPane character3Grid;

    @FXML
    private ImageView coin1;
    @FXML
    private ImageView coin2;
    @FXML
    private ImageView coin3;
    @FXML
    private ImageView coin4;

    @FXML
    private Label coin1Label;
    @FXML
    private Label coin2Label;
    @FXML
    private Label coin3Label;
    @FXML
    private Label coin4Label;

    @FXML
    private Label cost1 = new Label();
    @FXML
    private Label cost2 = new Label();
    @FXML
    private Label cost3 = new Label();

    @FXML
    private Label playerName1;
    @FXML
    private Label playerName2;
    @FXML
    private Label playerName3;
    @FXML
    private Label playerName4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String imageURL = "/graphics/buttons/button_unavailable.png";
        currNode = phaseLabel;
        Position pos = new Position();
        phaseLabel.setText(gameState);
        log.setText(logText);
        turnLabel.setText("This is " + this.guiView.getClientModel().getServerModel().getCurrentPlayer().getNickname() + "'s turn");
        if (this.guiView.getClientModel().getTypeOfRequest().equals("GAMEEND")) {
            windowNode = phaseLabel;
            try {
                this.guiView.openNewWindow("EndGame");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(assistantCardBtn, setOnSchoolBtn, setOnIslandBtn, moveBtn, cloudBtn));
        buttons.forEach(Game::setShadow);
        if (myTurn) {
            switch (this.guiView.getClientModel().getTypeOfRequest()) {
                case "CHOOSEASSISTANTCARD":
                    setOnSchoolBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    setOnIslandBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    moveBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    cloudBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    break;
                case "CHOOSEWHERETOMOVESTUDENTS":
                    assistantCardBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    moveBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    cloudBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    break;
                case "CHOOSEWHERETOMOVEMOTHER":
                    assistantCardBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    setOnSchoolBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    setOnIslandBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    cloudBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    break;
                case "CHOOSECLOUDS":
                    assistantCardBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    setOnSchoolBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    setOnIslandBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    moveBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
                    break;
            }
        } else {
            assistantCardBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
            setOnSchoolBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
            setOnIslandBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
            moveBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
            cloudBtn.setStyle("-fx-background-image: url(" + imageURL + ");");
        }

        GameMode gameMode = this.guiView.getClientModel().getServerModel().getGameMode();
        int motherNaturePos = this.guiView.getClientModel().getServerModel().getTable().getMotherNaturePosition();
        ArrayList<Island> islands = this.guiView.getClientModel().getServerModel().getTable().getIslands();
        ArrayList<Player> players = this.guiView.getClientModel().getServerModel().getPlayers();
        players.forEach(player -> System.out.println(player.getChosenCard() != null ? player.getChosenCard().getValues() : "non scelta"));

        ArrayList<Professor> professors = this.guiView.getClientModel().getServerModel().getTable().getProfessors();
        ArrayList<Cloud> clouds = this.guiView.getClientModel().getServerModel().getTable().getClouds();
        ArrayList<Team> teams = null;
        if (players.size() == 4) {
            teams = this.guiView.getClientModel().getServerModel().getTeams();
        }
        ArrayList<Label> playerNames = new ArrayList<>(Arrays.asList(playerName1, playerName2, playerName3, playerName4));
        ArrayList<ImageView> assistantCards = new ArrayList<>(Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4));
        ArrayList<ImageView> characterCardsImages = new ArrayList<>(Arrays.asList(characterCard1, characterCard2, characterCard3));
        ArrayList<GridPane> professorGrids = new ArrayList<>(Arrays.asList(professor1Grid, professor2Grid, professor3Grid, professor4Grid));
        ArrayList<GridPane> entranceGrids = new ArrayList<>(Arrays.asList(entrance1Grid, entrance2Grid, entrance3Grid, entrance4Grid));
        ArrayList<GridPane> schoolGrids = new ArrayList<>(Arrays.asList(school1Grid, school2Grid, school3Grid, school4Grid));
        ArrayList<GridPane> towerGrids = new ArrayList<>(Arrays.asList(tower1Grid, tower2Grid, tower3Grid, tower4Grid));
        ArrayList<ImageView> schools = new ArrayList<>(Arrays.asList(school1, school2, school3, school4));
        ArrayList<ImageView> coins = new ArrayList<>(Arrays.asList(coin1, coin2, coin3, coin4));
        ArrayList<Label> coinLabels = new ArrayList<>(Arrays.asList(coin1Label, coin2Label, coin3Label, coin4Label));
        ArrayList<Label> costs = new ArrayList<>(Arrays.asList(cost1, cost2, cost3));


        // INITIALIZE ISLANDS
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

            // INITIALIZE STUDENTS INHABITANTS
            GridPane students = new GridPane();
            students.setAlignment(Pos.CENTER);
            StudentSet islandSet = island.getInhabitants();
            populateGrid(students, 0, 3, islandSet);
            tile.getChildren().add(students);

            //INITIALIZE BLOCKS
            if (island.isBlocked()) {
                GridPane blocks = new GridPane();
                blocks.setPadding(new Insets(20));
                blocks.setAlignment(Pos.BOTTOM_LEFT);
                ImageView block = new ImageView("/graphics/pieces/islands/deny_island_icon.png");
                setShadow(block);
                block.setFitHeight(50);
                block.setFitWidth(50);
                blocks.addRow(1, block);
                tile.getChildren().add(blocks);
            }

            // INITIALIZE TOWERS IN ISLANDS
            GridPane towers = new GridPane();
            towers.setAlignment(Pos.BOTTOM_CENTER);
            towers.setHgap(-15);
            for (int j = 0; j < island.getNumberOfTowers(); j++) {
                ImageView towerImage = null;
                switch (island.getTowerColor()) {
                    case BLACK:
                        towerImage = new ImageView("graphics/pieces/towers/black_tower.png");
                        break;
                    case GREY:
                        towerImage = new ImageView("graphics/pieces/towers/grey_tower.png");
                        break;
                    case WHITE:
                        towerImage = new ImageView("graphics/pieces/towers/white_tower.png");
                        break;
                }
                towerImage.setFitWidth(40);
                towerImage.setFitHeight(40);
                setShadow(towerImage);
                towers.addRow(1, towerImage);
            }
            tile.getChildren().add(towers);

            // INITIALIZE MOTHER NATURE
            if (islands.indexOf(island) == motherNaturePos) {
                setShadow(motherNature);
                tile.getChildren().add(motherNature);
                StackPane.setAlignment(motherNature, Pos.TOP_CENTER);
            }
            islandGrid.add(tile, pos.islandX(islands.indexOf(island)), pos.islandY(islands.indexOf(island)));

        });

        // INITIALIZE CLOUDS
        clouds.forEach(cloud -> {
            StackPane tile = new StackPane();
            ImageView cloudImage = new ImageView("/graphics/pieces/clouds/cloud_card.png");
            imageResize(cloudImage, 120);
            tile.getChildren().add(cloudImage);
            GridPane studentsCloudGrid = new GridPane();
            studentsCloudGrid.setAlignment(Pos.CENTER);
            studentsCloudGrid.setHgap(7);
            studentsCloudGrid.setVgap(7);
            StudentSet cloudSet = cloud.getStudentsAccumulator();

            populateGrid(studentsCloudGrid, 0, 2, cloudSet);
            tile.getChildren().add(studentsCloudGrid);
            islandGrid.add(tile, pos.cloudX(clouds.indexOf(cloud)), pos.cloudY(clouds.indexOf(cloud)));
        });

        // INITIALIZE STUDENTS IN ENTRANCE
        entranceGrids.forEach(entrance -> entrance.setAlignment(Pos.CENTER));
        players.forEach(player -> {
            StudentSet entranceSet = player.getSchoolBoard().getEntranceSpace();
            populateGrid(entranceGrids.get(players.indexOf(player)), 1, 2, entranceSet);
        });

        // INITIALIZE STUDENTS IN SCHOOL
        players.forEach(player -> {
            GridPane school = schoolGrids.get(players.indexOf(player));
            if (school != null) {
                school.setAlignment(Pos.CENTER);
                for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfBlueStudents(); i++) {
                    ImageView studentBlue = new ImageView("/graphics/pieces/students/student_blue.png");
                    imageResize(studentBlue, 27);
                    setShadow(studentBlue);
                    school.add(studentBlue, i, this.getColorPlace("blue"));
                }
                for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfRedStudents(); i++) {
                    ImageView studentRed = new ImageView("/graphics/pieces/students/student_red.png");
                    imageResize(studentRed, 27);
                    setShadow(studentRed);
                    school.add(studentRed, i, this.getColorPlace("red"));
                }
                for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfGreenStudents(); i++) {
                    ImageView studentGreen = new ImageView("/graphics/pieces/students/student_green.png");
                    imageResize(studentGreen, 27);
                    setShadow(studentGreen);
                    school.add(studentGreen, i, this.getColorPlace("green"));
                }
                for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfPinkStudents(); i++) {
                    ImageView studentPink = new ImageView("/graphics/pieces/students/student_pink.png");
                    imageResize(studentPink, 27);
                    setShadow(studentPink);
                    school.add(studentPink, i, this.getColorPlace("pink"));
                }
                for (int i = 0; i < player.getSchoolBoard().getDinnerTable().getNumOfYellowStudents(); i++) {
                    ImageView studentYellow = new ImageView("/graphics/pieces/students/student_yellow.png");
                    imageResize(studentYellow, 27);
                    setShadow(studentYellow);
                    school.add(studentYellow, i, this.getColorPlace("yellow"));
                }
            }
        });

        // INITIALIZE PROFESSORS
        professorGrids.forEach(prof -> prof.getChildren().clear());
        professors.forEach(prof -> {
            if (prof.getHeldBy() != null) {
                Player chosenPlayer = null;
                for (Player player : players) {
                    if (player.getNickname().equals(prof.getHeldBy().getNickname())) {
                        chosenPlayer = player;
                    }
                }
                if (chosenPlayer != null) {
                    ImageView profImage = null;
                    String color = "";
                    switch (prof.getColor()) {
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
                    imageResize(profImage, 30);
                    setShadow(profImage);
                    professorGrids.get(players.indexOf(chosenPlayer)).add(profImage, 0, getColorPlace(color));
                }
            }
        });

        // INITIALIZE TOWERS
        for (Player player : players) {
            if (teams == null || teams.get(0).getPlayer1().getNickname().equals(player.getNickname()) || teams.get(1).getPlayer1().getNickname().equals(player.getNickname())) {
                GridPane tower = towerGrids.get(players.indexOf(player));
                tower.setAlignment(Pos.CENTER);
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
                    imageResize(towerImage, 50);
                    setShadow(towerImage);
                    tower.setHgap(-15);
                    tower.add(towerImage, i % 2, i / 2);
                }
            }
        }

        // INITIALIZE GAMEMODE
        if (gameMode.equals(GameMode.BEGINNER)) {
            characterCardsImages.forEach(card -> card.setVisible(false));
            coins.forEach(coin -> coin.setVisible(false));
            coinLabels.forEach(coinLabel -> coinLabel.setVisible(false));
            costs.forEach(cost -> cost.setVisible(false));
        } else {
            coins.forEach(Game::setShadow);
            ArrayList<CharacterCard> characterCards = this.guiView.getClientModel().getServerModel().getTable().getCharacters();
            toImageCharacters(characterCards, characterCardsImages);
            players.forEach(player -> {
                Label coinLabel = coinLabels.get(players.indexOf(player));
                coinLabel.setText("" + player.getCoins());
            });
            ArrayList<GridPane> characterGrids = new ArrayList<>(Arrays.asList(character1Grid, character2Grid, character3Grid));
            costs.forEach(cost -> cost.setText("Cost: " + characterCards.get(costs.indexOf(cost)).getCost()));
            characterGrids.forEach(grid -> {
                StudentSet studentSet = null;
                switch (characterCards.get(characterGrids.indexOf(grid)).getName()) {
                    case "MONK":
                        studentSet = guiView.getClientModel().getServerModel().getTable().getMonkSet();
                        break;
                    case "PRINCESS":
                        studentSet = guiView.getClientModel().getServerModel().getTable().getPrincessSet();
                        break;
                    case "JESTER":
                        studentSet = guiView.getClientModel().getServerModel().getTable().getJesterSet();
                        break;
                    case "GRANNY":
                        for (int i = 0; i < this.guiView.getClientModel().getServerModel().getTable().getNumDivieti(); i++) {
                            ImageView block = new ImageView("/graphics/pieces/islands/deny_island_icon.png");
                            setShadow(block);
                            this.imageResize(block, 30);
                            grid.add(block, i % 2, i / 2);
                        }
                }
                if (studentSet != null) {
                    populateGrid(grid, 0, 2, studentSet);
                }
            });
            characterCardsImages.forEach(Game::setShadow);
            characterCards.forEach(card -> characterCardsImages.get(characterCards.indexOf(card)).setOnMouseClicked(event -> {
                if (!this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSEASSISTANTCARD") && canOpenWindow) {
                    currentCharacter = card;
                    try {
                        if (currentCharacter.getName().equals("MINSTRELL") || currentCharacter.getName().equals("JESTER")) {
                            guiView.openNewWindow("JesterAndMinstrell");
                        } else {
                            guiView.openNewWindow("Character");
                        }
                        canOpenWindow = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        // INITIALIZE PLAYERS AND NUMBER OF PLAYERS
        for (Player player : players) {
            String name;
            name = player.getNickname();
            if (teams != null) {
                String team;
                if (player.getNickname().equals(teams.get(0).getPlayer1().getNickname()) || player.getNickname().equals(teams.get(0).getPlayer2().getNickname())) {
                    team = "1";
                } else {
                    team = "2";
                }
                name = "TEAM " + team + " - " + name;
            }
            name = player.isDisconnected() ? name + " (disconnected)" : name;
            playerNames.get(players.indexOf(player)).setText(name);
        }
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

        // INITIALIZE ASSISTANT CARDS
        players.forEach(player -> {
            if (player.getChosenCard() != null) {
                Image assistantImage = new Image("/graphics/assistants/assistantCard" + (int) player.getChosenCard().getValues() + ".png");
                assistantCards.get(players.indexOf(player)).setImage(assistantImage);
                setShadow(assistantCards.get(players.indexOf(player)));
                assistantCards.get(players.indexOf(player)).setVisible(true);
            } else {
                assistantCards.get(players.indexOf(player)).setVisible(false);
            }
        });
    }

    /**
     * This event is called if the user wants to quit the game.
     * It will open a new window where a confirmation is asked.
     */
    @FXML
    private void quit() throws IOException {
        this.guiView.openNewWindow("Quit");
    }

    /**
     * This event will open the "Choose assistant card" window, to choose an assistant card.
     */
    @FXML
    private void assistant() throws IOException {
        if (this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSEASSISTANTCARD") && canOpenWindow && myTurn) {
            this.guiView.openNewWindow("ChooseAssistantCard");
            canOpenWindow = false;
        }
    }

    /**
     * This event will open the "Move to school" window, to move a student in the player's school.
     */
    @FXML
    private void setOnSchool() throws IOException {
        if (this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSEWHERETOMOVESTUDENTS") && canOpenWindow && myTurn) {
            this.guiView.openNewWindow("MoveToSchool");
            canOpenWindow = false;
        }
    }

    /**
     * This event will open the "Move to island" window, to move a student on an island.
     */
    @FXML
    private void setOnIsland() throws IOException {
        if (this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSEWHERETOMOVESTUDENTS") && canOpenWindow && myTurn) {
            this.guiView.openNewWindow("MoveToIsland");
            canOpenWindow = false;
        }
    }

    /**
     * This event will open the "Move mother nature" window, to move mother nature on the islands.
     */
    @FXML
    private void move() throws IOException {
        if (this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSEWHERETOMOVEMOTHER") && canOpenWindow && myTurn) {
            this.guiView.openNewWindow("MoveMotherNature");
            canOpenWindow = false;
        }
    }

    /**
     * This event will open the "Choose cloud" window, to choose a cloud from the center table.
     */
    @FXML
    private void cloud() throws IOException {
        if (this.guiView.getClientModel().getTypeOfRequest().equals("CHOOSECLOUDS") && myTurn) {
            this.guiView.openNewWindow("ChooseCloud");
            canOpenWindow = false;
        }
    }

    /**
     * This method helps to populate a grid with students.
     *
     * @param grid       the grid to populate.
     * @param init       the first position to start from.
     * @param cols       the numbers of columns to fill.
     * @param studentSet the set of student to fill the grid with.
     */
    public void populateGrid(GridPane grid, int init, int cols, StudentSet studentSet) {
        int green = studentSet.getNumOfGreenStudents();
        int blue = studentSet.getNumOfBlueStudents();
        int red = studentSet.getNumOfRedStudents();
        int yellow = studentSet.getNumOfYellowStudents();
        int pink = studentSet.getNumOfPinkStudents();
        int position = init;
        for (int i = 0; i < green; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_green.png");
            setShadow(student);
            this.imageResize(student, 25);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < red; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_red.png");
            setShadow(student);
            this.imageResize(student, 25);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < yellow; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_yellow.png");
            setShadow(student);
            this.imageResize(student, 25);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < pink; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_pink.png");
            setShadow(student);
            this.imageResize(student, 25);
            grid.add(student, position % cols, position / cols);
            position++;
        }
        for (int i = 0; i < blue; i++) {
            ImageView student = new ImageView("/graphics/pieces/students/student_blue.png");
            setShadow(student);
            this.imageResize(student, 25);
            grid.add(student, position % cols, position / cols);
            position++;
        }
    }

    /**
     * This method is used to resize an ImageView.
     *
     * @param image the node to resize.
     * @param size  the size of the node.
     */
    private void imageResize(ImageView image, int size) {
        image.setFitHeight(size);
        image.setFitWidth(size);
    }

    /**
     * This method helps to map the elements visually in the correct place with a given color, for example
     * green should be in the position 0, while yellow in the position 2.
     *
     * @param color the chosen color.
     * @return the index corresponding to the chosen color.
     */
    private int getColorPlace(String color) {
        int n = -1;
        switch (color) {
            case "green":
                n = 0;
                break;
            case "red":
                n = 1;
                break;
            case "yellow":
                n = 2;
                break;
            case "pink":
                n = 3;
                break;
            case "blue":
                n = 4;
                break;

        }
        return n;
    }

    /**
     * This method is used to convert the character cards into images.
     *
     * @param characterCard the list of the character cards provided by the client model.
     * @param images        the list of images to change based of the right character cards.
     */
    private void toImageCharacters(ArrayList<CharacterCard> characterCard, List<ImageView> images) {
        for (int i = 0; i < 3; i++) {
            Image character = null;
            switch (characterCard.get(i).getName()) {
                case "MONK":
                    character = new Image("graphics/characters/monk.jpg");
                    break;
                case "PRINCESS":
                    character = new Image("graphics/characters/princess.jpg");
                    break;
                case "MUSHROOMHUNTER":
                    character = new Image("graphics/characters/mushroom_hunter.jpg");
                    break;
                case "THIEF":
                    character = new Image("graphics/characters/thief.jpg");
                    break;
                case "FARMER":
                    character = new Image("graphics/characters/farmer.jpg");
                    break;
                case "CENTAUR":
                    character = new Image("graphics/characters/centaur.jpg");
                    break;
                case "KNIGHT":
                    character = new Image("graphics/characters/knight.jpg");
                    break;
                case "POSTMAN":
                    character = new Image("graphics/characters/postman.jpg");
                    break;
                case "GRANNY":
                    character = new Image("graphics/characters/granny.jpg");
                    break;
                case "JESTER":
                    character = new Image("graphics/characters/jester.jpg");
                    break;
                case "HERALD":
                    character = new Image("graphics/characters/herald.jpg");
                    break;
                case "MINSTRELL":
                    character = new Image("graphics/characters/minstrell.jpg");
                    break;
            }
            images.get(i).setImage(character);
        }
    }

    public static void setShadow(Node node) {
        node.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }
}
