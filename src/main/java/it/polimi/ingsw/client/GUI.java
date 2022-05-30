package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.State;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GUI extends Application{
    public Stage stage;
    public Scene scene;
    private static ClientModel clientModel = new ClientModel();
    private ClientModel networkClientModel;
    public static String gameState;
    public static String messageToOthers = "aa";

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

    public void changeScene(String newScene, Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + newScene + ".fxml"));
        this.stage = (Stage) node.getScene().getWindow();
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        if (newScene.equals("Game")) {
            this.stage.setMaximized(true);
            System.out.println("massimizzo");
        }
        this.stage.show();
    }

    public void openNewWindow(String newWindow) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/windows/" + newWindow + ".fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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


    public void requestToMe(Node node) throws InterruptedException, IOException {
        Network.setClientModel(networkClientModel);

        switch (networkClientModel.getTypeOfRequest()) {
            case "TRYTORECONNECT":
                System.out.println("boh");
                break;
            case "DISCONNECTION":
                System.out.println("boh");
                break;
            case "CHOOSEASSISTANTCARD":
                System.out.println("assistent!");
                gameState = "Assistant Card phase";
                changeScene("ChooseAssistantCard", node);
                break;

        }
    }

    public void requestToOthers() throws IOException {
        switch (networkClientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                messageToOthers = "L'utente " + networkClientModel.getNickname() + " sta scegliendo la carta assistente";
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                messageToOthers = "L'utente " + networkClientModel.getNickname() + " sta scegliendo dove muovere lo studente";
                break;
            case "TEAMMATE":
                messageToOthers = "L'utente " + networkClientModel.getNickname() + " sta scegliendo il suo compagno di squadra";
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                messageToOthers = "L'utente " + networkClientModel.getNickname() + " sta scegliendo il numero di mosse di cui far spostare madre natura";
                break;
            case "CHOOSECLOUDS":
                messageToOthers = "L'utente " + networkClientModel.getNickname() + " sta scegliendo la nuvola dalla quale ricaricare gli studenti";
                break;
        }
        if (!networkClientModel.getTypeOfRequest().equals("TEAMMATE") && networkClientModel.getServermodel()!=null) {
            //todo: what?
        }
    }

    public void response() throws IOException {

    }

    public void requestPing() {
        try {
            TimeUnit.SECONDS.sleep(1);
            Network.setClientModel(networkClientModel);
            Gson json = new Gson();
            networkClientModel.setPingMessage(true);
            Network.send(json.toJson(networkClientModel));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
