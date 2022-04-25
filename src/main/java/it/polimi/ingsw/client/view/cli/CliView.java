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

    @Override
    public void askConnectionInfo() {
        if (callingState instanceof AskConnectionInfoScreen) {

            ((AskConnectionInfoScreen) callingState).userInfo().enable();

            CommandPrompt.ask(
                    "Inserisci nickname, indirizzo ip e porta separati da uno spazio e clicca invio",
                    "nickname ip porta> ");

            ((AskConnectionInfoScreen) callingState).userInfo().disable();
        }
    }

    @Override
    public void setCallingState(State callingState) {
        this.precedentCallingState = this.callingState;
        this.callingState = callingState;
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
        ((WelcomeScreen)callingState).notStart().enable();
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
    public void showConfirmation(String nickname) {
        try {
            CommandPrompt.println("Bene, allora ti chiamerò " + nickname);
        }catch (IOException e){};
        System.exit(0 );
    }
}
