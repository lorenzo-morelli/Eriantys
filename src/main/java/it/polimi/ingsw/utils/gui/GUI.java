package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
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

public class GUI extends Application implements View {
    Stage stage;
    Scene scene;
    public State callingState;
    private ClientModel clientModel;
    private Gson json;

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

    @Override
    public void setCallingState(State callingState) {
        this.callingState = callingState;
    }

    @Override
    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public void askToStart() {

    }

    @Override
    public void askDecision(String option1, String option2) {

    }

    @Override
    public void askParameters() {

    }

    @Override
    public void requestToMe() throws InterruptedException {

    }

    @Override
    public void requestToOthers() throws IOException {

    }

    @Override
    public void response() throws IOException {

    }
}
