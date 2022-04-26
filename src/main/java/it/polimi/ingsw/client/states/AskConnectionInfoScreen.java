package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class AskConnectionInfoScreen extends State{
    Model model;
    View view;
    InputString insertUserInfo;

    public AskConnectionInfoScreen(View view, Model model) throws IOException {
        super("[STATO di attesa del nickname]");
        this.view = view;
        this.model = model;

        // L'utente deve inserire 3 parametri: nickname, ip e porta
        insertedUserInfo = new ParametersFromTerminal(model, 3);

        // Devo essere capace di intercettare se l'utente inserisce un numero diverso da 3 parametri
        numberOfParametersIncorrect = new IncorrectParameters(model, 3);
    }

    public InputString userInfo() {
        return insertUserInfo;
    }

    public IncorrectParameters numberOfParametersIncorrect() {
        return numberOfParametersIncorrect;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.askConnectionInfo();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            model.setNickname(model.getFromTerminal().get(0));
            model.setIp(model.getFromTerminal().get(1));
            model.setPort(model.getFromTerminal().get(2));
        }
        //todo: connettiti al server (indirizzo Ip, numero di porta NOTI) tramite connection info dati
    }

}
