package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.cliController.states.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.enums.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.concurrent.TimeUnit;
import java.util.regex.*;
import java.util.*;

import static it.polimi.ingsw.client.view.gui.common.Check.*;

/**
 * This class implements the methods needed to view information and interact with the client's command line interface
 * The information relating to the state that called the view is stored, in such a way as to make the interaction with the cli
 * (for example, if the client fails to enter a data, the view will print an additional warning message on the screen)
 */

public class CliView implements View {
    private State callingState;
    private ArrayList<String> parsedStrings;

    private ClientModel networkClientModel;
    private String nickname;
    private String response;
    private boolean cardJustUsed;

    public CliView() {
        response = "\n";
        cardJustUsed = false;
        networkClientModel = new ClientModel();
    }

    public synchronized void setClientModel(ClientModel clientModel) {
        this.networkClientModel = clientModel;
    }

    /**
     * A state that wants to call a view method is first registered by calling this method
     * for example I am in the WelcomeScreen state, and I do "view.setCallingState(this)"
     * It is nothing but the Observer pattern re-adapted for the State pattern
     *
     * @param callingState State of the controller that updated the view
     */
    @Override
    public synchronized void setCallingState(State callingState) {
        this.callingState = callingState;
    }

    /**
     * Ask the client to write the word "start" (in reality this interaction is no longer used,
     * but it was present in the first versions of the game, it is more a case study
     * on how to use this type of "framework").
     *
     * @throws InterruptedException I/O errors
     */
    @Override
    public void askToStart() throws InterruptedException {
        if (callingState instanceof WelcomeScreen) {
            ((WelcomeScreen) callingState).start().enable();
            ((WelcomeScreen) callingState).notStart().enable();

            CommandPrompt.ask(
                    "Write start to start the game and press ENTER",
                    "START THE GAME: ");


            ((WelcomeScreen) callingState).start().disable();
            ((WelcomeScreen) callingState).notStart().disable();
        }
    }

    /**
     * Ask the client to make a choice between two options, the method is deliberately
     * parametric and as general as possible to increase modularity and reuse of the code
     *
     * @param option1 String that the client could choose
     * @param option2 String that the client could choose
     * @throws InterruptedException I/0 errors
     */
    @Override
    public void askDecision(String option1, String option2) throws InterruptedException {
        if (callingState instanceof Decision) {
            ((Decision) callingState).heChoose1().enable();
            ((Decision) callingState).heChoose2().enable();

            CommandPrompt.ask(
                    "Choose between " + option1 + " and " + option2,
                    option1 + " or " + option2 + ": ");

            ((Decision) callingState).heChoose1().disable();
            ((Decision) callingState).heChoose2().disable();
        }
    }

    /**
     * Method of requesting the user to enter a certain number of words separated by a space,
     * the calling state will be a Read state (read from the terminal), the state only takes care of declaring
     * the number of words to read and then retrieve the information by placing it in the ClientModel object which
     * will then be sent to the server.
     * It is also possible to implement client side checks for the correctness of the parameters entered, for example if the
     * IP address is valid or not.
     */
    @Override
    public void askParameters() throws InterruptedException {
        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().enable();
        ((ReadFromTerminal) callingState).insertedParameters().enable();

        switch (((ReadFromTerminal) callingState).getType()) {
            case "USERINFO":
                CommandPrompt.ask("Enter Nickname Ip and port separated by a space and press ENTER [empty String: default setup in localhost]",
                        "Nickname Ip Port:");
                parsedStrings =
                        new ArrayList<>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
                if (!CommandPrompt.gotFromTerminal().equals("")) {
                    if (parsedStrings.size() != 3 || isValidIp(parsedStrings.get(1)) || isValidPort(parsedStrings.get(2))) {
                        System.out.print("ALERT: invalid entered data, try again\n");
                        askParameters();
                    }
                    setNickname(parsedStrings.get(0));
                } else {
                    setNickname("Default");
                }
                break;

            case "GAMEINFO":
                CommandPrompt.ask("Enter number of players followed by game mode (2/3/4 BEGINNER/EXPERT) [empty String: default 4 EXPERT]",
                        "NumOfPlayers GameMode:");
                parsedStrings =
                        new ArrayList<>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
                if (!parsedStrings.get(0).equals("")) {
                    if (!isValidCifra(parsedStrings.get(0))) {
                        System.out.print("ALERT: Enter a digit on the number of players, try again (e.g. 2 BEGINNER)\n");
                        askParameters();
                        return;
                    }
                } else {
                    return;
                }
                if (Integer.parseInt(parsedStrings.get(0)) < 2 || Integer.parseInt(parsedStrings.get(0)) > 4) {
                    System.out.print("ALERT: numOfPlayers must be between 2 and 4, try again (e.g. 2 BEGINNER)\n");
                    askParameters();
                    return;
                }
                if (!parsedStrings.get(1).equals("BEGINNER") && !parsedStrings.get(1).equals("EXPERT")) {
                    System.out.print("ALERT: the chosen gameMode must be 'BEGINNER' or 'EXPERT', try again (e.g. 2 BEGINNER)\n");
                    askParameters();
                    return;
                }
                break;

            case "NICKNAMEEXISTENT":
                CommandPrompt.ask("ALERT: Nickname chosen already existing, please re-enter a new one",
                        "Nickname:");
                setNickname(CommandPrompt.gotFromTerminal());
                break;
        }

        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().disable();
        ((ReadFromTerminal) callingState).insertedParameters().disable();
    }

    /**
     * Method to send ping messages requests, use the sleep method
     * to make sure not to clog the network interface and gson to serialize the message
     */
    public synchronized void requestPing() {
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

    /**
     * The requestToMe method is used to specify all the reactions of the client in case
     * of a specific request (the one who sent the message, the server, has filled in an
     * identification field as the recipient of the message, and this recipient must be
     * able to perform an action , i.e. ask the user for information / show information on the screen).
     * <br>
     * A switch case is used to parameterize the different usage scenarios
     * (based on the type of request, another field present in the message)
     *
     * @throws InterruptedException I/0 errors
     */
    public void requestToMe() throws InterruptedException {
        Network.setClientModel(networkClientModel);

        switch (networkClientModel.getTypeOfRequest()) {
            case "TRYTORECONNECT":
                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n" + " THE SERVER IS ATTEMPTING TO RECONNECT A PLAYER TO A MINIMUM TO ALLOW THE GAME TO CONTINUE" + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));
                break;
            case "DISCONNECTION":
                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n" + " THE GAME IS OVER DUE TO DISCONNECTION, NO PLAYER HAS RECONNECTED..." + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));
                break;
            case "TEAMMATE":
                for (String nickname : networkClientModel.getNicknames()) {
                    System.out.println(nickname);
                }
                CommandPrompt.ask("Enter your teammate’s nickname: ", "Nickname: ");

                boolean ResultOfChoosingTeamMate= chosenTeamMateManagement();
                if(ResultOfChoosingTeamMate){
                    TimeUnit.SECONDS.sleep(1);
                    requestToMe();
                    return;
                }
                break;
            case "CHOOSEASSISTANTCARD":
                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n" + "IT’S YOUR TURN - ASSISTANT CARD PHASE" + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));
                System.out.println("Choose a Assistant Card");
                for (AssistantCard a : networkClientModel.getDeck()) {
                    System.out.println("Value: " + (int) a.getValues() + "  Moves: " + a.getMoves());
                }
                CommandPrompt.ask("Enter the value of the chosen card", "Card: ");
                boolean ResultOfChoosingAssistantCard= chosenAssistantCardManagement();
                if(ResultOfChoosingAssistantCard){
                    TimeUnit.SECONDS.sleep(1);
                    requestToMe();
                    return;
                }
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":

                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n" + "IT’S YOUR TURN - STUDENT PHASE" + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));

                if (networkClientModel.getServerModel().getGameMode().equals(GameMode.EXPERT) && networkClientModel.getServerModel().getTable().getCharacters().stream().anyMatch(j -> j.getCost() <= networkClientModel.getServerModel().getCurrentPlayer().getCoins() && !cardJustUsed)) {

                    CommandPrompt.ask("Choose the color of the student you want to move OR insert CARD to use a character card", "RED or GREEN or BLUE or YELLOW or PINK or CARD: ");

                    PeopleColor chosenColor= chosenColorManagement();

                    if(chosenColor==null && !CommandPrompt.gotFromTerminal().equals("CARD")){
                        System.out.println("ALERT: You have not entered either an valid color or the CARD command, please try again");
                        TimeUnit.SECONDS.sleep(1);
                        requestToMe();
                        return;
                    }

                    if(chosenColor==null) {
                        boolean ResultOfCardUsage = chosenCharacterCardManagement();
                        if (ResultOfCardUsage) {
                            TimeUnit.SECONDS.sleep(1);
                            requestToMe();
                            return;
                        }
                    }

                    if (!CommandPrompt.gotFromTerminal().equals("CARD")) {
                        boolean ResultOfSchoolOrIsland =schoolOrIslandChooseManagement(chosenColor);
                        if (ResultOfSchoolOrIsland) {
                            TimeUnit.SECONDS.sleep(1);
                            requestToMe();
                            return;
                        }
                    }
                }
                else {

                    CommandPrompt.ask("Choose the color of the student you want to move ", "RED or GREEN or BLUE or YELLOW or PINK: ");

                    PeopleColor chosenColor= chosenColorManagement();

                    if(chosenColor==null){
                        System.out.println("ALERT: You have not entered a valid color, please try again");
                        TimeUnit.SECONDS.sleep(1);
                        requestToMe();
                        return;
                    }

                    boolean ResultOfSchoolOrIsland =schoolOrIslandChooseManagement(chosenColor);
                    if (ResultOfSchoolOrIsland) {
                        TimeUnit.SECONDS.sleep(1);
                        requestToMe();
                        return;
                    }
                    break;
                }
                break;
            case "CHOOSEWHERETOMOVEMOTHER":

                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n " + "IT’S YOUR TURN - MOTHER PHASE" + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));

                if (networkClientModel.getServerModel().getGameMode().equals(GameMode.EXPERT) && networkClientModel.getServerModel().getTable().getCharacters().stream().anyMatch(j -> j.getCost() <= networkClientModel.getServerModel().getCurrentPlayer().getCoins() && !cardJustUsed)) {

                    CommandPrompt.ask("Choose the number of moves to move Mother Nature OR enter CARD to use a character card", "Moves: ");
                    if (CommandPrompt.gotFromTerminal().equals("CARD")) {
                        boolean resultCardUsage= chosenCharacterCardManagement();
                        if(resultCardUsage){
                            TimeUnit.SECONDS.sleep(1);
                            requestToMe();
                            return;
                        }
                    }else {

                       boolean ResultOfMovingMother= chosenMotherMovementManagement();
                       if(ResultOfMovingMother){
                           TimeUnit.SECONDS.sleep(1);
                           requestToMe();
                           return;
                       }
                    }
                } else {

                    CommandPrompt.ask("Choose the number of moves to move Mother Nature ", "Moves: ");
                    boolean ResultOfMovingMother= chosenMotherMovementManagement();
                    if(ResultOfMovingMother){
                        TimeUnit.SECONDS.sleep(1);
                        requestToMe();
                        return;
                    }
                }
                break;
            case "CHOOSECLOUDS":
                System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: \n" + "IT’S YOUR TURN - CLOUD PHASE" + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));
                CommandPrompt.ask("Choose the number of the cloud card from which you want to recharge students", "Cloud: ");

                boolean ResultOfChoosingCloud= chosenCloudManagement();
                if(ResultOfChoosingCloud){
                    TimeUnit.SECONDS.sleep(1);
                    requestToMe();
                    return;
                }
                cardJustUsed = false;
                break;
            case "GAMEEND":
                if (networkClientModel.getGameWinner().equals("DRAW")) {
                    System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: THE GAME ENDED IN A TIE, THE PLAYERS ARE ALL WINNERS!!"));
                } else if (networkClientModel.getServerModel().getNumberOfPlayers() < 4) {
                    System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: " + "THE GAME IS OVER! WINNER IS" + networkClientModel.getGameWinner()));
                } else {
                    System.out.println(networkClientModel.getServerModel().toString(networkClientModel.getNickname(), "GAME STATE: " + "THE GAME IS OVER! THE WINNERS ARE " + networkClientModel.getGameWinner()));
                }
                Network.disconnect();
                break;

        }

        clearResponse();
    }

    private boolean chosenTeamMateManagement(){
        Gson json;
        if (!networkClientModel.getNicknames().contains(CommandPrompt.gotFromTerminal())) {
            System.out.print("ALERT: The entered nickname does not exist, please choose a nickname from those specified");
            return true;
        }
        String teamMate = CommandPrompt.gotFromTerminal();
        networkClientModel.getNicknames().remove(teamMate);
        networkClientModel.getNicknames().add(teamMate);
        networkClientModel.getNicknames().add(networkClientModel.getNickname());
        networkClientModel.setResponse(true);
        networkClientModel.setPingMessage(false);
        networkClientModel.setFromTerminal(parsedStrings);
        json = new Gson();
        Network.send(json.toJson(networkClientModel));
        return false;
    }
    private PeopleColor chosenColorManagement() {

        if (!CommandPrompt.gotFromTerminal().equals("RED") &&
                !CommandPrompt.gotFromTerminal().equals("GREEN") &&
                !CommandPrompt.gotFromTerminal().equals("BLUE") &&
                !CommandPrompt.gotFromTerminal().equals("YELLOW") &&
                !CommandPrompt.gotFromTerminal().equals("PINK") &&
                !CommandPrompt.gotFromTerminal().equals("CARD")) {
            return null;
        }

        PeopleColor chosenColor;
        String terminalInput = CommandPrompt.gotFromTerminal();
        switch (terminalInput) {
            case "RED":
                chosenColor = PeopleColor.RED;
                break;
            case "GREEN":
                chosenColor = PeopleColor.GREEN;
                break;
            case "BLUE":
                chosenColor = PeopleColor.BLUE;
                break;
            case "YELLOW":
                chosenColor = PeopleColor.YELLOW;
                break;
            case "PINK":
                chosenColor = PeopleColor.PINK;
                break;
            default:
                return null;
        }
        return chosenColor;
    }
    private void addCards(ArrayList<String> available) {
        System.out.println("AVAILABLE CARDS:");
        for (int i = 0; i < networkClientModel.getServerModel().getTable().getCharacters().size(); i++) {
            if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getCost() <= networkClientModel.getServerModel().getCurrentPlayer().getCoins()) {
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MONK")) {
                    System.out.println("MONK - EFFECT: Take a student from this card and place him on an island of your choice.\n" + " COMMAND: Enter MONK , chosen color and island number separated by a space.\n\n");
                    available.add("MONK");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("PRINCESS")) {
                    System.out.println("PRINCESS - EFFECT: Take a student from this card and place it in your Room.\n" + "COMMAND: Enter PRINCESS and chosen color separated by a space.\n\n");
                    available.add("PRINCESS");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MUSHROOMHUNTER")) {
                    System.out.println("MUSHROOMHUNTER - EFFECT: Choose a student color; in this turn, when calculating the influence, that color does not provide influence.\n" + " COMMAND: Enter MUSHROOMHUNTER and chosen color separated by a space.\n\n");
                    available.add("MUSHROOMHUNTER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("THIEF")) {
                    System.out.println("THIEF - EFFECT: Choose a student color; each player (including you) must return to the bag three students of that color present in the Room (or all those who have if he had less than three).\n" + "COMMAND: Enter THIEF and chosen color separated by a space\n\n");
                    available.add("THIEF");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("FARMER")) {
                    System.out.println("FARMER - EFFECT: During this turn, take control of the professors even if you have the same number of students in your room as the player who controls them at that time.\n" + "COMMAND: Enter FARMER.\n\n");
                    available.add("FARMER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("CENTAUR")) {
                    System.out.println("CENTAUR - EFFECT: When counting an influence of the island the towers present are not calculated.\n" + "COMMAND: Enter CENTAUR.\n\n");
                    available.add("CENTAUR");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("KNIGHT")) {
                    System.out.println("KNIGHT - EFFECT: In this turn, when calculating the influence, you have two additional points of influence.\n" + "COMMAND: Enter KNIGHT.\n\n");
                    available.add("KNIGHT");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("POSTMAN")) {
                    System.out.println("POSTMAN - EFFECT: You can move Mother Nature up to two additional islands from the assistant card you played.\n" + "COMMAND: Enter POSTMAN.\n\n");
                    available.add("POSTMAN");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("GRANNY")) {
                    System.out.println("GRANNY - EFFECT: Place a ban on an island of your choice. The first time Mother Nature finishes her movement the influence will not be calculated and the ban will be reinstated in this card.\n" + "COMMAND: Enter GRANNY and island number separated by a space.\n\n");
                    available.add("GRANNY");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("HERALD")) {
                    System.out.println("HERALD - EFFECT: Choose an island and calculate the majority as if mother nature had finished her movement there. In this turn mother nature will move, as usual, and in the island where her movement will end, the majority will normally be calculated.\nCOMMAND: Enter HERALD and island number separated by a space.\n\n");
                    available.add("HERALD");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("JESTER")) {
                    System.out.println("JESTER - EFFECT: You can take up to 3 students from this card and exchange them with as many students present in your Entry.\n" + "COMMAND: enter JESTER , below the colors to be exchanged from Input and then the colors to be exchanged from this Card, all separated by a space (the number of colors of one and the other must be the same).\n\n");
                    available.add("JESTER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MINSTRELL")) {
                    System.out.println("MINSTRELL - EFFECT: You can exchange up to two students in your Hall and Entrance.\n" + "COMMAND: Enter MINSTRELL , below the colors to be exchanged from Input and then the colors to be exchanged from Room all separated by a space (the number of colors of one and the other must be the same).\n\n");
                    available.add("MINSTRELL");
                }
            }
        }
    }
    private boolean chosenCharacterCardManagement() throws InterruptedException {
        Gson json;
        System.out.println("\n");
        ArrayList<String> available = new ArrayList<>();
        addCards(available);
        CommandPrompt.ask("Choose the character card by following the directions, any other line if you want to go back", "CHARACTER:");
        parsedStrings =
                new ArrayList<>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
        if (!available.contains(parsedStrings.get(0))) {
            System.out.println("GO BACK");
            return true;
        }
        CharacterCard modelCard = null;
        for (int i = 0; i < networkClientModel.getServerModel().getTable().getCharacters().size(); i++) {
            if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals(parsedStrings.get(0))) {
                modelCard = networkClientModel.getServerModel().getTable().getCharacters().get(i);
            }
        }
        switch (parsedStrings.get(0)) {
            case "MUSHROOMHUNTER":
            case "THIEF":
                if (!(parsedStrings.size() == 2) || !(Objects.equals(parsedStrings.get(1), "RED") || Objects.equals(parsedStrings.get(1), "PINK") || Objects.equals(parsedStrings.get(1), "GREEN") || Objects.equals(parsedStrings.get(1), "YELLOW") || Objects.equals(parsedStrings.get(1), "BLUE"))) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                PeopleColor color = null;
                switch (parsedStrings.get(1)) {
                    case "RED":
                        color = PeopleColor.RED;
                        break;
                    case "GREEN":
                        color = PeopleColor.GREEN;
                        break;
                    case "PINK":
                        color = PeopleColor.PINK;
                        break;
                    case "BLUE":
                        color = PeopleColor.BLUE;
                        break;
                    case "YELLOW":
                        color = PeopleColor.YELLOW;
                        break;
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                networkClientModel.setChosenColor(color);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
                break;
            case "MONK":
                if (!(parsedStrings.size() == 3) || !(Objects.equals(parsedStrings.get(1), "RED") || Objects.equals(parsedStrings.get(1), "PINK") || Objects.equals(parsedStrings.get(1), "GREEN") || Objects.equals(parsedStrings.get(1), "YELLOW") || Objects.equals(parsedStrings.get(1), "BLUE")) || (Integer.parseInt(parsedStrings.get(2)) < 1 || Integer.parseInt(parsedStrings.get(2)) > networkClientModel.getServerModel().getTable().getIslands().size())) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                color = null;
                switch (parsedStrings.get(1)) {
                    case "RED":
                        color = PeopleColor.RED;
                        break;
                    case "GREEN":
                        color = PeopleColor.GREEN;
                        break;
                    case "PINK":
                        color = PeopleColor.PINK;
                        break;
                    case "BLUE":
                        color = PeopleColor.BLUE;
                        break;
                    case "YELLOW":
                        color = PeopleColor.YELLOW;
                        break;
                }
                assert modelCard != null;
                if (modelCard.getName().equals("MONK")) {
                    assert color != null;
                    if (networkClientModel.getServerModel().getTable().getMonkSet().numStudentsByColor(color) == 0) {
                        System.out.println("ALERT: The card does not have the color you have chosen, chosen rejected card , please try again");
                        return true;
                    }
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                networkClientModel.setChosenColor(color);
                networkClientModel.setChosenIsland(Integer.parseInt(parsedStrings.get(2)) - 1);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
                break;
            case "PRINCESS":
                if (!(parsedStrings.size() == 2) || !(Objects.equals(parsedStrings.get(1), "RED") || Objects.equals(parsedStrings.get(1), "PINK") || Objects.equals(parsedStrings.get(1), "GREEN") || Objects.equals(parsedStrings.get(1), "YELLOW") || Objects.equals(parsedStrings.get(1), "BLUE"))) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                color = null;
                switch (parsedStrings.get(1)) {
                    case "RED":
                        color = PeopleColor.RED;
                        break;
                    case "GREEN":
                        color = PeopleColor.GREEN;
                        break;
                    case "PINK":
                        color = PeopleColor.PINK;
                        break;
                    case "BLUE":
                        color = PeopleColor.BLUE;
                        break;
                    case "YELLOW":
                        color = PeopleColor.YELLOW;
                        break;
                }
                assert modelCard != null;
                if (modelCard.getName().equals("PRINCESS")) {
                    assert color != null;
                    if (networkClientModel.getServerModel().getTable().getPrincessSet().numStudentsByColor(color) == 0) {
                        System.out.println("ALERT: The card does not have the color you have chosen, chosen rejected card , please try again");
                        return true;
                    }
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                networkClientModel.setChosenColor(color);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
                break;
            case "FARMER":
            case "CENTAUR":
            case "KNIGHT":
            case "POSTMAN":
                if (parsedStrings.size() != 1) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
                break;
            case "GRANNY":
            case "HERALD":
                if (!(parsedStrings.size() == 2) || (Integer.parseInt(parsedStrings.get(1)) < 1 || Integer.parseInt(parsedStrings.get(1)) > networkClientModel.getServerModel().getTable().getIslands().size())) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                assert modelCard != null;
                if (modelCard.getName().equals("GRANNY") && networkClientModel.getServerModel().getTable().getIslands().get(Integer.parseInt(parsedStrings.get(1)) - 1).isBlocked()) {
                    System.out.println("ALERT: already blocked island, rejected card choice , try again");
                    return true;
                }
                if (modelCard.getName().equals("GRANNY") && networkClientModel.getServerModel().getTable().getNumDivieti() == 0) {
                    System.out.println("ALERT: there are no positionable bans, rejected card choice, try again");
                    return true;
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                networkClientModel.setChosenIsland(Integer.parseInt(parsedStrings.get(1)) - 1);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
                break;
            case "MINSTRELL":
            case "JESTER":
                int max = 2;
                StudentSet destination = networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getDinnerTable();
                ArrayList<PeopleColor> colors1 = new ArrayList<>();
                ArrayList<PeopleColor> colors2 = new ArrayList<>();
                if (parsedStrings.get(0).equals("JESTER")) {
                    max = 3;
                    destination = networkClientModel.getServerModel().getTable().getJesterSet();
                }
                if (parsedStrings.size() < 3 || parsedStrings.size() > (1 + 2 * max) || parsedStrings.size() % 2 == 0) {
                    System.out.println("ALERT: invalid size, rejected card choice, try again");
                    return true;
                }
                for (int i = 1; i < parsedStrings.size(); i++) {
                    switch (parsedStrings.get(i)) {
                        case "RED":
                            if (i <= (parsedStrings.size() - 1) / 2) {
                                colors1.add(PeopleColor.RED);
                            } else {
                                colors2.add(PeopleColor.RED);
                            }
                            break;
                        case "GREEN":
                            if (i <= (parsedStrings.size() - 1) / 2) {
                                colors1.add(PeopleColor.GREEN);
                            } else {
                                colors2.add(PeopleColor.GREEN);
                            }
                            break;
                        case "BLUE":
                            if (i <= (parsedStrings.size() - 1) / 2) {
                                colors1.add(PeopleColor.BLUE);
                            } else {
                                colors2.add(PeopleColor.BLUE);
                            }
                            break;
                        case "YELLOW":
                            if (i <= (parsedStrings.size() - 1) / 2) {
                                colors1.add(PeopleColor.YELLOW);
                            } else {
                                colors2.add(PeopleColor.YELLOW);
                            }
                            break;
                        case "PINK":
                            if (i <= (parsedStrings.size() - 1) / 2) {
                                colors1.add(PeopleColor.PINK);
                            } else {
                                colors2.add(PeopleColor.PINK);
                            }
                            break;
                    }
                }
                if (networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getEntranceSpace().contains(colors1) || destination.contains(colors2)) {
                    System.out.println("ALERT: one or more chosen colors are not present, rejected card choice, try again");
                    return true;
                }
                networkClientModel.setTypeOfRequest(parsedStrings.get(0));
                networkClientModel.setResponse(true);
                networkClientModel.setPingMessage(false);
                networkClientModel.setColors1(colors1);
                networkClientModel.setColors2(colors2);
                json = new Gson();
                cardJustUsed = true;
                Network.send(json.toJson(networkClientModel));
        }
        return false;
    }
    private boolean chosenAssistantCardManagement(){
        Gson json;
        if (isValidNumber(CommandPrompt.gotFromTerminal())) {
            System.out.println("ALERT: The card you choose has an invalid value, please try again");
            return true;
        }
        boolean check = false;
        for (int i = 0; i < networkClientModel.getDeck().size(); i++) {
            if (networkClientModel.getDeck().get(i).getValues() == Integer.parseInt((CommandPrompt.gotFromTerminal()))) {
                check = true;
            }
        }
        if (!check) {
            System.out.println("ALERT: The chosen card does not exist, try again");
            return true;
        }
        networkClientModel.setCardChosenValue(Float.parseFloat(CommandPrompt.gotFromTerminal()));
        networkClientModel.setResponse(true);
        networkClientModel.setPingMessage(false);
        networkClientModel.setFromTerminal(parsedStrings);
        json = new Gson();
        Network.send(json.toJson(networkClientModel));
        return false;
    }
    private boolean schoolOrIslandChooseManagement(PeopleColor chosenColor) throws InterruptedException {

        Gson json;
        if (networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getEntranceSpace().numStudentsByColor(chosenColor) == 0) {
            System.out.println("ALERT: You have entered a color not present among those available, try again");
            return true;
        }

        CommandPrompt.ask("Choose SCHOOL if you want to move a student from your entrance space to the dining room, \notherwise write ISLAND if you want to move a student on an island. Enter any other command line if you want to go back", "SCHOOL or ISLAND: ");

        String command = CommandPrompt.gotFromTerminal();
        if (!command.equals("SCHOOL") && !command.equals("ISLAND")) {
            System.out.println("GO BACK");
            return true;
        } else if (command.equals("SCHOOL")) {
            if (networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getDinnerTable().numStudentsByColor(chosenColor) == 10) {
                System.out.println("ALERT: The dining room of that color is full.");
                return true;
            }
            networkClientModel.setTypeOfRequest("SCHOOL");
        } else {
            CommandPrompt.ask("Enter the number of the island on which you want to move the student", "Island ");
            if (isValidNumber(CommandPrompt.gotFromTerminal())) {
                System.out.println("ALERT: You entered an invalid number, try again");
                return true;
            }
            if (Integer.parseInt(CommandPrompt.gotFromTerminal()) > networkClientModel.getServerModel().getTable().getIslands().size() ||
                    Integer.parseInt(CommandPrompt.gotFromTerminal()) < 0) {
                System.out.println("ALERT: The chosen island is invalid.");
                return true;
            }
            networkClientModel.setTypeOfRequest("ISLAND");
            networkClientModel.setChosenIsland(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1);
        }
        networkClientModel.setTypeOfRequest(command);
        networkClientModel.setResponse(true);
        networkClientModel.setPingMessage(false);
        networkClientModel.setChosenColor(chosenColor);
        json = new Gson();
        Network.send(json.toJson(networkClientModel));
        return false;
    }
    private boolean chosenMotherMovementManagement() {
        Gson json;
        if (isValidNumber(CommandPrompt.gotFromTerminal())) {
            System.out.println("ALERT: The number of moves you entered is not a valid number, try again");
            return true;
        }
        if (Integer.parseInt((CommandPrompt.gotFromTerminal())) <= 0) {
            System.out.println("ALERT: The number of moves must be a positive integer, try again");
            return true;
        }
        if (networkClientModel.getServerModel().getCurrentPlayer().getChosenCard().getMoves() < Integer.parseInt(CommandPrompt.gotFromTerminal())) {
            System.out.println("ALERT: The number of moves you enter exceeds the maximum number of moves mother nature can move," +
                    " i.e. " + networkClientModel.getServerModel().getCurrentPlayer().getChosenCard().getMoves());
            return true;
        }
        networkClientModel.setTypeOfRequest("MOTHER");
        networkClientModel.setChosenMoves(Integer.parseInt(CommandPrompt.gotFromTerminal()));
        networkClientModel.setResponse(true);
        networkClientModel.setPingMessage(false);
        networkClientModel.setFromTerminal(parsedStrings);
        json = new Gson();
        Network.send(json.toJson(networkClientModel));
        return false;
    }
    private boolean chosenCloudManagement() {
        Gson json;

        if (isValidNumber(CommandPrompt.gotFromTerminal())) {
            System.out.println("ALERT: The number entered is not a valid number, try again");
            return true;
        }
        if (networkClientModel.getServerModel().getTable().getClouds().size() < Integer.parseInt(CommandPrompt.gotFromTerminal()) ||
                Integer.parseInt(CommandPrompt.gotFromTerminal()) < 1) {
            System.out.println("ALERT: The number you entered does not represent an existing cloud tile, please try again");
            return true;
        }
        if (networkClientModel.getServerModel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1).getStudentsAccumulator().size() == 0) {
            System.out.println("ALERT: You have chosen a cloud that has already been chosen by another player, try again");
            return true;
        }
        networkClientModel.setCloudChosen(networkClientModel.getServerModel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1));
        networkClientModel.setResponse(true);
        networkClientModel.setPingMessage(false);
        networkClientModel.setFromTerminal(parsedStrings);
        json = new Gson();
        Network.send(json.toJson(networkClientModel));
        return false;
    }

    /**
     * Intercepts a server request to another client, and on the basis
     * of this request performs an action
     * (for example prints "Pippo is choosing the assistant card")
     */
    public void requestToOthers() {
        String message = null;
        switch (networkClientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                message = "The player " + networkClientModel.getNickname() + " is choosing the assistant card";
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                message = "The player " + networkClientModel.getNickname() + " is choosing where to move the student";
                break;
            case "TEAMMATE":
                message = "The player " + networkClientModel.getNickname() + " is choosing his teammate";
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                message = "The player " + networkClientModel.getNickname() + " is choosing the number of moves to move Mother Nature";
                break;
            case "CHOOSECLOUDS":
                message = "The player " + networkClientModel.getNickname() + " is choosing the cloud from which to recharge the students";
                break;
        }
        System.out.println(message);
        if (!networkClientModel.getTypeOfRequest().equals("TEAMMATE") && networkClientModel.getServerModel() != null) {
            System.out.println(networkClientModel.getServerModel().toString(getNickname(), "GAME STATE: " + message + "\n\nMOVES BY OTHER PLAYERS: " + getResponse()));
        }
    }

    /**
     * Intercepts a response from a client to the server, and on the basis
     * of this request performs an action
     * (for example prints "Pippo has chosen the assistant card xyz")
     */
    public synchronized void response() {
        switch (networkClientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                setResponse("The player " + networkClientModel.getNickname() + " chose " +
                        "the card with value = " + networkClientModel.getCardChosenValue());
                break;
            case "TEAMMATE":
                setResponse("The player " + networkClientModel.getNickname() + " create the teams:\n" +
                        "TEAM 1: " + networkClientModel.getNicknames().get(3) + " " + networkClientModel.getNicknames().get(2) + "\n" +
                        "TEAM 2: " + networkClientModel.getNicknames().get(1) + " " + networkClientModel.getNicknames().get(0) + "\n");
                break;
            case "SCHOOL":
                setResponse("The player " + networkClientModel.getNickname() + " chose to move one color student " +
                        networkClientModel.getChosenColor().toString() + " inside his school");
                break;
            case "ISLAND":
                setResponse("The player " + networkClientModel.getNickname() + " chose to move one color student " +
                        networkClientModel.getChosenColor().toString() + " inside the island number " + (networkClientModel.getChosenIsland() + 1));
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                setResponse("The player " + networkClientModel.getNickname() + " chose to move mother nature by " + networkClientModel.getChosenMoves() + " moves");
                break;
            case "CHOOSECLOUDS":
                setResponse("The player " + networkClientModel.getNickname() + " chose to recharge students from the cloud: " + networkClientModel.getCloudChosen());
                break;
            case "MONK":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the MONK character card, choosing as color: " + networkClientModel.getChosenColor() + " and choosing as an island: " + networkClientModel.getChosenIsland() + " (EFFECT: Take a student from this card and place it on an island of your choice).");
                break;
            case "HERALD":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the HERALD character card, choosing as an island: " + networkClientModel.getChosenIsland() + " (EFFECT: Choose an island and calculate the majority as if Mother Nature had finished her movement there. In this turn Mother Nature will move 'as usual and in the island where she will end' her movement the majority will be normally calculated).");
                break;
            case "PRINCESS":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the PRINCESS character card, choosing as color: " + networkClientModel.getChosenColor() + " (EFFECT: Take a student from this card and place it in your Room).");
                break;
            case "THIEF":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the THIEF character card, choosing as color: " + networkClientModel.getChosenColor() + " (EFFECT: Choose a student color; each player (including you) must return to the bag three students of that color present in the Hall (or all those who have if he had less than three) ).");
                break;
            case "MUSHROOMHUNTER":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the MUSHROOMHUNTER character card, choosing as color: " + networkClientModel.getChosenColor() + " (EFFECT: Choose a student color; in this turn, when calculating the influence, that color does not provide influence).");
                break;
            case "KNIGHT":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the KNIGHT character card" + " (EFFECT: In this turn, when calculating the influence, you have two additional influence points).");
                break;
            case "CENTAUR":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the CENTAUR character card" + " (EFFECT: When counting an influence of the island the towers present are not calculated).");
                break;
            case "FARMER":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use FARMER character card" + " (EFFECT: During this turn, take control of the professors even if in your room you have the same number of students as the player who controls them at that time).");
                break;
            case "POSTMAN":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the POSTMAN character card" + " (EFFECT: You can move Mother Nature up to two additional islands than shown on the assistant card you played).");
                break;
            case "GRANNY":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the GRANNY character card, choosing as an island: " + networkClientModel.getChosenIsland() + " (EFFECT: Place a ban on an island of your choice. The first time Mother Nature finishes her movement there the influence will not be calculated and the ban will be reinstated in this card).");
                break;
            case "JESTER":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the JESTER character card, choosing to exchange colors: " + networkClientModel.getColors1() + " from his Entrance with colors: " + networkClientModel.getColors2() + " from this card" + " (EFFECT: You can take up to 3 students from this card and exchange them with as many students present in your Entry).");
                break;
            case "MINSTRELL":
                setResponse("The player " + networkClientModel.getNickname() + " chose to use the MINSTRELL character card, choosing to exchange colors: " + networkClientModel.getColors1() + " from his Entrance with colors: " + networkClientModel.getColors2() + " from his Dinner Table" + " (EFFECT: You can exchange up to two students in your Dinner Table and Entry).");
                break;
        }

    }

    /**
     * Method for client-side checking validity of decimal digit
     *
     * @param cifra A string
     * @return true if represent a decimal digit, false else
     */
    public static boolean isValidCifra(String cifra) {

        String CIFRA_REGEX = "\\d";

        Pattern CIFRA_PATTERN = Pattern.compile(CIFRA_REGEX);

        if (cifra == null) {
            return false;
        }

        Matcher matcher = CIFRA_PATTERN.matcher(cifra);

        return matcher.matches();
    }

    /**
     * Method for client-side checking validity of decimal numbers
     *
     * @param number A string
     * @return true if represent a decimal number, false else
     */
    public static boolean isValidNumber(String number) {
        String NUMERO_REGEX = "\\d+";
        Pattern NUMERO_PATTERN = Pattern.compile(NUMERO_REGEX);

        if (number == null) {
            return true;
        }

        Matcher matcher = NUMERO_PATTERN.matcher(number);

        return !matcher.matches();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = this.response + "\n" + response;
    }

    public void clearResponse() {
        this.response = "\n";
    }
}