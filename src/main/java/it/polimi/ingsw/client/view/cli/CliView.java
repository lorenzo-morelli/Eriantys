package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.controller.states.Decision;
import it.polimi.ingsw.client.controller.states.ReadFromTerminal;
import it.polimi.ingsw.client.controller.states.WelcomeScreen;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.common.Check;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.utils.common.Check.isValidIp;
import static it.polimi.ingsw.utils.common.Check.isValidPort;

public class CliView implements View{

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;

    // parsedString ci serve per parsare l'input e verificare la correttezza dei dati inseriti
    private ArrayList<String> parsedStrings;

    private ClientModel clientModel;

    private Gson json;

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    // Uno stato che vuole chiamare un metodo della vista si registra prima chiamando questo metodo
    // ad esempio sono nello stato WelcomeScreen e faccio "view.setCallingState(this)"
    // Non è altro che il pattern Observer riadattato per il pattern State
    @Override
    public void setCallingState(State callingState) {
        this.callingState = callingState;
    }

    @Override
    public void askToStart() {
        if (callingState instanceof WelcomeScreen) {
            ((WelcomeScreen) callingState).start().enable();
            ((WelcomeScreen) callingState).notStart().enable();

            CommandPrompt.ask(
                    "Scrivi start per far partire il gioco e premi invio",
                    "START THE GAME> ");


            ((WelcomeScreen) callingState).start().disable();
            ((WelcomeScreen) callingState).notStart().disable();
        }
    }

    @Override
    public void askDecision(String option1, String option2) {
        if (callingState instanceof Decision) {
            ((Decision) callingState).haScelto1().enable();
            ((Decision) callingState).haScelto2().enable();

            CommandPrompt.ask(
                    "Scegli tra " + option1 + " e " + option2,
                    option1 + " or " + option2+"> ");

            ((Decision) callingState).haScelto1().disable();
            ((Decision) callingState).haScelto2().disable();
        }
    }

    /**
     * Metodo di richiesta all'utente di inserimento di un certo numero di parole separate da uno spazio,
     * lo stato chiamante sarà uno stato di Read (lettura da terminale), lo stato si occupa solo del dichiarare
     * il numero di parole da leggere e di recuperare poi le informazioni mettendole nel ClientModel, oggetto che
     * verrà poi spedito al server.
     * E' possibile implementare anche controlli client side di correttezza dei parametri inseriti, ad esempio se un
     * indirizzo IP è valido o meno.
     */
    @Override
    public void askParameters() {
        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().enable();
        ((ReadFromTerminal) callingState).insertedParameters().enable();

        switch (((ReadFromTerminal) callingState).getType()){

            case "USERINFO":
                CommandPrompt.ask("Inserire Nickname Ip e porta separati da uno spazio e premere invio",
                                      "nickname ip porta>");
                // Controllo di correttezza dei dati inseriti lato client
                parsedStrings =
                        new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
                if (parsedStrings.size()!=3 || !isValidIp(parsedStrings.get(1)) || !isValidPort(parsedStrings.get(2))){
                    System.out.print("ALERT: dati inseriti non validi, riprovare\n");
                    askParameters();
                }
                break;

            case "GAMEINFO" :
                CommandPrompt.ask("Inserire numero di giocatori e modalità di gioco ",
                        "numOfPlayers gameMode>");
                // Controllo di correttezza dei dati inseriti lato client
                parsedStrings =
                        new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
                // controllo che numOfPlayers sia una cifra
                if (!isValidCifra(parsedStrings.get(0))){
                    System.out.print("ALERT: Inserisci una cifra su numOfPlayers, riprovare\n");
                    askParameters();
                    return;
                }
                // se è una cifra allora deve essere compresa tra 2 e 4
                if(Integer.parseInt(parsedStrings.get(0))<2 || Integer.parseInt(parsedStrings.get(0))>4){
                    System.out.print("ALERT: numOfPlayers deve essere compresa tra 2 e 4, riprovare\n");
                    askParameters();
                    return;
                }
                // controllo che il gamemode sia tra PRINCIPIANT ed EXPERT
                if (!parsedStrings.get(1).equals("PRINCIPIANT") && !parsedStrings.get(1).equals("EXPERT")){
                    System.out.print("ALERT: il gameMode scelto deve essere 'PRINCIPIANT' oppure 'EXPERT', riprovare\n");
                    askParameters();
                    return;
                }
                // Se siamo arrivati quì l'utente ha inserito numero di giocatori e gamemode nel formato richiesto
                // ovvero 2, 3, o 4 per numOfPlayers e PRINCIPIANT o EXPERT per la modalità di gioco
                // non occorre fare ulteriori controlli.
                break;

            case "NICKNAMEEXISTENT" :
                CommandPrompt.ask("Il nickname scelto è già esistente, si prega di reinserirne uno nuovo",
                        "nickname>");
                break;
        }

        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().disable();
        ((ReadFromTerminal) callingState).insertedParameters().disable();
    }

    // Il server mi invia una richiesta di interazione: devo digitare roba da terminale
    public void requestToMe() throws InterruptedException {
        switch(clientModel.getTypeOfRequest()){

            case "CHOOSEASSISTANTCARD" :
                System.out.println(clientModel.getServermodel().toString());
                System.out.println("Scegli una Carta Assistente");
                List<Integer> indexList = new ArrayList<>();
                int i = 0;
                for (AssistantCard a : clientModel.getDeck()){
                    indexList.add(i);
                    System.out.println(i+": "+ "valore: " + a.getValues() + "  mosse: " + a.getMoves());
                    i++;
                }
                CommandPrompt.ask("Inserisci codice indentficativo della carta","Carta> ");
                parsedStrings = new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));

                // la cifra inserita deve essere valida
               if (!isValidCifra(parsedStrings.get(0))) {
                    System.out.print("ALERT: cifra inserita non valida, riprovare\n");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
               // la cifra inserita deve essere tra quelle proposte (ovvero nelle availableCards)
               if(!indexList.contains(Integer.parseInt(parsedStrings.get(0)))){
                   System.out.print("ALERT: carta inserita inesistente, riprovare\n");
                   TimeUnit.SECONDS.sleep(2);
                   requestToMe();
                   return;
               }
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(clientModel));
                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                System.out.println(clientModel.getServermodel().toString());

                CommandPrompt.ask("Scegli il colore dello studente che desideri muovere ","RED or GREEN or BLUE or YELLOW or PINK> ");

                if(     !CommandPrompt.gotFromTerminal().equals("RED")   &&
                        !CommandPrompt.gotFromTerminal().equals("GREEN")   &&
                        !CommandPrompt.gotFromTerminal().equals("BLUE")   &&
                        !CommandPrompt.gotFromTerminal().equals("YELLOW")   &&
                        !CommandPrompt.gotFromTerminal().equals("PINK")   ){
                    System.out.println("Si è inserito un colore non valido, reinserire i dati con più attenzione !!!!");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }

                PeopleColor choosedColor;

                switch(CommandPrompt.gotFromTerminal()) {
                    case "RED":
                        choosedColor = PeopleColor.RED;
                        break;
                    case "GREEN":
                        choosedColor = PeopleColor.GREEN;
                        break;
                    case "BLUE":
                        choosedColor = PeopleColor.BLUE;
                        break;
                    case "YELLOW":
                        choosedColor = PeopleColor.YELLOW;
                        break;
                    case "PINK":
                        choosedColor = PeopleColor.PINK;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + CommandPrompt.gotFromTerminal());
                }

                if(clientModel.getServermodel().getcurrentPlayer().getSchoolBoard().getEntranceSpace().numStudentsbycolor(choosedColor) == 0 ){
                    System.out.println("Si è inserito un colore non presente tra quelli disponibili, reinserire i dati con più attenzione !!!!");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }

                CommandPrompt.ask("Scegliere SCHOOL se si desidera " +
                        "muovere uno studente dalla tua entrance space alla sala da pranzo, " +
                        "altrimenti scrivi ISLAND se desideri muovere uno studente su un'isola", "SCHOOL or ISLAND> ");

                String command = CommandPrompt.gotFromTerminal();
                if(!command.equals("SCHOOL") && !command.equals("ISLAND")){
                    requestToMe();
                    return;
                }else if(command.equals("SCHOOL")) {
                    if (clientModel.getServermodel().getcurrentPlayer().getSchoolBoard().getDinnerTable().numStudentsbycolor(choosedColor) == 10) {
                        System.out.println("La sala da pranzo di quel colore è piena.");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    clientModel.setTypeOfRequest("SCHOOL");
                }else if(command.equals("ISLAND")){
                    CommandPrompt.ask("Inserire numero dell'isola su cui si desidera muovere lo studente", "isola> ");
                    if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                        System.out.println("Si è inserito un numero non valido, reinserire i dati con più attenzione !!!!");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    if(Integer.parseInt(CommandPrompt.gotFromTerminal()) > clientModel.getServermodel().getTable().getIslands().size() ||
                            Integer.parseInt(CommandPrompt.gotFromTerminal()) < 0 ){
                        System.out.println("L'isola scelta non è valida.");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    clientModel.setTypeOfRequest("ISLAND");
                    clientModel.setChoosedIsland(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1);
                }
                clientModel.setTypeOfRequest(command);
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setChoosedColor(choosedColor);
                json = new Gson();
                Network.send(json.toJson(clientModel));
                break;

            case "TEAMMATE" :
                for (String nickname : clientModel.getNicknames()){
                    System.out.println(nickname);
                }
                CommandPrompt.ask("Inserisci il nickname del tuo compagno di squadra: ","Nickname> ");
                if(!clientModel.getNicknames().contains(CommandPrompt.gotFromTerminal())){
                    System.out.print("Il nickname inserito non esiste, si prega di scegliere un nickname tra quelli specificati");
                    requestToMe();
                    return;
                }
                String teamMate = CommandPrompt.gotFromTerminal();
                clientModel.getNicknames().remove(teamMate);
                clientModel.getNicknames().add(teamMate);
                clientModel.getNicknames().add(clientModel.getNickname());
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(clientModel));
                break;

            case "CHOOSEWHERETOMOVEMOTHER" :
                System.out.println(clientModel.getServermodel().toString());
                CommandPrompt.ask("Scegliere il numero di mosse di cui far spostare madre natura ","mosse> ");

                if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                    System.out.println("Il numero di mosse da te inserito non è un numero valido, si prega di fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                if(Integer.parseInt((CommandPrompt.gotFromTerminal()))<=0){
                    System.out.println("Il numero di mosse deve essere un numero intero positivo, fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                }
                if(clientModel.getServermodel().getcurrentPlayer().getChoosedCard().getMoves() < Integer.parseInt(CommandPrompt.gotFromTerminal())){
                    System.out.println("Il numero di mosse da te inserito eccede il numero massimo di mosse di cui madre natura può spostarsi," +
                            " ovvero " + clientModel.getServermodel().getcurrentPlayer().getChoosedCard().getMoves() );
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                clientModel.setChoosedMoves(Integer.parseInt(CommandPrompt.gotFromTerminal()));
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(clientModel));

                break;

            case "CHOOSECLOUDS" :
                System.out.println(clientModel.getServermodel().toString());
                CommandPrompt.ask("Scegliere il numero della tessera nuvola da cui si desidera ricaricarsi di studenti","tessera nuvola> ");

                if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                    requestToMe();
                    System.out.println("Il numero inserito non è un numero valido");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                if( clientModel.getServermodel().getTable().getClouds().size() < Integer.parseInt(CommandPrompt.gotFromTerminal()) ||
                    Integer.parseInt(CommandPrompt.gotFromTerminal()) < 1 ){
                    System.out.println("Il numero inserito non rappresenta una tessera nuvola esistente, si prega di fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                // la nuvola scelta deve avere una size > 0
                if(clientModel.getServermodel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1).getStudentsAccumulator().size() == 0){
                    System.out.println("Hai scelta una nuvola che è stata già scelta da un altro giocatore");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                }
                clientModel.setCloudChoosed(clientModel.getServermodel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1));
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(clientModel));
                break;

        }

    }

    // Qualcun altro sta interagendo con il terminale: devo gestire il tempo di attesa
    // Esempio "pippo: sta salutando"
    public void requestToOthers() throws IOException {
        switch(clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD" :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo la carta assistente");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo dove muovere lo studente");
                break;
            case "TEAMMATE" :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo il suo compagno di squadra");
                break;
            case "CHOOSEWHERETOMOVEMOTHER"    :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo il numero di mosse di cui far spostare madre natura");
                break;
            case "CHOOSECLOUDS"    :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo la nuvola dalla quale ricaricare gli studenti");
                break;
        }
    }

    // Qualcun altro ha risposto al server: devo mostrare a schermo un'interpretazione della risposta
    // Esempio "pippo: ha salutato"
    public void response() throws IOException {
        switch(clientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                System.out.println("Il giocatore " + clientModel.getNickname()+" ha scelto " +
                        "valore = " +clientModel.getDeck().get(Integer.parseInt(clientModel.getFromTerminal().get(0))).getValues() +
                        " mosse = "+clientModel.getDeck().get(Integer.parseInt(clientModel.getFromTerminal().get(0))).getMoves());
                break;
            case "TEAMMATE" :
                System.out.println("Il giocatore " + clientModel.getNickname()+" ha formato i teams:\n" +
                        "TEAM 1: " + clientModel.getNicknames().get(3) + " " + clientModel.getNicknames().get(2)+"\n" +
                        "TEAM 2: " + clientModel.getNicknames().get(1) + " " + clientModel.getNicknames().get(0)+"\n" );
                break;
            case "SCHOOL"    :
                System.out.println("L'utente " +clientModel.getNickname()+ " ha scelto di muovere 1 studente di colore " +
                         clientModel.getChoosedColor().toString() +" sulla sua scuola");
                break;
            case "ISLAND"    :
                System.out.println("L'utente " +clientModel.getNickname()+ " ha scelto di muovere 1 studente di colore " +
                        clientModel.getChoosedColor().toString() +" sull' isola numero " +(clientModel.getChoosedIsland()+1));
                break;
            case "CHOOSEWHERETOMOVEMOTHER"    :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " ha scelto di spostare madre natura di " + clientModel.getChoosedMoves() + " mosse");
                break;
            case "CHOOSECLOUDS":
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " ha scelto di ricaricare gli studenti dalla nuvola: " + clientModel.getCloudChoosed());
                break;
        }
    }





    // Methods for client side type-safety checking
    // Metodo che controlla se un IP è valido con le regular expressions


    // Metodo che controlla se una porta è valida con le regular expressions

    // Metodo che controlla se una cifra è valida con le regular expressions
    public static boolean isValidCifra(String cifra)
    {

        String CIFRA_REGEX ="[0-9]";

        Pattern CIFRA_PATTERN = Pattern.compile(CIFRA_REGEX);

        if (cifra == null) {
            return false;
        }

        Matcher matcher = CIFRA_PATTERN.matcher(cifra);

        return matcher.matches();
    }

    // Metodo che controlla se un numero è valido con le regular expressions
    public static boolean isValidNumber(String number)
    {
        String NUMERO_REGEX ="[0-9]+";
        Pattern NUMERO_PATTERN = Pattern.compile(NUMERO_REGEX);

        if (number == null) {
            return false;
        }

        Matcher matcher = NUMERO_PATTERN.matcher(number);

        return matcher.matches();
    }
}

