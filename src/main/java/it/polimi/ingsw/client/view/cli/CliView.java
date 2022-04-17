package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.commandLine.CommandPrompt;

import java.io.IOException;

public class CliView implements View {
    @Override
    public String askCiao() {
        CommandPrompt.ask("Scrivi ciao","SALUTAMI> ");
        return CommandPrompt.gotFromTerminal();
    }

    @Override
    public String askNickname() {
        CommandPrompt.ask("Scrivi il tuo nickname","NICKNAME> ");
        return CommandPrompt.gotFromTerminal();
    }

    @Override
    public String askNicknameConfirmation(String nickname) {
        CommandPrompt.ask("Il tuo nickname è proprio " + nickname +"?","si/no> ");
        return CommandPrompt.gotFromTerminal();
    }

    @Override
    public void showConfirmation(String nickname) {
        try {
            CommandPrompt.println("Bene, allora ti chiamerò " + nickname);
        }catch (IOException e){};
        System.exit(0 );
    }
}
