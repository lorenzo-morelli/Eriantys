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

                       boolean ResultOfMovingMother= ChosenMotherMovementManagement();
                       if(ResultOfMovingMother){
                           TimeUnit.SECONDS.sleep(1);
                           requestToMe();
                           return;
                       }
                    }
                } else {

                    CommandPrompt.ask("Choose the number of moves to move Mother Nature ", "Moves: ");
                    boolean ResultOfMovingMother= ChosenMotherMovementManagement();
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
    private void addCards(ArrayList<String> available) { ////////////////////////
        System.out.println("AVAILABLE CARDS:");
        for (int i = 0; i < networkClientModel.getServerModel().getTable().getCharacters().size(); i++) {
            if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getCost() <= networkClientModel.getServerModel().getCurrentPlayer().getCoins()) {
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MONK")) {
                    System.out.println("MONK - EFFETTO: Prendi uno studente da questa carta e piazzalo su un isola a tua scelta.\n COMMAND: Inserisci MONK , colore scelto e numero dell'isola separati da uno spazio.\n\n");
                    available.add("MONK");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("PRINCESS")) {
                    System.out.println("PRINCESS - EFFETTO: Prendi uno studente da questa carta e piazzalo nella tua Sala.\nCOMMAND: Inserisci PRINCESS e colore scelto separati da uno spazio.\n\n");
                    available.add("PRINCESS");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MUSHROOMHUNTER")) {
                    System.out.println("MUSHROOMHUNTER - EFFETTO: Scegli un colore di studente; in questo turno, durante il calcolo dell'influenza, quel colore non fornisce influenza.\n COMMAND: Inserisci MUSHROOMHUNTER e colore scelto separati da uno spazio.\n\n");
                    available.add("MUSHROOMHUNTER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("THIEF")) {
                    System.out.println("THIEF - EFFETTO: Scegli un colore di studente; ogni giocatore (incluso te) deve rimettere nel sacchetto tre studenti di quel colore presenti nella Sala (o tutti quelli che ha se ne avesse meno di tre).\nCOMMAND: Inserisci THIEF e colore scelto separati da uno spazio\n\n");
                    available.add("THIEF");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("FARMER")) {
                    System.out.println("FARMER - EFFETTO: Durante questo turno, prendi il controllo dei professori anche se nella tua sala hai lo stesso numero di studenti del giocatore che li controlla in quel momento.\nCOMMAND: inserisci FARMER.\n\n");
                    available.add("FARMER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("CENTAUR")) {
                    System.out.println("CENTAUR - EFFETTO: Durante il conteggio di un influenza dell'isola le torri presenti non vengono calcolate.\nCOMMAND: inserisci CENTAUR.\n\n");
                    available.add("CENTAUR");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("KNIGHT")) {
                    System.out.println("KNIGHT - EFFETTO: In questo turno, durante il calcolo dell'influenza, hai due punti di influenza addizionali.\nCOMMAND: inserisci KNIGHT.\n\n");
                    available.add("KNIGHT");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("POSTMAN")) {
                    System.out.println("POSTMAN - EFFETTO: Puoi muovere madre natura fino a due isole addizionali rispetto a quanto indicato sulla carta assistente che hai giocato.\nCOMMAND: inserisci POSTMAN.\n\n");
                    available.add("POSTMAN");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("GRANNY")) {
                    System.out.println("GRANNY - EFFETTO: Piazza un divieto su un isola a tua scelta. La prima volta che madre natura termina il suo movimento li l'influenza non verra' calcolata e il divieto verra' reinserito in questa carta.\nCOMMAND: inserisci GRANNY e numero dell'isola separati da uno spazio.\n\n");
                    available.add("GRANNY");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("HERALD")) {
                    System.out.println("HERALD - EFFETTO: Scegli un isola e calcola la maggioranza come se madre natura avesse terminato il suo movimento li. In questo turno madre natura si muovera' come di consueto e nell'isola dove terminera' il suo movimento la maggioranza verra' normalmente calcolata.\nCOMMAND: inserisci HERALD e numero dell'isola separati da uno spazio.\n\n");
                    available.add("HERALD");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("JESTER")) {
                    System.out.println("JESTER - EFFETTO: Puoi prendere fino a 3 studenti da questa carta e scambiarli con altrettanti studenti presenti nel tuo Ingresso.\nCOMMAND: inserisci JESTER ,di seguito i colori da scambiare da Ingresso e poi i colori da scambiare da questa Carta, tutti separati da uno spazio (il numero di colori di uno e dell'altro deve essere lo stesso).\n\n");
                    available.add("JESTER");
                }
                if (networkClientModel.getServerModel().getTable().getCharacters().get(i).getName().equals("MINSTRELL")) {
                    System.out.println("MINSTRELL - EFFETTO: Puoi scambiare fra loro fino a due studenti presenti nella tua Sala e nel tuo Ingresso.\nCOMMAND: Inserisci MINSTRELL ,di seguito i colori da scambiare da Ingresso e poi i colori da scambiare da Sala tutti separati da uno spazio (il numero di colori di uno e dell'altro deve essere lo stesso).\n\n");
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
    private boolean schoolOrIslandChooseManagement(PeopleColor chosenColor) throws InterruptedException {  ///////////////

        Gson json;
        if (networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getEntranceSpace().numStudentsByColor(chosenColor) == 0) {
            System.out.println("Si e' inserito un colore non presente tra quelli disponibili, reinserire i dati con piu' attenzione !!!!");
            return true;
        }

        CommandPrompt.ask("Scegliere SCHOOL se si desidera " +
                "muovere uno studente dalla tua entrance space alla sala da pranzo, " +
                "altrimenti scrivi ISLAND se desideri muovere uno studente su un'isola, inserire una qualsiasi altra riga di comando se si vuole tornare indietro", "SCHOOL or ISLAND: ");

        String command = CommandPrompt.gotFromTerminal();
        if (!command.equals("SCHOOL") && !command.equals("ISLAND")) {
            System.out.println("ALERT: Command non valid");
            return true;
        } else if (command.equals("SCHOOL")) {
            if (networkClientModel.getServerModel().getCurrentPlayer().getSchoolBoard().getDinnerTable().numStudentsByColor(chosenColor) == 10) {
                System.out.println("La sala da pranzo di quel colore e' piena.");
                return true;
            }
            networkClientModel.setTypeOfRequest("SCHOOL");
        } else {
            CommandPrompt.ask("Inserire numero dell'isola su cui si desidera muovere lo studente", "isola: ");
            if (isValidNumber(CommandPrompt.gotFromTerminal())) {
                System.out.println("Si e' inserito un numero non valido, reinserire i dati con piu' attenzione !!!!");
                return true;
            }
            if (Integer.parseInt(CommandPrompt.gotFromTerminal()) > networkClientModel.getServerModel().getTable().getIslands().size() ||
                    Integer.parseInt(CommandPrompt.gotFromTerminal()) < 0) {
                System.out.println("L'isola scelta non e' valida.");
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
    private boolean ChosenMotherMovementManagement() { ///////////////
        Gson json;
        if (isValidNumber(CommandPrompt.gotFromTerminal())) {
            System.out.println("Il numero di mosse da te inserito non e' un numero valido, si prega di fare piu' attenzione");
            return true;
        }
        if (Integer.parseInt((CommandPrompt.gotFromTerminal())) <= 0) {
            System.out.println("Il numero di mosse deve essere un numero intero positivo, fare piu' attenzione");
            return true;
        }
        if (networkClientModel.getServerModel().getCurrentPlayer().getChosenCard().getMoves() < Integer.parseInt(CommandPrompt.gotFromTerminal())) {
            System.out.println("Il numero di mosse da te inserito eccede il numero massimo di mosse di cui madre natura puo' spostarsi," +
                    " ovvero " + networkClientModel.getServerModel().getCurrentPlayer().getChosenCard().getMoves());
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
    private boolean chosenCloudManagement() { ////////////////////////
        Gson json;

        if (isValidNumber(CommandPrompt.gotFromTerminal())) {
            System.out.println("Il numero inserito non e' un numero valido");
            return true;
        }
        if (networkClientModel.getServerModel().getTable().getClouds().size() < Integer.parseInt(CommandPrompt.gotFromTerminal()) ||
                Integer.parseInt(CommandPrompt.gotFromTerminal()) < 1) {
            System.out.println("Il numero inserito non rappresenta una tessera nuvola esistente, si prega di fare piu' attenzione");
            return true;
        }
        if (networkClientModel.getServerModel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1).getStudentsAccumulator().size() == 0) {
            System.out.println("Hai scelta una nuvola che e' stata gia' scelta da un altro giocatore");
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
                message = "L'utente " + networkClientModel.getNickname() + " sta scegliendo la carta assistente";
                break;
            case "CHOOSEWHERETOMOVESTUDENTS":
                message = "L'utente " + networkClientModel.getNickname() + " sta scegliendo dove muovere lo studente";
                break;
            case "TEAMMATE":
                message = "L'utente " + networkClientModel.getNickname() + " sta scegliendo il suo compagno di squadra";
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                message = "L'utente " + networkClientModel.getNickname() + " sta scegliendo il numero di mosse di cui far spostare madre natura";
                break;
            case "CHOOSECLOUDS":
                message = "L'utente " + networkClientModel.getNickname() + " sta scegliendo la nuvola dalla quale ricaricare gli studenti";
                break;
        }
        System.out.println(message);
        if (!networkClientModel.getTypeOfRequest().equals("TEAMMATE") && networkClientModel.getServerModel() != null) {
            System.out.println(networkClientModel.getServerModel().toString(getNickname(), "STATO DEL GIOCO: " + message + "\n\nMOSSE ALTRI GIOCATORI: " + getResponse()));
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
                setResponse("Il giocatore " + networkClientModel.getNickname() + " ha scelto " +
                        "la carta col valore = " + networkClientModel.getCardChosenValue());
                break;
            case "TEAMMATE":
                setResponse("Il giocatore " + networkClientModel.getNickname() + " ha formato i teams:\n" +
                        "TEAM 1: " + networkClientModel.getNicknames().get(3) + " " + networkClientModel.getNicknames().get(2) + "\n" +
                        "TEAM 2: " + networkClientModel.getNicknames().get(1) + " " + networkClientModel.getNicknames().get(0) + "\n");
                break;
            case "SCHOOL":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di muovere 1 studente di colore " +
                        networkClientModel.getChosenColor().toString() + " sulla sua scuola");
                break;
            case "ISLAND":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di muovere 1 studente di colore " +
                        networkClientModel.getChosenColor().toString() + " sull' isola numero " + (networkClientModel.getChosenIsland() + 1));
                break;
            case "CHOOSEWHERETOMOVEMOTHER":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di spostare madre natura di " + networkClientModel.getChosenMoves() + " mosse");
                break;
            case "CHOOSECLOUDS":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di ricaricare gli studenti dalla nuvola: " + networkClientModel.getCloudChosen());
                break;
            case "MONK":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio MONK, scegliendo come colore: " + networkClientModel.getChosenColor() + " e scegliendo come isola: " + networkClientModel.getChosenIsland() + "  (EFFETTO: Prendi uno studente da questa carta e piazzalo su un isola a tua scelta).");
                break;
            case "HERALD":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio HERALD, scegliendo come isola: " + networkClientModel.getChosenIsland() + "  (EFFETTO: Scegli un isola e calcola la maggioranza come se madre natura avesse terminato il suo movimento li. In questo turno madre natura si muovera' come di consueto e nell'isola dove terminera' il suo movimento la maggioranza verra' normalmente calcolata).");
                break;
            case "PRINCESS":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio PRINCESS, scegliendo come colore: " + networkClientModel.getChosenColor() + "  (EFFETTO: Prendi uno studente da questa carta e piazzalo nella tua Sala).");
                break;
            case "THIEF":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio THIEF, scegliendo come colore: " + networkClientModel.getChosenColor() + "  (EFFETTO: Scegli un colore di studente; ogni giocatore (incluso te) deve rimettere nel sacchetto tre studenti di quel colore presenti nella Sala (o tutti quelli che ha se ne avesse meno di tre) ).");
                break;
            case "MUSHROOMHUNTER":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio MUSHROOMHUNTER, scegliendo come colore: " + networkClientModel.getChosenColor() + "  (EFFETTO: Scegli un colore di studente; in questo turno, durante il calcolo dell'influenza, quel colore non fornisce influenza).");
                break;
            case "KNIGHT":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio KNIGHT" + "  (EFFETTO: In questo turno, durante il calcolo dell'influenza, hai due punti di influenza addizionali).");
                break;
            case "CENTAUR":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio CENTAUR" + "  (EFFETTO: Durante il conteggio di un influenza dell'isola le torri presenti non vengono calcolate).");
                break;
            case "FARMER":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio FARMER" + "  (EFFETTO: Durante questo turno, prendi il controllo dei professori anche se nella tua sala hai lo stesso numero di studenti del giocatore che li controlla in quel momento).");
                break;
            case "POSTMAN":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio POSTMAN" + "  (EFFETTO: Puoi muovere madre natura fino a due isole addizionali rispetto a quanto indicato sulla carta assistente che hai giocato).");
                break;
            case "GRANNY":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio GRANNY, scegliendo come isola: " + networkClientModel.getChosenIsland() + "  (EFFETTO: Piazza un divieto su un isola a tua scelta. La prima volta che madre natura termina il suo movimento li l'influenza non verra' calcolata e il divieto verra' reinserito in questa carta).");
                break;
            case "JESTER":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio JESTER, scegliendo di scambiare i colori: " + networkClientModel.getColors1() + " dal suo Ingresso con i colori: " + networkClientModel.getColors2() + " da questa carta" + "  (EFFETTO: Puoi prendere fino a 3 studenti da questa carta e scambiarli con altrettanti studenti presenti nel tuo Ingresso).");
                break;
            case "MINSTRELL":
                setResponse("L'utente " + networkClientModel.getNickname() + " ha scelto di usare la carta personaggio MINSTRELL, scegliendo di scambiare i colori: " + networkClientModel.getColors1() + " dal suo Ingresso con i colori: " + networkClientModel.getColors2() + " dalla sua Sala" + "  (EFFETTO: Puoi scambiare fra loro fino a due studenti presenti nella tua Sala e nel tuo Ingresso).");
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