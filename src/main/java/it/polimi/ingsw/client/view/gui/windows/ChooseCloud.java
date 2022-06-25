package it.polimi.ingsw.client.view.gui.windows;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.gui.GuiView;
import it.polimi.ingsw.client.view.gui.Game;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.GuiView.windowNode;

public class ChooseCloud implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Label notice;
    @FXML
    private GridPane cloudGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        windowNode = notice;
        this.notice.setText("");
        ArrayList<Cloud> clouds = this.guiView.getClientModel().getServermodel().getTable().getClouds();
        if (clouds.size() == 2) {
            cloudGrid.setLayoutX(150);
        }
        clouds.forEach(cloud -> {
            StackPane tile = new StackPane();
            ImageView cloudImage = new ImageView("/graphics/pieces/clouds/cloud_card.png");
            cloudImage.setFitHeight(130);
            cloudImage.setFitWidth(130);
            tile.getChildren().add(cloudImage);
            GridPane studentsCloudGrid = new GridPane();
            studentsCloudGrid.setAlignment(Pos.CENTER);
            studentsCloudGrid.setHgap(7);
            studentsCloudGrid.setVgap(7);

            Game game = new Game();
            game.populateGrid(studentsCloudGrid, 0, 2, cloud.getStudentsAccumulator());
            tile.getChildren().add(studentsCloudGrid);
            cloudGrid.add(tile, clouds.indexOf(cloud), 0);
            tile.setOnMouseClicked(event -> {
                if (cloud.getStudentsAccumulator().size() == 0) {
                    this.notice.setText("ERROR: Cloud already chosen!");
                } else {
                    this.guiView.getClientModel().setCloudChoosed(cloud);
                    this.guiView.getClientModel().setResponse(true); //lo flaggo come messaggio di risposta
                    this.guiView.getClientModel().setPingMessage(false);
                    Gson gson = new Gson();
                    Network.send(gson.toJson(this.guiView.getClientModel()));
                    this.guiView.closeWindow(event);
                }
            });
        });
    }
}
