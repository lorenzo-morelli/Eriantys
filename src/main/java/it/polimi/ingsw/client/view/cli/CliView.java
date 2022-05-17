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

    private ClientModel networkClientModel;
    private String mynickname;
    private String responce;

    private Gson json;
    public CliView(){
        responce="\n";
    }
    public void setClientModel(ClientModel clientModel) {
        this.networkClientModel = clientModel;
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
                setMynickname(parsedStrings.get(0));
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
                setMynickname(CommandPrompt.gotFromTerminal());
                break;
        }

        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().disable();
        ((ReadFromTerminal) callingState).insertedParameters().disable();
    }

    // Il server mi invia una richiesta di interazione: devo digitare roba da terminale
    public void requestToMe() throws InterruptedException {
        switch(networkClientModel.getTypeOfRequest()){

            case "CHOOSEASSISTANTCARD" :
                System.out.println(networkClientModel.getServermodel().toString(networkClientModel.getNickname(),"STATO DEL GIOCO: \n"+"E' IL TUO TURNO - ASSISTENT CARD PHASE"+ "\n\nMOSSE ALTRI GIOCATORI: "+getResponce()));
                System.out.println("Scegli una Carta Assistente");
                for (AssistantCard a : networkClientModel.getDeck()){;
                    System.out.println("valore: " + a.getValues() + "  mosse: " + a.getMoves());
                }
                CommandPrompt.ask("Inserisci valore della carta scelta","Carta> ");

                if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                    System.out.println("la carta da te scelta ha un valore non  valido, si prega di fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                boolean check=false;
                for(int i=0;i<networkClientModel.getDeck().size();i++){
                    if(networkClientModel.getDeck().get(i).getValues()==Integer.parseInt((CommandPrompt.gotFromTerminal()))){
                        check=true;
                    }
                }
                if(!check){
                    System.out.println("la carta scelta non è presente, fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                networkClientModel.setCardChoosedValue(Float.parseFloat(CommandPrompt.gotFromTerminal()));
                networkClientModel.setResponse(true); //lo flaggo come messaggio di risposta
                networkClientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(networkClientModel));

                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                System.out.println(networkClientModel.getServermodel().toString(networkClientModel.getNickname(),"STATO DEL GIOCO: \n"+"E' IL TUO TURNO - STUDENT PHASE"+ "\n\nMOSSE ALTRI GIOCATORI: "+getResponce()));

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

                if(networkClientModel.getServermodel().getcurrentPlayer().getSchoolBoard().getEntranceSpace().numStudentsbycolor(choosedColor) == 0 ){
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
                    if (networkClientModel.getServermodel().getcurrentPlayer().getSchoolBoard().getDinnerTable().numStudentsbycolor(choosedColor) == 10) {
                        System.out.println("La sala da pranzo di quel colore è piena.");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    networkClientModel.setTypeOfRequest("SCHOOL");
                }else if(command.equals("ISLAND")){
                    CommandPrompt.ask("Inserire numero dell'isola su cui si desidera muovere lo studente", "isola> ");
                    if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                        System.out.println("Si è inserito un numero non valido, reinserire i dati con più attenzione !!!!");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    if(Integer.parseInt(CommandPrompt.gotFromTerminal()) > networkClientModel.getServermodel().getTable().getIslands().size() ||
                            Integer.parseInt(CommandPrompt.gotFromTerminal()) < 0 ){
                        System.out.println("L'isola scelta non è valida.");
                        TimeUnit.SECONDS.sleep(2);
                        requestToMe();
                        return;
                    }
                    networkClientModel.setTypeOfRequest("ISLAND");
                    networkClientModel.setChoosedIsland(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1);
                }
                networkClientModel.setTypeOfRequest(command);
                networkClientModel.setResponse(true); //lo flaggo come messaggio di risposta
                networkClientModel.setChoosedColor(choosedColor);
                json = new Gson();
                Network.send(json.toJson(networkClientModel));
                break;

            case "TEAMMATE" :
                for (String nickname : networkClientModel.getNicknames()){
                    System.out.println(nickname);
                }
                CommandPrompt.ask("Inserisci il nickname del tuo compagno di squadra: ","Nickname> ");
                if(!networkClientModel.getNicknames().contains(CommandPrompt.gotFromTerminal())){
                    System.out.print("Il nickname inserito non esiste, si prega di scegliere un nickname tra quelli specificati");
                    requestToMe();
                    return;
                }
                String teamMate = CommandPrompt.gotFromTerminal();
                networkClientModel.getNicknames().remove(teamMate);
                networkClientModel.getNicknames().add(teamMate);
                networkClientModel.getNicknames().add(networkClientModel.getNickname());
                networkClientModel.setResponse(true); //lo flaggo come messaggio di risposta
                networkClientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(networkClientModel));
                break;

            case "CHOOSEWHERETOMOVEMOTHER" :
                System.out.println(networkClientModel.getServermodel().toString(networkClientModel.getNickname(),"STATO DEL GIOCO:\n "+"E' IL TUO TURNO - MOTHER PHASE"+ "\n\nMOSSE ALTRI GIOCATORI: "+getResponce()));
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
                    break;
                }
                if(networkClientModel.getServermodel().getcurrentPlayer().getChoosedCard().getMoves() < Integer.parseInt(CommandPrompt.gotFromTerminal())){
                    System.out.println("Il numero di mosse da te inserito eccede il numero massimo di mosse di cui madre natura può spostarsi," +
                            " ovvero " + networkClientModel.getServermodel().getcurrentPlayer().getChoosedCard().getMoves() );
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                networkClientModel.setChoosedMoves(Integer.parseInt(CommandPrompt.gotFromTerminal()));
                networkClientModel.setResponse(true); //lo flaggo come messaggio di risposta
                networkClientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(networkClientModel));

                break;

            case "CHOOSECLOUDS" :
                System.out.println(networkClientModel.getServermodel().toString(networkClientModel.getNickname(),"STATO DEL GIOCO: \n"+"E' IL TUO TURNO - CLOUD PHASE"+ "\n\nMOSSE ALTRI GIOCATORI: "+getResponce()));
                CommandPrompt.ask("Scegliere il numero della tessera nuvola da cui si desidera ricaricarsi di studenti","tessera nuvola> ");

                if(!isValidNumber(CommandPrompt.gotFromTerminal())){
                    System.out.println("Il numero inserito non è un numero valido");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                if( networkClientModel.getServermodel().getTable().getClouds().size() < Integer.parseInt(CommandPrompt.gotFromTerminal()) ||
                    Integer.parseInt(CommandPrompt.gotFromTerminal()) < 1 ){
                    System.out.println("Il numero inserito non rappresenta una tessera nuvola esistente, si prega di fare più attenzione");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                // la nuvola scelta deve avere una size > 0
                if(networkClientModel.getServermodel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1).getStudentsAccumulator().size() == 0){
                    System.out.println("Hai scelta una nuvola che è stata già scelta da un altro giocatore");
                    TimeUnit.SECONDS.sleep(2);
                    requestToMe();
                    return;
                }
                networkClientModel.setCloudChoosed(networkClientModel.getServermodel().getTable().getClouds().get(Integer.parseInt(CommandPrompt.gotFromTerminal()) - 1));
                networkClientModel.setResponse(true); //lo flaggo come messaggio di risposta
                networkClientModel.setFromTerminal(parsedStrings);
                json = new Gson();
                Network.send(json.toJson(networkClientModel));
                break;

            case "GAMEEND":
                System.out.println("Il vincitore/i è/sono :" + networkClientModel.getGamewinner());
                Network.disconnect();
            break;

        }
        clearResponce();

    }

    // Qualcun altro sta interagendo con il terminale: devo gestire il tempo di attesa
    // Esempio "pippo: sta salutando"
    public void requestToOthers() throws IOException {
        String message=null;
        switch(networkClientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD" :
                message="L'utente " +networkClientModel.getNickname()+ " sta scegliendo la carta assistente";
                break;
            case "CHOOSEWHERETOMOVESTUDENTS"    :
                message="L'utente " +networkClientModel.getNickname()+ " sta scegliendo dove muovere lo studente";
                break;
            case "TEAMMATE" :
                message="L'utente " +networkClientModel.getNickname()+ " sta scegliendo il suo compagno di squadra";
                break;
            case "CHOOSEWHERETOMOVEMOTHER"    :
                message="L'utente " +networkClientModel.getNickname()+ " sta scegliendo il numero di mosse di cui far spostare madre natura";
                break;
            case "CHOOSECLOUDS"    :
                message="L'utente " +networkClientModel.getNickname()+ " sta scegliendo la nuvola dalla quale ricaricare gli studenti";
                break;
        }
        System.out.println(networkClientModel.getServermodel().toString(getMynickname(),"STATO DEL GIOCO: "+message+ "\n\nPRECEDENTE MOSSA ALTRI GIOCATORI: "+getResponce()));
    }

    // Qualcun altro ha risposto al server: devo mostrare a schermo un'interpretazione della risposta
    // Esempio "pippo: ha salutato"
    public void response() throws IOException {
        switch(networkClientModel.getTypeOfRequest()) {
            case "CHOOSEASSISTANTCARD":
                setResponce("Il giocatore " + networkClientModel.getNickname()+" ha scelto " +
                        "la carta col valore = " +networkClientModel.getCardChoosedValue());
                break;
            case "TEAMMATE" :
                setResponce("Il giocatore " + networkClientModel.getNickname()+" ha formato i teams:\n" +
                        "TEAM 1: " + networkClientModel.getNicknames().get(3) + " " + networkClientModel.getNicknames().get(2)+"\n" +
                        "TEAM 2: " + networkClientModel.getNicknames().get(1) + " " + networkClientModel.getNicknames().get(0)+"\n" );
                break;
            case "SCHOOL"    :
                setResponce("L'utente " +networkClientModel.getNickname()+ " ha scelto di muovere 1 studente di colore " +
                         networkClientModel.getChoosedColor().toString() +" sulla sua scuola");
                break;
            case "ISLAND"    :
                setResponce("L'utente " +networkClientModel.getNickname()+ " ha scelto di muovere 1 studente di colore " +
                        networkClientModel.getChoosedColor().toString() +" sull' isola numero " +(networkClientModel.getChoosedIsland()+1));
                break;
            case "CHOOSEWHERETOMOVEMOTHER"    :
                setResponce("L'utente " +networkClientModel.getNickname()+ " ha scelto di spostare madre natura di " + networkClientModel.getChoosedMoves() + " mosse");
                break;
            case "CHOOSECLOUDS":
                setResponce("L'utente " +networkClientModel.getNickname()+ " ha scelto di ricaricare gli studenti dalla nuvola: " + networkClientModel.getCloudChoosed());
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

    public String getMynickname() {
        return mynickname;
    }

    public void setMynickname(String mynickname) {
        this.mynickname = mynickname;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = this.responce+"\n"+responce;
    }
    public void clearResponce(){
        this.responce="\n";
    }
}

