package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {
    Stage stage;
    Scene scene;
    private static ClientModel clientModel = new ClientModel();
    private ChangeListener<? super Number> widthChangeListener;
    private ChangeListener<? super Number> heightChangeListener;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameOld.fxml"));
            this.scene = new Scene(loader.load());
            this.stage.setScene(scene);
//            widthChangeListener = (observable, oldValue, newValue) -> {
//                stage.heightProperty().removeListener(heightChangeListener);
//                stage.setHeight(newValue.doubleValue() / 2.0);
//                stage.heightProperty().addListener(heightChangeListener);
//            };
//            heightChangeListener = (observable, oldValue, newValue) -> {
//                stage.widthProperty().removeListener(widthChangeListener);
//                stage.setWidth(newValue.doubleValue() * 2.0);
//                stage.widthProperty().addListener(widthChangeListener);
//            };
//
//            stage.widthProperty().addListener(widthChangeListener);
//            stage.heightProperty().addListener(heightChangeListener);
//            this.stage.setMaximized(true);
//            this.stage.minWidthProperty().bind(scene.heightProperty().multiply(2));
//            this.stage.minHeightProperty().bind(scene.widthProperty().divide(2));
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
        if (newScene.equals("Game")) {
            this.stage.setMaximized(true);
        }
        this.stage.show();
    }

    public void openNewWindow(String newWindow) throws IOException {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/windows/" + newWindow + ".fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
