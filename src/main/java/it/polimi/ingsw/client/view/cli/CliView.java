package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.cli.CommandPrompt;

import java.io.IOException;

public class CliView implements View {

    @Override
    public void askToStart() {
        CommandPrompt.ask(
                "Scrivi start per far partire il gioco e premi invio",
                "START THE GAME> ");
    }

    @Override
    public void askNickname() {
        CommandPrompt.ask(
                "Scrivi il tuo nickname",
                "your nickname> ");
    }

    @Override
    public void askNicknameConfirmation(String nickname) {
        CommandPrompt.ask(
                "Il tuo nickname è proprio " + nickname +"?",
                "si/no> ");
    }

    @Override
    public void showConfirmation(String nickname) {
        try {
            CommandPrompt.println("Bene, allora ti chiamerò " + nickname);
        }catch (IOException e){};
        System.exit(0 );
    }
}
