package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.model.Cloud;
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
 * GUI View
 * @author Ignazio Neto Dell'Acqua
 * @author Lorenzo Morelli
 */
public class GuiView extends Application {
    private Stage stage;
    private Scene scene;
    private static ClientModel clientModel = new ClientModel();
    public static String gameState;

    public static String logText;
    public static Node currNode = null;
    public static boolean myTurn = false;
    public static boolean isCardUsed = false;
    public static CharacterCard currentCharacter = null;
    public static boolean canOpenWindow = true;
    public static Node windowNode = null;


    /**
     * This is the entry point for the GUI application
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.allowhidpi", "false");
        GuiView.clientModel = new ClientModel();
        gameState = "";
        UpdateLog updateLog = new UpdateLog();
        logText = updateLog.getTime() + "GAME STARTED!";
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.setTitle("Eriantys");
        this.stage.getIcons().add(new Image("/graphics/pieces/mother_nature.png"));
        this.stage.show();
    }

    /**
     * This method is used to switch between FXML files in order to change the scene
     *
     * @param newScene the name of the FXML file, without the extension.
     */
    public synchronized void changeScene(String newScene) throws IOException {
        System.out.println("cambio scena!");
        System.out.println("nuova scena: " + newScene);
        System.out.println("currNode: " + currNode);
        System.out.println("currScene: " + currNode.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + newScene + ".fxml"));
        this.stage = (Stage) currNode.getScene().getWindow();
        this.scene = new Scene(loader.load());
        this.stage.setScene(scene);
        this.stage.setMaximized(newScene.equals("Game") || newScene.equals("TryToReconnect"));
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
        stage.setAlwaysOnTop(true);
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

    public synchronized void closeWindow() {
        final Stage stage = (Stage) windowNode.getScene().getWindow();
        canOpenWindow = true;
        stage.close();
    }

    public synchronized ClientModel getClientModel() {
        return clientModel;
    }

    public synchronized void setClientModel(ClientModel clientModel) {
        GuiView.clientModel = clientModel;
    }

    /**
     * This method is called whenever it's this player's turn
     */
    public synchronized void requestToMe() throws IOException {
        myTurn = true;
        Network.setClientModel(clientModel);
        switch (clientModel.getTypeOfRequest()) {
            case "TRYTORECONNECT":
                changeScene("TryToReconnect");
                break;
            case "DISCONNECTION":
                changeScene("Disconnection");
                break;
            case "CHOOSEASSISTANTCARD":
                gameState = "ASSISTANT CARD PHASE";
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                System.out.println("carico gli studenti");
                gameState = "MOVING STUDENTS";
                changeScene("Game");
                break;
            case "TEAMMATE":
                gameState = "TEAM MATE";
                changeScene("TeamMate");
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                gameState = "MOVING MOTHER NATURE";
                changeScene("Game");
                break;
            case "CHOOSECLOUDS":
                gameState = "CHOOSING CLOUD";
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
        if (windowNode != null) {
            closeWindow();
        }
        Network.setClientModel(GuiView.clientModel);
        switch (clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                gameState = "ASSISTANT CARD PHASE";
                changeScene("Game");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                gameState = "MOVING STUDENT";
                changeScene("Game");
                break;
            case "TEAMMATE":
                gameState = "TEAM MATE";
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                gameState = "MOVING MOTHER NATURE";
                changeScene("Game");
                break;
            case "CHOOSECLOUDS":
                gameState = "CHOOSE CLOUD";
                changeScene("Game");
                break;
            case "GAMEEND":
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
        Network.setClientModel(GuiView.clientModel);
        Gson gson = new Gson();
        GuiView.clientModel.setPingMessage(true);
        GuiView.clientModel.setReply(true);
        Network.send(gson.toJson(GuiView.clientModel));
    }

    public synchronized void response() {
        UpdateLog updateLog = new UpdateLog();
        String head = updateLog.getTime();
        String tail = "\n" + logText;
        switch (GuiView.clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                logText = head + "The player " + GuiView.clientModel.getNickname() + " chose a card with value = " + (int) GuiView.clientModel.getCardChosenValue() + tail;
                break;
            case "TEAMMATE":
                logText = head + "The player " + GuiView.clientModel.getNickname() + " created the teams:\n" +
                        "           TEAM 1: " + GuiView.clientModel.getNicknames().get(3) + " " + GuiView.clientModel.getNicknames().get(2) + "\n" +
                        "           TEAM 2: " + GuiView.clientModel.getNicknames().get(1) + " " + GuiView.clientModel.getNicknames().get(0) + tail;
                break;
            case "SCHOOL":
                logText = head + "The player " + clientModel.getNickname() + " decided to move one " +
                        clientModel.getChosenColor().toString() + " student on their school" + tail;
                break;
            case "ISLAND":
                logText = head + "The player " + clientModel.getNickname() + " decided to move one " +
                        clientModel.getChosenColor().toString() + " on the island number " + (clientModel.getChosenIsland() + 1) + tail;
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                logText = head + "The player " + clientModel.getNickname() + " decided to move mother nature of " + clientModel.getChosenMoves() + " moves" + tail;
                break;
            case "CHOOSECLOUDS":
                logText = head + "The player " + clientModel.getNickname() + " chose to move students from the cloud with students: " + printCloud(clientModel.getCloudChosen()) + tail;
                break;
            case "MONK":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card MONK, choosing the color: " + clientModel.getChosenColor() + " and the island: " + clientModel.getChosenIsland() + tail;
                break;
            case "HERALD":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card HERALD, choosing the island: " + clientModel.getChosenIsland() + tail;
                break;
            case "PRINCESS":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card PRINCESS, choosing the color: " + clientModel.getChosenColor() + tail;
                break;
            case "THIEF":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card THIEF, choosing the color: " + clientModel.getChosenColor() + tail;
                break;
            case "MUSHROOMHUNTER":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card MUSHROOM HUNTER, choosing the color: " + clientModel.getChosenColor() + tail;
                break;
            case "KNIGHT":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card KNIGHT" + tail;
                break;
            case "CENTAUR":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card CENTAUR" + tail;
                break;
            case "FARMER":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card FARMER" + tail;
                break;
            case "POSTMAN":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card POSTMAN" + tail;
                break;
            case "GRANNY":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card GRANNY, choosing the island: " + clientModel.getChosenIsland() + tail;
                break;
            case "JESTER":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card JESTER, choosing to switch the colors: " + clientModel.getColors1() +
                        " from their Entrance with the colors: " + clientModel.getColors2() + " from this card" + tail;
                break;
            case "MINSTRELL":
                logText = head + "The player " + clientModel.getNickname() + " chose to use the character card MINSTRELL, choosing to switch the colors: " + clientModel.getColors1() +
                        " from their Entrance with the colors: " + clientModel.getColors2() + " from their Dinner Table" + tail;
                break;
        }

    }

    public String printCloud(Cloud cloud) {
        String students = "";
        students = students + "Blue: " + cloud.getStudentsAccumulator().getNumOfBlueStudents() + " ";
        students = students + "Red: " + cloud.getStudentsAccumulator().getNumOfRedStudents() + " ";
        students = students + "Green: " + cloud.getStudentsAccumulator().getNumOfGreenStudents() + " ";
        students = students + "Pink: " + cloud.getStudentsAccumulator().getNumOfPinkStudents() + " ";
        students = students + "Yellow: " + cloud.getStudentsAccumulator().getNumOfYellowStudents();
        return students;
    }
}


