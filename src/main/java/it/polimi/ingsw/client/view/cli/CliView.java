package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.State;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CliView implements View{

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;
    private State precedentCallingState;

    // parsedString ci serve per parsare l'input e verificare la correttezza dei dati inseriti
    private ArrayList<String> parsedStrings;

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


    // Uno stato che vuole chiamare un metodo della vista si registra prima chiamando questo metodo
    // ad esempio sono nello stato WelcomeScreen e faccio "view.setCallingState(this)"
    // Non è altro che il pattern Observer riadattato per il pattern State
    @Override
    public void setCallingState(State callingState) {
        this.precedentCallingState = this.callingState;
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

        @Override
    public void showConnectingGame() {
        try{
            CommandPrompt.println("Connessione ad una partita esistente....");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

    @Override
    public void showWaitingForOtherPlayer(){
        try{
            CommandPrompt.println("Waiting for other player to join the game...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }
    @Override
    public void showGameStarted(){
        try{
            CommandPrompt.println("Game Started!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

    @Override
    public void itsyourturn(String command){
        try{
            if(command!="ENDGAME")
            CommandPrompt.println(" IT'S YOUR TURN - PHASE: "+command);
            else
                CommandPrompt.println(" THE GAME ENDED ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

    @Override
    public void showendscreen(String winner){
        try{
                CommandPrompt.println("GAME ENDED !! THE WINNER IS: "+ winner);
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

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
                    askParameters();
                }
                // se è una cifra allora deve essere compresa tra 2 e 4
                if(Integer.parseInt(parsedStrings.get(0))<2 || Integer.parseInt(parsedStrings.get(0))>4){
                    askParameters();
                }
                // controllo che il gamemode sia tra PRINCIPIANT ed EXPERT
                if (!parsedStrings.get(1).equals("PRINCIPIANT") && !parsedStrings.get(1).equals("EXPERT")){
                    askParameters();
                }
                break;

            case "NICKNAMEEXISTENT" :
                CommandPrompt.ask("Il nickname scelto è già esistente, si prega di reinserirne uno nuovo",
                        "nickname>");
                break;

            case "WICHCARD" :
                CommandPrompt.ask("Inserire la carta scelta",
                        "carta>");
                break;
            case "WICHSTUDENT" :
                CommandPrompt.ask("Inserire lo studente scelto",
                        "studente>");
                break;
            case "WICHISLAND" :
                CommandPrompt.ask("Inserire l'isola scelta'",
                        "isola>");
                break;
            case "WHEREMOVEMOTHER" :
                CommandPrompt.ask("Inserire di quanti passi si desidera muovere madre natura'",
                        "passi>");
                break;
            case "WICHCLOUD" :
                CommandPrompt.ask("Inserire da quale nuvola prelevare gli studenti",
                        "nuvola>");
                break;
        }

        ((ReadFromTerminal) callingState).numberOfParametersIncorrect().disable();
        ((ReadFromTerminal) callingState).insertedParameters().disable();
    }

}

