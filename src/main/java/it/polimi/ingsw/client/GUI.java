package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.utils.network.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Launcher for GUI View
 */
public class GUI extends Application {
    private Stage stage;
    private Scene scene;
    private static ClientModel clientModel = new ClientModel();
    public static String gameState;
    public static Node currNode = null;
    public static boolean myTurn = false;
    public static boolean isCardUsed = false;
    public static CharacterCard currentCharacter = null;
    public static boolean canOpenWindow = true;

    /**
     * This is the entry point for the GUI application
     */
    public static void main(String[] args) {
        launch(args);
        GUI.clientModel = new ClientModel();
    }

    @Override
    public void start(Stage stage) throws IOException {
        gameState = "";
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.getIcons().add(new Image("/graphics/pieces/mother_nature.png"));
        this.stage.show();
    }

    /**
     * This method is used to switch between FXML files in order to change the scene
     *
     * @param newScene the name of the FXML file, without the extension.
     */
    public synchronized void changeScene(String newScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + newScene + ".fxml"));
        if (currNode.getScene() != null) {
            this.stage = (Stage) currNode.getScene().getWindow();
        } else {
            this.stage = new Stage();
        }
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.setMaximized(newScene.equals("Game"));
        this.stage.show();
    }

    /**
     * As the previous method, this one also is used to switch from a FXML to another,
     * but instead of changing the current scene, it opens a new window with the selected scene.
     *
     * @param newWindow the name of the FXML file, without the extension.
     */
    public synchronized void openNewWindow(String newWindow) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/windows/" + newWindow + ".fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getIcons().add(new Image("/graphics/pieces/islands/island1.png"));
        stage.setOnCloseRequest(event -> canOpenWindow = true);
        stage.show();
    }

    /**
     * This method is used to close the current window.
     *
     * @param mouseEvent the event necessary to close the window.
     */
    public synchronized void closeWindow(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        canOpenWindow = true;
        stage.close();
    }

    public synchronized ClientModel getClientModel() {
        return clientModel;
    }

    public synchronized void setClientModel(ClientModel clientModel) {
        GUI.clientModel = clientModel;
    }

    /**
     * This method is called whenever it's this player's turn
     */
    public synchronized void requestToMe() throws IOException {
        myTurn = true;
        Network.setClientModel(GUI.clientModel);
        switch (GUI.clientModel.getTypeOfRequest()) {
            case "TRYTORECONNECT":
                changeScene("TryToReconnect");
                break;
            case "DISCONNECTION":
                changeScene("Disconnection");
                break;
            case "CHOOSEASSISTANTCARD":
                gameState = "Assistant Card phase";
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                System.out.println("carico gli studenti");
                gameState = "Moving students";
                changeScene("Game");
                break;
            case "TEAMMATE":
                gameState = "Team mate";
                changeScene("TeamMate");
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                gameState = "Moving mother nature";
                changeScene("Game");
                break;
            case "CHOOSECLOUDS":
                gameState = "Choosing cloud";
                changeScene("Game");
                break;
            case "GAMEEND":
                changeScene("Game");
                break;
        }
    }

    /**
     * This method is called whenever it's someone else's turn
     */
    public synchronized void requestToOthers() throws IOException {
        myTurn = false;
        isCardUsed = false;
        Network.setClientModel(GUI.clientModel);
        switch (GUI.clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                gameState = "Assistant Card phase";
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                gameState = "Moving students";
                changeScene("Game");
                break;
            case "TEAMMATE":
                gameState = "Team mate";
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                gameState = "Moving mother nature";
                changeScene("Game");
                break;
            case "CHOOSECLOUDS":
                changeScene("Game");
                break;
        }
        notifyAll();
    }

    /**
     * This method, which runs on another thread, accepts and responds to the pings sent from the server
     */
    public synchronized void requestPing() {
        System.out.println(("risposta ping"));
        ClientModel model = new ClientModel();
        model.setClientIdentity(GUI.clientModel.getClientIdentity());
        Network.setClientModel(model);
        Gson gson = new Gson();
        model.setPingMessage(true);
        model.setReply(false);
        Network.send(gson.toJson(model));
    }
}


