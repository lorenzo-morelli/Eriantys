package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.gui.GuiView;
import it.polimi.ingsw.client.view.gui.Position;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.Game.setShadow;
import static it.polimi.ingsw.client.view.gui.GuiView.windowNode;

public class MoveMotherNature implements Initializable {
    private final GuiView guiView = new GuiView();
    private int distance;
    @FXML
    private GridPane islandGrid;
    @FXML
    private Label notice = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        windowNode = islandGrid;
        Position pos = new Position();
        notice.setText("");
        ArrayList<Island> islands = this.guiView.getClientModel().getServerModel().getTable().getIslands();
        int motherNaturePos = this.guiView.getClientModel().getServerModel().getTable().getMotherNaturePosition();
        int moves = this.guiView.getClientModel().getServerModel().getCurrentPlayer().getChoosedCard().getMoves();
        distance = 0;
        islands.forEach(island -> {
            distance = islands.indexOf(island) - motherNaturePos;
            while (distance <= 0) {
                distance += islands.size();
            }
            ImageView islandImage = new ImageView();
            boolean canMove = distance <= moves;
            switch (islands.indexOf(island) % 3) {
                case 0:
                    if (canMove) {
                        islandImage = new ImageView("/graphics/pieces/islands/island1.png");
                    } else {
                        islandImage = new ImageView("/graphics/pieces/islands/island1_bw.png");
                    }
                    break;
                case 1:
                    if (canMove) {
                        islandImage = new ImageView("/graphics/pieces/islands/island2.png");
                    } else {
                        islandImage = new ImageView("/graphics/pieces/islands/island2_bw.png");
                    }
                    break;
                case 2:
                    if (canMove) {
                        islandImage = new ImageView("/graphics/pieces/islands/island3.png");
                    } else {
                        islandImage = new ImageView("/graphics/pieces/islands/island3_bw.png");
                    }
                    break;
            }
            islandImage.setFitHeight(60);
            islandImage.setFitWidth(60);
            setShadow(islandImage);
            islandGrid.add(islandImage, pos.islandX(islands.indexOf(island)), pos.islandY(islands.indexOf(island)));

            islandImage.setOnMouseClicked((event) -> {
                distance = islands.indexOf(island) - motherNaturePos;
                while (distance <= 0) {
                    distance += 12;
                }
                if (distance > moves) {
                    notice.setText("ERROR: You don't have enough moves available!");
                } else {
                    this.guiView.getClientModel().setTypeOfRequest("MOTHER");
                    this.guiView.getClientModel().setChoosedMoves(distance);
                    this.guiView.getClientModel().setResponse(true);
                    this.guiView.getClientModel().setPingMessage(false);
                    Gson gson = new Gson();
                    Network.send(gson.toJson(this.guiView.getClientModel()));
                    this.guiView.closeWindow(event);
                }
            });
        });
    }
}
