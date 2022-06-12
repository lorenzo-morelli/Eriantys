package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currentCharacter;
import static it.polimi.ingsw.client.view.gui.Position.islandX;
import static it.polimi.ingsw.client.view.gui.Position.islandY;

public class Character implements Initializable {
    private final GUI gui = new GUI();
    public Label explaination = new Label();
    public Label notice = new Label();
    private boolean canClose = false;
    public GridPane islandGrid;
    public ImageView student1;
    public ImageView student2;
    public ImageView student3;
    public ImageView student4;
    public ImageView student5;
    private PeopleColor color;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CharacterCard card = currentCharacter;
        ArrayList<Island> islands = this.gui.getClientModel().getServermodel().getTable().getIslands();
        ArrayList<ImageView> students = new ArrayList<>(Arrays.asList(student1, student2, student3, student4, student5));

        switch (card.getName()) {
            case "KNIGHT":
                explaination.setText("During the influence calculation this turn, you count as having 2 more influence");
                canClose = true;
                break;
            case "CENTAUR":
                explaination.setText("When resolving a conquering on an island, towers do not count towards influence");
                canClose = true;
                break;
            case "FARMER":
                explaination.setText("During this turn, you take control of any number of professors even if you have the same number of student as the player who currently controls them");
                canClose = true;
                break;
            case "POSTMAN":
                explaination.setText("You can move mother nature up to 2 additional islands that is indicated by the assistant card you've played");
                canClose = true;
                break;
            case "MONK":
                explaination.setText("You can select 1 student from this card and place it on an island of your choice");
                students.forEach(student -> {
                    student.setOnMouseClicked(event -> {
                        switch (students.indexOf(student)) {
                            case 0:
                                color = PeopleColor.BLUE;
                                break;
                            case 1:
                                color = PeopleColor.GREEN;
                                break;
                            case 2:
                                color = PeopleColor.PINK;
                                break;
                            case 3:
                                color = PeopleColor.RED;
                                break;
                            case 4:
                                color = PeopleColor.YELLOW;
                                break;
                        }
                    });
                });
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
                    islandGrid.add(islandImage, islandX(islands.indexOf(island)), islandY(islands.indexOf(island)));
                    islandImage.setOnMouseClicked((event) -> {
                        if (color == null) {
                            notice.setText("Missing information!");
                        } else {
                            //todo invia al server
                            //todo fai piu righe (3?) di students.
                        }
                    });
                });
                break;
            case "PRINCESS":

                break;
            case "MUSHROOMHUNTER":

                break;
            case "HERALD":

                break;
            case "GRANNY":

                break;
            case "JESTER":

                break;
            case "MINSTRELL":

                break;
            case "THIEF":

                break;
        }
    }

    public void okay(MouseEvent mouseEvent) {
        if (canClose) {
            this.gui.closeWindow(mouseEvent);
        } else {
            notice.setText("Information missing!");
        }
    }
}
