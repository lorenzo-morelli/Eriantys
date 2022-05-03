package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class CliView implements View{

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;
    private State precedentCallingState;

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
    public void showTryToConnect() {
        try {
            CommandPrompt.println("Tentativo di connessione al server in corso");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
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
        ((Read_from_terminal) callingState).numberOfParametersIncorrect().enable();
        ((Read_from_terminal) callingState).insertedParameters().enable();

        switch (((Read_from_terminal) callingState).getType()){
            case "USERINFO":
                CommandPrompt.ask("Inserire Nickname Ip e porta separati da uno spazio e premere invio",
                                      "nickname ip porta>");
                break;
            case "GAMECODE":
                CommandPrompt.ask("Inserire il codice di gioco e premere invio",
                                    "GAMECODE>");
                break;
            case "GAMEINFO" :
                CommandPrompt.ask("Inserire numero di giocatori e modalità di gioco ",
                        "numOfPlayers gameMode>");
                break;
            case "WICHCARD"   :
                CommandPrompt.ask("Inserire la carta scelta",
                        "carta>");
                break;
            case "WICHSTUDENT":
                CommandPrompt.ask("Inserire lo studente scelto",
                        "studente>");
                break;
            case "WICHISLAND" :
                CommandPrompt.ask("Inserire l'isola scelta'",
                        "isola>");
                break;
            case "WHEREMOVEMOTHER":
                CommandPrompt.ask("Inserire di quanti passi si desidera muovere madre natura'",
                        "passi>");
                break;
            case "WICHCLOUD":
                CommandPrompt.ask("Inserire da quale nuvola prelevare gli studenti",
                        "nuvola>");
                break;
        }

        ((Read_from_terminal) callingState).numberOfParametersIncorrect().disable();
        ((Read_from_terminal) callingState).insertedParameters().disable();
    }

}

