package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.model.characters.CharacterCard;
import it.polimi.ingsw.utils.network.Network;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GUI extends Application {
    public Stage stage;
    public Scene scene;
    private static ClientModel clientModel = new ClientModel();
    public static String gameState;
    public static String messageToOthers = "aa";
    public static Node currNode = null;
    public static boolean myTurn = false;
    public static CharacterCard currentCharacter = null;
    @FXML
    Label label=new Label();

    public static void main(String[] args) {
        launch(args);
        GUI.clientModel = new ClientModel();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * This method is used to switch between FXML files in order to change the scene
     * @param newScene the name of the FXML file, without the extension.
     */
    public synchronized void changeScene(String newScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + newScene + ".fxml"));
        if(currNode.getScene() !=null){
            this.stage = (Stage) currNode.getScene().getWindow();
        }
        else{
            this.stage= new Stage();
        }
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.setMaximized(newScene.equals("Game"));
        this.stage.show();
    }

    /**
     * As the previous method, this one also is used to switch from a FXML to another,
     * but instead of changing the current scene, it opens a new window with the selected scene.
     * @param newWindow the name of the FXML file, without the extension.
     */
    public synchronized void openNewWindow(String newWindow) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/windows/" + newWindow + ".fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This method is used to close the current window.
     * @param mouseEvent the event necessary to close the window.
     */
    public synchronized void closeWindow(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public synchronized void setClientModel(ClientModel clientModel) {
        GUI.clientModel = clientModel;
    }

    /**
     * This method is called whenever it's this player's turn
     */
    public synchronized void requestToMe() throws InterruptedException, IOException {
        myTurn = true;
        Network.setClientModel(GUI.clientModel);
        switch (GUI.clientModel.getTypeOfRequest()) {
            case "TRYTORECONNECT":
                label.setText("Numero minimo di giocatori non disponibile... attendo");
                changeScene("EndGame");
                break;
            case "DISCONNECTION":
                label.setText("Partita terminata per disconnessione");
                changeScene("EndGame");
                break;
            case "CHOOSEASSISTANTCARD":
                System.out.println("assistent!");
                gameState = "Assistant Card phase";
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                System.out.println("student");
                gameState = "Moving students";
                changeScene("Game");
                break;
            case "TEAMMATE":
                //todo
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
                if(GUI.clientModel.getServermodel().getNumberOfPlayers()==4){
                    label.setText("Partita terminata: i vincitori sono:" + GUI.clientModel.getGameWinner());
                }
                else {
                    label.setText("Partita terminata: il vincitore è:" + GUI.clientModel.getGameWinner());
                }
                changeScene("EndGame");
                break;

        }
    }

    public synchronized void requestToOthers() throws IOException {
        myTurn = false;
        Network.setClientModel(GUI.clientModel);
        switch (GUI.clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                messageToOthers = "L'utente " + GUI.clientModel.getNickname() + " sta scegliendo la carta assistente";
                System.out.println("wait choose assistant card");
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                messageToOthers = "L'utente " + GUI.clientModel.getNickname() + " sta scegliendo dove muovere lo studente";
                System.out.println("wait choose where to move students");
                changeScene("Game");
                break;
            case "TEAMMATE":
                messageToOthers = "L'utente " + GUI.clientModel.getNickname() + " sta scegliendo il suo compagno di squadra";
                System.out.println("wait team mate");
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                messageToOthers = "L'utente " + GUI.clientModel.getNickname() + " sta scegliendo il numero di mosse di cui far spostare madre natura";
                System.out.println("wait choose where to move mother");
                changeScene("Game");
                break;
            case "CHOOSECLOUDS":
                messageToOthers = "L'utente " + GUI.clientModel.getNickname() + " sta scegliendo la nuvola dalla quale ricaricare gli studenti";
                System.out.println("wait choose clouds");
                changeScene("Game");
                break;
        }
        if (!GUI.clientModel.getTypeOfRequest().equals("TEAMMATE") && GUI.clientModel.getServermodel() != null) {
            //todo: what?  -> just ignore
        }
        notifyAll();
    }

    public synchronized void requestPing() {
        try {
            System.out.println(("risposta ping"));
            TimeUnit.SECONDS.sleep(1);
            Network.setClientModel(GUI.clientModel);
            Gson gson = new Gson();
            GUI.clientModel.setPingMessage(true);
            Network.send(gson.toJson(GUI.clientModel));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


