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
    public void askConnectionInfo() {
        if (callingState instanceof READ) {
            // Gli eventi (di uscita dallo stato corrente callingState) devono essere abilitati per poter avvenire
            ((READ) callingState).numberOfParametersIncorrect().enable();
            ((READ) callingState).insertedParameters().enable();

            if (precedentCallingState instanceof WelcomeScreen){
                CommandPrompt.ask(
                        "Inserisci nickname, indirizzo ip e porta separati da uno spazio e clicca invio",
                        "nickname ip porta> ");
            }

            // Disabilitare gli eventi una volta che uno di loro è avvenuto elimina la possibilità del verificarsi di
            // eventi concorrenti.
            ((READ) callingState).numberOfParametersIncorrect().disable();
            ((READ) callingState).insertedParameters().disable();
        }
    }

    @Override
    public void askGameCode() {
        if (callingState instanceof READ) {
            // Gli eventi (di uscita dallo stato corrente callingState) devono essere abilitati per poter avvenire
            ((READ) callingState).numberOfParametersIncorrect().enable();
            ((READ) callingState).insertedParameters().enable();


            CommandPrompt.ask(
                        "Inserisci il game code della partita a cui ti vuoi unire",
                        "gamecode");

            // Disabilitare gli eventi una volta che uno di loro è avvenuto elimina la possibilità del verificarsi di
            // eventi concorrenti.
            ((READ) callingState).numberOfParametersIncorrect().disable();
            ((READ) callingState).insertedParameters().disable();
        }
    }
    @Override
    public void askGameInfo() {
        if (callingState instanceof READ) {
            // Gli eventi (di uscita dallo stato corrente callingState) devono essere abilitati per poter avvenire
            ((READ) callingState).numberOfParametersIncorrect().enable();
            ((READ) callingState).insertedParameters().enable();

                CommandPrompt.ask(
                        "Inserisci informazioni del gioco che vuoi creare",
                        "numerogiocatori gamemode> ");

            // Disabilitare gli eventi una volta che uno di loro è avvenuto elimina la possibilità del verificarsi di
            // eventi concorrenti.
            ((READ) callingState).numberOfParametersIncorrect().disable();
            ((READ) callingState).insertedParameters().disable();
        }
    }

    @Override
    public void askToStart() {
        if (callingState instanceof WelcomeScreen) {
            ((WelcomeScreen)callingState).start().enable();
            ((WelcomeScreen)callingState).notStart().enable();

            CommandPrompt.ask(
                    "Scrivi start per far partire il gioco e premi invio",
                    "START THE GAME> ");
        }

        ((WelcomeScreen)callingState).start().disable();
        ((WelcomeScreen)callingState).notStart().disable();
    }

    @Override
    public void askConnectOrCreate() {
        if (callingState instanceof CreateOrConnectDecision) {
            ((CreateOrConnectDecision)callingState).haSceltoCrea().enable();
            ((CreateOrConnectDecision)callingState).haSceltoConnetti().enable();
            ((CreateOrConnectDecision)callingState).sceltaNonValida().enable();

            CommandPrompt.ask(
                    "Scegli se creare una nuova partita o connetterti ad una partita esistente",
                    "create or connect> ");

            ((CreateOrConnectDecision)callingState).haSceltoCrea().disable();
            ((CreateOrConnectDecision)callingState).haSceltoConnetti().disable();
            ((CreateOrConnectDecision)callingState).sceltaNonValida().disable();
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
    public void ComunicationError(){
        try{
            CommandPrompt.println("COMUNICATION ERROR: something strage happened...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

       @Override
    public void ask_carta_assistente() {
        if (callingState instanceof ShowAssistantsCards) {


            // Gli eventi (di uscita dallo stato corrente callingState) devono essere abilitati per poter avvenire
            ((ShowAssistantsCards) callingState).insertedCard().enable();
            ((ShowAssistantsCards) callingState).numberOfParametersIncorrect().enable();

            if (precedentCallingState instanceof WaitForTurn) {
                CommandPrompt.ask(
                        "Inserisci numero di carta scelta",
                        "numero della carta");
            }


            // Disabilitare gli eventi una volta che uno di loro è avvenuto elimina la possibilità del verificarsi di
            // eventi concorrenti.
            ((ShowAssistantsCards) callingState).insertedCard().disable();
            ((ShowAssistantsCards) callingState).numberOfParametersIncorrect().disable();
        }
    }
}

