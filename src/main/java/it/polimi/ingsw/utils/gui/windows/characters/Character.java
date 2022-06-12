package it.polimi.ingsw.utils.gui.windows.characters;

import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currentCharacter;

public class Character implements Initializable {
    private final GUI gui = new GUI();
    public Label explaination = new Label();
    private boolean canClose = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CharacterCard card = currentCharacter;
        switch (card.getName()) {
            case "KNIGHT":
                explaination.setText("During the influence calculation this turn, you count as having 2 more influence");
                break;
            case "CENTAUR":
                explaination.setText("When resolving a conquering on an island, towers do not count towards influence");
                break;
            case "FARMER":
                explaination.setText("During this turn, you take control of any number of professors even if you have the same number of student as the player who currently controls them");
                break;
            case "POSTMAN":
                explaination.setText("You can move mother nature up to 2 additional islands that is indicated by the assistant card you've played");
                break;
            case "MONK":
                explaination.setText("You can select 1 student from this card and place it on an island of your choice");

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
        canClose = true;
    }

    public void okay(MouseEvent mouseEvent) {
        this.gui.closeWindow(mouseEvent);
    }
}
