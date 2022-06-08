package it.polimi.ingsw.utils.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.gui.Position.islandX;
import static it.polimi.ingsw.utils.gui.Position.islandY;

public class MoveStudents implements Initializable {
    private final GUI gui = new GUI();
    public GridPane islandGrid;
    public Label notice;
    PeopleColor studentColor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notice.setText("");

        int numIslands = 12;
        if (islandGrid != null) {
            for (int i = 0; i < numIslands; i++) {
                ImageView island = new ImageView();
                switch (i % 3) {
                    case 0: island = new ImageView("/graphics/pieces/islands/island1.png"); break;
                    case 1: island = new ImageView("/graphics/pieces/islands/island2.png"); break;
                    case 2: island = new ImageView("/graphics/pieces/islands/island3.png"); break;
                }
                island.setFitHeight(60);
                island.setFitWidth(60);
                islandGrid.add(island, islandX(i), islandY(i));
                int finalI = i;
                island.setOnMouseClicked((event) -> {
                    System.out.println(finalI);
                    if (studentColor == null) {
                        notice.setText("ERROR: Please select the student you want to move");
                    } else {
                        //todo: modifica il model
                        this.gui.closeWindow(event);
                    }
                });
            }
        }
    }

    public void setBlue(MouseEvent mouseEvent) {
       setColor("blue");
    }

    public void setGreen(MouseEvent mouseEvent) {
        setColor("green");
    }

    public void setPink(MouseEvent mouseEvent) {
        setColor("pink");
    }

    public void setRed(MouseEvent mouseEvent) {
        setColor("red");
    }

    public void setYellow(MouseEvent mouseEvent) {
        setColor("yellow");
    }

    public void setColor(String color) {
        if (true) { //todo: ha quello studente
            switch (color) {
                case "yellow": studentColor = PeopleColor.YELLOW; break;
                case "red": studentColor = PeopleColor.RED; break;
                case "blue": studentColor = PeopleColor.BLUE; break;
                case "green": studentColor = PeopleColor.GREEN; break;
                case "pink": studentColor = PeopleColor.PINK; break;
            }
            System.out.println("selected " + color);
        } else {
            notice.setText("ERROR: Student unavailable!");
        }
    }
}
