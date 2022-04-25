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
    public void askNickname() {
        ((AskNicknameScreen) callingState).nickname().enable();
        if(precedentCallingState instanceof WelcomeScreen){
            CommandPrompt.ask(
                        "Scrivi il tuo nickname",
                        "your nickname> ");
        }
        else{
            CommandPrompt.ask(
                        "Ok, allora correggi il tuo nickname",
                        "your nickname> ");
        }
        ((AskNicknameScreen) callingState).nickname().disable();


    }


    @Override
    public void askNicknameConfirmation(String nickname) {
        ((CheckNicknameScreen) callingState).si().enable();
        ((CheckNicknameScreen) callingState).no().enable();
        ((CheckNicknameScreen) callingState).neSineNo().enable();
        CommandPrompt.ask(
                "Il tuo nickname è proprio " + nickname +"?",
                "si/no> ");
        ((CheckNicknameScreen) callingState).si().disable();
        ((CheckNicknameScreen) callingState).no().disable();
        ((CheckNicknameScreen) callingState).neSineNo().disable();
    }

    @Override
    public void showConfirmation(String nickname) {
        try {
            CommandPrompt.println("Bene, allora ti chiamerò " + nickname);
        }catch (IOException e){};
        System.exit(0 );
    }
}
