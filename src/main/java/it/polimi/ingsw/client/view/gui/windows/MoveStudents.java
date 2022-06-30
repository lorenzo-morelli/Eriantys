package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.gui.Game;
import it.polimi.ingsw.client.view.gui.GuiView;
import it.polimi.ingsw.client.view.gui.Position;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.Game.setShadow;
import static it.polimi.ingsw.client.view.gui.GuiView.canOpenWindow;
import static it.polimi.ingsw.client.view.gui.GuiView.windowNode;

public class MoveStudents implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private GridPane islandGrid;
    @FXML
    private Label notice = new Label();
    private PeopleColor studentColor;
    private Player currentPlayer;

    @FXML
    private ImageView blue;
    @FXML
    private ImageView green;
    @FXML
    private ImageView pink;
    @FXML
    private ImageView red;
    @FXML
    private ImageView yellow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        windowNode = blue;
        this.notice.setText("");
        Position pos = new Position();
        ArrayList<Island> islands = this.guiView.getClientModel().getServerModel().getTable().getIslands();
        ArrayList<ImageView> students = new ArrayList<>(Arrays.asList(blue, green, pink, red, yellow));
        students.forEach(Game::setShadow);
        students.forEach(student -> student.setCursor(Cursor.HAND));
        currentPlayer = this.guiView.getClientModel().getServerModel().getCurrentPlayer();
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
                setShadow(islandImage);
                islandImage.setCursor(Cursor.HAND);
                islandGrid.add(islandImage, pos.islandX(islands.indexOf(island)), pos.islandY(islands.indexOf(island)));
                islandImage.setOnMouseClicked((event) -> {
                    if (studentColor == null) {
                        notice.setText("ERROR: Please select the student you want to move");
                    } else {
                        this.guiView.getClientModel().setTypeOfRequest("ISLAND");
                        this.guiView.getClientModel().setChosenIsland(islands.indexOf(island));
                        this.guiView.getClientModel().setResponse(true);
                        this.guiView.getClientModel().setPingMessage(false);
                        this.guiView.getClientModel().setChosenColor(studentColor);
                        this.guiView.response();
                        Gson gson = new Gson();
                        Network.send(gson.toJson(this.guiView.getClientModel()));
                        canOpenWindow = true;
                        this.guiView.closeWindow(event);
                    }
                });
            });
        }
    }
    @FXML
    private void setBlue(MouseEvent mouseEvent) {
        setColor("blue", mouseEvent);
    }

    @FXML
    private void setGreen(MouseEvent mouseEvent) {
        setColor("green", mouseEvent);
    }

    @FXML
    private void setPink(MouseEvent mouseEvent) {
        setColor("pink", mouseEvent);
    }

    @FXML
    private void setRed(MouseEvent mouseEvent) {
        setColor("red", mouseEvent);
    }

    @FXML
    private void setYellow(MouseEvent mouseEvent) {
        setColor("yellow", mouseEvent);
    }

    /**
     * This method is used to set color based on what button has been pressed.
     * @param color the chosen color.
     * @param mouseEvent the event necessary to close the window.
     */
    private void setColor(String color, MouseEvent mouseEvent) {
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
            this.guiView.getClientModel().setTypeOfRequest("SCHOOL");
            this.guiView.getClientModel().setResponse(true);
            this.guiView.getClientModel().setPingMessage(false);
            this.guiView.getClientModel().setChosenColor(studentColor);
            this.guiView.response();
            Gson gson = new Gson();
            Network.send(gson.toJson(this.guiView.getClientModel()));
            studentColor = null;
            this.guiView.closeWindow(mouseEvent);
        }
    }
}
