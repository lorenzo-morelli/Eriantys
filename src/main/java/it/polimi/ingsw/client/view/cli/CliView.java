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
        if (callingState instanceof AskConnectionInfoScreen) {


            // Gli eventi (di uscita dallo stato corrente callingState) devono essere abilitati per poter avvenire
            ((AskConnectionInfoScreen) callingState).userInfo().enable();
            ((AskConnectionInfoScreen) callingState).numberOfParametersIncorrect().enable();

            CommandPrompt.ask(
                    "Inserisci nickname, indirizzo ip e porta separati da uno spazio e clicca invio",
                    "nickname ip porta> ");

            // Disabilitare gli eventi una volta che uno di loro è avvenuto elimina la possibilità del verificarsi di
            // eventi concorrenti.
            ((AskConnectionInfoScreen) callingState).userInfo().disable();
            ((AskConnectionInfoScreen) callingState).numberOfParametersIncorrect().disable();
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
        if (callingState instanceof CreateOrConnectScreen) {
            ((CreateOrConnectScreen)callingState).haSceltoCrea().enable();
            ((CreateOrConnectScreen)callingState).haSceltoConnetti().enable();
            ((CreateOrConnectScreen)callingState).sceltaNonValida().enable();

            CommandPrompt.ask(
                    "Scegli se creare una nuova partita o connetterti ad una partita esistente",
                    "create or connect> ");

            ((CreateOrConnectScreen)callingState).haSceltoCrea().enable();
            ((CreateOrConnectScreen)callingState).haSceltoConnetti().enable();
            ((CreateOrConnectScreen)callingState).sceltaNonValida().enable();
        }
    }

    @Override
    public void showTryToConnect() {
        try{
            CommandPrompt.println("Tentativo di connessione in corso");
            System.exit(0); // per il momento chiude l'applicazione
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }

    @Override
    public void showCreatingGame() {
        try{
            CommandPrompt.println("Connessione ad una partita esistente");
            System.exit(0); // per il momento chiude l'applicazione
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }
}
