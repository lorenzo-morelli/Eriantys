package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.view.gui.Position;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currentCharacter;
import static it.polimi.ingsw.client.GUI.isCardUsed;

public class Character implements Initializable {
    private final GUI gui = new GUI();
    private boolean flagIsland = false;
    private boolean flagColor = false;
    private int chosenIsland;
    private PeopleColor chosenColor;

    @FXML
    private Label explaination = new Label();
    @FXML
    private Label notice = new Label();
    @FXML
    private Label name = new Label();

    @FXML
    private GridPane islandGrid;
    @FXML
    private ImageView student1;
    @FXML
    private ImageView student2;
    @FXML
    private ImageView student3;
    @FXML
    private ImageView student4;
    @FXML
    private ImageView student5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notice.setText("");
        name.setText(currentCharacter.getName());
        ArrayList<Island> islands = this.gui.getClientModel().getServermodel().getTable().getIslands();
        ArrayList<ImageView> students = new ArrayList<>(Arrays.asList(student1, student2, student3, student4, student5));

        chosenColor = null;
        chosenIsland = -1;
        flagColor = false;
        flagIsland = false;

        switch (currentCharacter.getName()) {
            case "KNIGHT":
                explaination.setText("During the influence calculation this turn, you count as having 2 more influence");
                students.forEach(student -> student.setVisible(false));
                flagIsland = true;
                flagColor = true;
                break;
            case "CENTAUR":
                explaination.setText("When resolving a conquering on an island, towers do not count towards influence");
                students.forEach(student -> student.setVisible(false));
                flagIsland = true;
                flagColor = true;
                break;
            case "FARMER":
                explaination.setText("During this turn, you take control of any number of professors even if you have the same number of student as the player who currently controls them");
                students.forEach(student -> student.setVisible(false));
                flagIsland = true;
                flagColor = true;
                break;
            case "POSTMAN":
                explaination.setText("You can move mother nature up to 2 additional islands that is indicated by the assistant card you've played");
                students.forEach(student -> student.setVisible(false));
                flagIsland = true;
                flagColor = true;
                break;
            case "MONK":
                explaination.setText("Take 1 student from this card and place it on an island of your choice");
                this.selectStudent(students, this.gui.getClientModel().getServermodel().getTable().getMonkSet());
                this.selectIsland(islands);
                break;
            case "PRINCESS":
                explaination.setText("You can select 1 student from this card and place it in your dinner table");
                this.selectStudent(students, this.gui.getClientModel().getServermodel().getTable().getPrincessSet());
                flagIsland = true;
                break;
            case "MUSHROOMHUNTER":
                explaination.setText("Choose a color of a student: during the influence calculation this turn, that color adds no influence");
                this.selectStudent(students, null);
                flagIsland = true;
                break;
            case "HERALD":
                explaination.setText("Choose an island and resolve the island as if mother nature had ended her movement there. Mother nature will still move and the island where she ends her movement will also be resolved");
                students.forEach(student -> student.setVisible(false));
                this.selectIsland(islands);
                flagColor = true;
                break;
            case "GRANNY":
                explaination.setText("Place a No Entry tile on an island of your choice. The first time mother nature ends her movement there, the influence won't be calculated");
                students.forEach(student -> student.setVisible(false));
                this.selectIsland(islands);
                flagColor = true;
                break;
            case "THIEF":
                explaination.setText("Choose a type of student: every player (including yourself) must return 3 students of that type from their dining room to the bag. If any player has fewer than 3 students of that type, return as many students as they have");
                this.selectStudent(students, null);
                flagIsland = true;
                break;
        }
    }

    public void selectStudent(ArrayList<ImageView> students, StudentSet studentSet) {
        setToBlackAndWhite(students, studentSet, 0);
        students.forEach(student -> student.setOnMouseClicked(event -> {
            switch (students.indexOf(student)) {
                case 0:
                    if (studentSet == null || studentSet.getNumOfBlueStudents() > 0) {
                        chosenColor = PeopleColor.BLUE;
                        flagColor = true;
                    }
                    break;
                case 1:
                    if (studentSet == null || studentSet.getNumOfGreenStudents() > 0) {
                        chosenColor = PeopleColor.GREEN;
                        flagColor = true;
                    }
                    break;
                case 2:
                    if (studentSet == null || studentSet.getNumOfPinkStudents() > 0) {
                        chosenColor = PeopleColor.PINK;
                        flagColor = true;
                    }
                    break;
                case 3:
                    if (studentSet == null || studentSet.getNumOfRedStudents() > 0) {
                        chosenColor = PeopleColor.RED;
                        flagColor = true;
                    }
                    break;
                case 4:
                    if (studentSet == null || studentSet.getNumOfYellowStudents() > 0) {
                        chosenColor = PeopleColor.YELLOW;
                        flagColor = true;
                    }
                    break;
            }
            System.out.println("scelto: " + chosenColor.name());
            if (chosenColor == null) {
                notice.setText("ERROR: Color unavailable!");
            } else {
                notice.setText("");
            }
        }));
    }

    public void selectIsland(ArrayList<Island> islands) {
        Position pos = new Position();
        islands.forEach(island -> {
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
            islandImage.setFitHeight(60);
            islandImage.setFitWidth(60);
            islandGrid.add(islandImage, pos.islandX(islands.indexOf(island)), pos.islandY(islands.indexOf(island)));
            islandImage.setOnMouseClicked(event -> {
                chosenIsland = islands.indexOf(island);
                flagIsland = true;
            });

        });
    }

    public void setToBlackAndWhite(ArrayList<ImageView> images, StudentSet studentSet, int remaining) {
        images.forEach(student -> {
            switch (images.indexOf(student)) {
                case 0:
                    if (studentSet != null && studentSet.getNumOfBlueStudents() <= remaining) {
                        student.setImage(new Image("/graphics/pieces/students/student_blue_bw.png"));
                    }
                    break;
                case 1:
                    if (studentSet != null && studentSet.getNumOfGreenStudents() <= remaining) {
                        student.setImage(new Image("/graphics/pieces/students/student_green_bw.png"));
                    }
                    break;
                case 2:
                    if (studentSet != null && studentSet.getNumOfPinkStudents() <= remaining) {
                        student.setImage(new Image("/graphics/pieces/students/student_pink_bw.png"));
                    }
                    break;
                case 3:
                    if (studentSet != null && studentSet.getNumOfRedStudents() <= remaining) {
                        student.setImage(new Image("/graphics/pieces/students/student_red_bw.png"));
                    }
                    break;
                case 4:
                    if (studentSet != null && studentSet.getNumOfYellowStudents() <= remaining) {
                        student.setImage(new Image("/graphics/pieces/students/student_yellow_bw.png"));
                    }
                    break;
            }
        });
    }

    public void okay(MouseEvent mouseEvent) throws InterruptedException {
        if (isCardUsed) {
            notice.setText("You can use only one card at a time!");
        } else if (this.gui.getClientModel().getServermodel().getcurrentPlayer().getCoins() < currentCharacter.getCost()) {
            notice.setText("You don't have enough coins! :(");
        } else if (!flagColor || !flagIsland) {
            notice.setText("Information missing!");
        } else {
            this.gui.getClientModel().setTypeOfRequest(currentCharacter.getName());
            this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
            this.gui.getClientModel().setPingMessage(false);
            if (chosenColor != null) {
                this.gui.getClientModel().setChoosedColor(chosenColor);
            }
            if (chosenIsland != -1) {
                this.gui.getClientModel().setChoosedIsland(chosenIsland);
            }
            Gson gson = new Gson();
            Network.send(gson.toJson(this.gui.getClientModel()));
            isCardUsed = true;
            this.gui.closeWindow(mouseEvent);
        }
    }
}