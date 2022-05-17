package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.State;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {
    Stage stage;
    Scene scene;
    private static ClientModel clientModel = new ClientModel();
    private ClientController clientController;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            this.scene = new Scene(loader.load());
            this.stage.setScene(scene);
            this.stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene(String newScene, MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + newScene + ".fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void setClientModel(ClientModel clientModel) {
        GUI.clientModel = clientModel;
    }
}
