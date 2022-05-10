package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Professor;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CliView implements View{

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;

    // parsedString ci serve per parsare l'input e verificare la correttezza dei dati inseriti
    private ArrayList<String> parsedStrings;

    private ClientModel clientModel;

    // Le regular expressions servono per individuare pattern e verificare la correttezza dell'input dell'utente
    private static final String IP_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final String PORT_REGEX=
            "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    private static final String CIFRA_REGEX ="[0-9]";
    private static final String NUMERO_REGEX ="[0-9]+";
    private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);
    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);
    private static final Pattern CIFRA_PATTERN = Pattern.compile(CIFRA_REGEX);
    private static final Pattern NUMERO_PATTERN = Pattern.compile(NUMERO_REGEX);

    // Metodo che controlla se un IP è valido con le regular expressions
    public static boolean isValidIp(String ip)
    {
        if (ip == null) {
            return false;
        }

        Matcher matcher = IP_PATTERN.matcher(ip);

        return matcher.matches();
    }

    // Metodo che controlla se una porta è valida con le regular expressions
    public static boolean isValidPort(String port)
    {
        if (port == null) {
            return false;
        }

        Matcher matcher = PORT_PATTERN.matcher(port);

        return matcher.matches();
    }

    // Metodo che controlla se una cifra è valida con le regular expressions
    public static boolean isValidCifra(String cifra)
    {
        if (cifra == null) {
            return false;
        }

        Matcher matcher = CIFRA_PATTERN.matcher(cifra);

        return matcher.matches();
    }

    // Metodo che controlla se un numero è valido con le regular expressions
    public static boolean isValidNumber(String number)
    {
        if (number == null) {
            return false;
        }

        Matcher matcher = NUMERO_PATTERN.matcher(number);

        return matcher.matches();
    }

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
                }
                // se è una cifra allora deve essere compresa tra 2 e 4
                if(Integer.parseInt(parsedStrings.get(0))<2 || Integer.parseInt(parsedStrings.get(0))>4){
                    System.out.print("ALERT: numOfPlayers deve essere compresa tra 2 e 4, riprovare\n");
                    askParameters();
                }
                // controllo che il gamemode sia tra PRINCIPIANT ed EXPERT
                if (!parsedStrings.get(1).equals("PRINCIPIANT") && !parsedStrings.get(1).equals("EXPERT")){
                    System.out.print("ALERT: il gameMode scelto deve essere 'PRINCIPIANT' oppure 'EXPERT', riprovare\n");
                    askParameters();
                }
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
    public void requestToMe(){
        switch(clientModel.getTypeOfRequest()){

            case "CHOOSEASSISTANTCARD" :
                List<Integer> indexList = new ArrayList<>();
                int i = 0;
                for (AssistantCard a : clientModel.getDeck()){
                    indexList.add(i);
                    System.out.println(i+": "+ "valore: " + a.getValues() + "  mosse: " + a.getMoves());
                    i++;
                }
                CommandPrompt.ask("Scegli una carta assistente",
                        "numero della carta>");
                parsedStrings = new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));

                // la cifra inserita deve essere valida
               if (!isValidCifra(parsedStrings.get(0))) {
                    System.out.print("ALERT: cifra inserita non valida, riprovare\n");
                    requestToMe();
                }
               // la cifra inserita deve essere tra quelle proposte (ovvero nelle availableCards)
               if(!indexList.contains(Integer.parseInt(parsedStrings.get(0)))){
                   System.out.print("ALERT: carta inserita inesistente, riprovare\n");
                   requestToMe();
               }
                clientModel.setResponse(true); //lo flaggo come messaggio di risposta
                clientModel.setFromTerminal(parsedStrings);
                Gson json = new Gson();
                Network.send(json.toJson(clientModel));
                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                /*System.out.println("PLAYERS SCHOOL BOARDS");
                for(Player p : clientModel.getPlayers()) {
                    System.out.println(p.getNickname());
                    System.out.println(p.getSchoolBoard().getDinnerTable().toString());
                    System.out.println(p.getSchoolBoard().getEntranceSpace().toString());
                    System.out.println("TOWERS: "+ p.getSchoolBoard().getNumOfTowers());
                }
                System.out.println("PROFESSORS");
                for(Professor p : clientModel.getProfessors()){
                    if(p.getHeldBy()==null) System.out.println(p.getColor()+ " " + "NESSUNO");
                    else {
                        System.out.println(p.getColor() + " " + p.getHeldBy().getNickname());
                    }
                }
                System.out.println("ISLANDS");
                int j=0;
                for(Island is : clientModel.getIslands()){
                    System.out.println(j);
                    System.out.println(is.getTowerColor().toString());
                    System.out.println(is.getInhabitants().toString());
                    j++;
                }*/
                for(Player p : clientModel.getPlayers()) {
                    System.out.println(p.toString());
                }
                System.out.println(clientModel.getCentreTable());

        }
    }

    // Qualcun altro sta interagendo con il terminale: devo gestire il tempo di attesa
    // Esempio "pippo: sta salutando"
    public void requestToOthers() throws IOException {
        switch(clientModel.getTypeOfRequest()) {
            case "HELLO" :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scrivendo Hello");
                break;
            case "CHOOSEASSISTANTCARD" :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo la carta assistente");
                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                CommandPrompt.println("L'utente " +clientModel.getNickname()+ " sta scegliendo dove muovere lo studente");
        }
    }

    // Qualcun altro ha risposto al server: devo mostrare a schermo un'interpretazione della risposta
    // Esempio "pippo: ha salutato"
    public void response() throws IOException {
        switch(clientModel.getTypeOfRequest()) {
            case "HELLO" :
                System.out.println("L'utente " +clientModel.getNickname()+ " ha scritto Hello");
            break;
            case "CHOOSEASSISTANTCARD":
                System.out.println("Il giocatore " + clientModel.getNickname()+" ha scelto " +
                        "valore = " +clientModel.getDeck().get(Integer.parseInt(clientModel.getFromTerminal().get(0))).getValues() +
                        " mosse = "+clientModel.getDeck().get(Integer.parseInt(clientModel.getFromTerminal().get(0))).getMoves());
                break;
        }
    }

}

