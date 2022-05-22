package it.polimi.ingsw.utils.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MoveToSchool implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setBlue(MouseEvent mouseEvent) {
        this.gui.getClientModel().setChoosedColor(PeopleColor.BLUE);
    }

    public void setGreen(MouseEvent mouseEvent) {
        this.gui.getClientModel().setChoosedColor(PeopleColor.GREEN);
    }

    public void setPink(MouseEvent mouseEvent) {
        this.gui.getClientModel().setChoosedColor(PeopleColor.PINK);
    }

    public void setRed(MouseEvent mouseEvent) {
        this.gui.getClientModel().setChoosedColor(PeopleColor.RED);
    }

    public void setYellow(MouseEvent mouseEvent) {
        this.gui.getClientModel().setChoosedColor(PeopleColor.YELLOW);
    }
}
