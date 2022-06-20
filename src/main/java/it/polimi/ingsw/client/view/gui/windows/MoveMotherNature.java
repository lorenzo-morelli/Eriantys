package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
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

public class MoveMotherNature implements Initializable {
    private final GUI gui = new GUI();
    private int distance;
    @FXML
    private GridPane islandGrid;
    @FXML
    private Label notice = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Position pos = new Position();
        notice.setText("");
        ArrayList<Island> islands = this.gui.getClientModel().getServermodel().getTable().getIslands();
        int motherNaturePos = this.gui.getClientModel().getServermodel().getTable().getMotherNaturePosition();
        int moves = this.gui.getClientModel().getServermodel().getcurrentPlayer().getChoosedCard().getMoves();
        distance = 0;
        islands.forEach(island -> {
            distance = islands.indexOf(island) - motherNaturePos;
            while (distance <= 0) {
                distance += 12;
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
            islandGrid.add(islandImage, pos.islandX(islands.indexOf(island)), pos.islandY(islands.indexOf(island)));

            islandImage.setOnMouseClicked((event) -> {
                distance = islands.indexOf(island) - motherNaturePos;
                while (distance <= 0) {
                    distance += 12;
                }
                if (distance > moves) {
                    notice.setText("ERROR: You don't have enough moves available!");
                } else {
                    this.gui.getClientModel().setTypeOfRequest("MOTHER");
                    this.gui.getClientModel().setChoosedMoves(distance);
                    this.gui.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
                    this.gui.getClientModel().setPingMessage(false);
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
