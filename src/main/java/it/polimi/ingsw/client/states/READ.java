package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class READ extends State {
    Model model;
    View view;
    String type;
    int parameter;
    ParametersFromTerminal insertedParameters;
    IncorrectParameters numberOfParametersIncorrect;

    public READ(View view, Model model, int numofparameters, String type) throws IOException {
        super("[STATO di lettura di " + numofparameters + " parametri da terminale]");
        this.view = view;
        this.model = model;
        this.parameter = numofparameters;
        this.type = type;

        // L'utente deve inserire 3 parametri: nickname, ip e porta
        insertedParameters = new ParametersFromTerminal(model, numofparameters);

        // Devo essere capace di intercettare se l'utente inserisce un numero diverso da 3 parametri
        numberOfParametersIncorrect = new IncorrectParameters(model, numofparameters);
    }

    public ParametersFromTerminal insertedParameters() {
        return insertedParameters;
    }

    public IncorrectParameters numberOfParametersIncorrect() {
        return numberOfParametersIncorrect;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);

        switch (this.type) {
            case ("INFO"):
                view.askConnectionInfo();
                break;
            case ("GAMECODE"):
                view.askGameCode();
                break;
            case ("GAMEINFO"):
                view.askGameInfo();
                break;
        }
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            switch (this.type) {
                case ("INFO"): //NICKNAME / IP / PORTA
                    model.setNickname(model.getFromTerminal().get(0));
                    model.setIp(model.getFromTerminal().get(1));
                    model.setPort(model.getFromTerminal().get(2));
                    break;
                case ("GAMECODE"): //GAMECODE
                    model.setGameCodeNumber(Integer.parseInt(model.getFromTerminal().get(0)));
                    break;
                case ("GAMEINFO"):  //NUMOFPLAYER / GAMEMODE
                    model.setNumofplayer(Integer.parseInt(model.getFromTerminal().get(0)));
                    model.setGamemode(model.getFromTerminal().get(1));
                    break;
            }
        }
    }
}
