package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.Position.islandX;
import static it.polimi.ingsw.client.view.gui.Position.islandY;

public class MoveStudents implements Initializable {
    private final GUI gui = new GUI();
    public GridPane islandGrid;
    public Label notice = new Label();
    private PeopleColor studentColor;
    private Player currentPlayer;

    public ImageView blue;
    public ImageView green;
    public ImageView pink;
    public ImageView red;
    public ImageView yellow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notice.setText("");
        ArrayList<Island> islands = this.gui.getClientModel().getServermodel().getTable().getIslands();
        ArrayList<ImageView> students = new ArrayList<>(Arrays.asList(blue, green, pink, red, yellow));
        currentPlayer = this.gui.getClientModel().getServermodel().getcurrentPlayer();
        Character character = new Character();
        character.setToBlackAndWhite(students, currentPlayer.getSchoolBoard().getEntranceSpace(), 0);
        if (islandGrid != null) {
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
                    if (studentColor == null) {
                        notice.setText("ERROR: Please select the student you want to move");
                    } else {
                        this.gui.getClientModel().setTypeOfRequest("ISLAND");
                        this.gui.getClientModel().setChoosedIsland(islands.indexOf(island));
                        this.gui.getClientModel().setResponse(true);
                        this.gui.getClientModel().setPingMessage(false);
                        this.gui.getClientModel().setChoosedColor(studentColor);
                        Gson gson = new Gson();
                        try {
                            Network.send(gson.toJson(this.gui.getClientModel()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.gui.closeWindow(event);
                    }
                });
            });
        }
    }
    @FXML
    private void setBlue(MouseEvent mouseEvent) throws InterruptedException {
        setColor("blue", mouseEvent);
    }

    @FXML
    private void setGreen(MouseEvent mouseEvent) throws InterruptedException {
        setColor("green", mouseEvent);
    }

    @FXML
    private void setPink(MouseEvent mouseEvent) throws InterruptedException {
        setColor("pink", mouseEvent);
    }

    @FXML
    private void setRed(MouseEvent mouseEvent) throws InterruptedException {
        setColor("red", mouseEvent);
    }

    @FXML
    private void setYellow(MouseEvent mouseEvent) throws InterruptedException {
        setColor("yellow", mouseEvent);
    }

    /**
     * This method is used to set color based on what button has been pressed.
     * @param color the chosen color.
     * @param mouseEvent the event necessary to close the window.
     * @throws InterruptedException
     */
    private void setColor(String color, MouseEvent mouseEvent) throws InterruptedException {
        int red = currentPlayer.getSchoolBoard().getEntranceSpace().getNumOfRedStudents();
        int blue = currentPlayer.getSchoolBoard().getEntranceSpace().getNumOfBlueStudents();
        int green = currentPlayer.getSchoolBoard().getEntranceSpace().getNumOfGreenStudents();
        int yellow = currentPlayer.getSchoolBoard().getEntranceSpace().getNumOfYellowStudents();
        int pink = currentPlayer.getSchoolBoard().getEntranceSpace().getNumOfPinkStudents();
        switch (color) {
            case "yellow":
                if (yellow == 0) notice.setText("ERROR: Student unavailable!");
                else studentColor = PeopleColor.YELLOW;
                break;
            case "red":
                if (red == 0) notice.setText("ERROR: Student unavailable!");
                else studentColor = PeopleColor.RED;
                break;
            case "blue":
                if (blue == 0) notice.setText("ERROR: Student unavailable!");
                else studentColor = PeopleColor.BLUE;
                break;
            case "green":
                if (green == 0) notice.setText("ERROR: Student unavailable!");
                else studentColor = PeopleColor.GREEN;
                break;
            case "pink":
                if (pink == 0) notice.setText("ERROR: Student unavailable!");
                else studentColor = PeopleColor.PINK;
                break;
        }
        if (islandGrid == null && studentColor != null) {
            this.gui.getClientModel().setTypeOfRequest("SCHOOL");
            this.gui.getClientModel().setResponse(true);
            this.gui.getClientModel().setPingMessage(false);
            this.gui.getClientModel().setChoosedColor(studentColor);
            Gson gson = new Gson();
            Network.send(gson.toJson(this.gui.getClientModel()));
            studentColor = null;
            this.gui.closeWindow(mouseEvent);
        }
    }
}
