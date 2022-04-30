package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class READ extends State{
    Model model;
    View view;

    int parameter;
    ParametersFromTerminal insertedUserInfo;
    IncorrectParameters numberOfParametersIncorrect;

    public READ(View view, Model model,int numofparameters) throws IOException {
        super("[STATO di lettura]");
        this.view = view;
        this.model = model;
        this.parameter= numofparameters;

        // L'utente deve inserire 3 parametri: nickname, ip e porta
        insertedUserInfo = new ParametersFromTerminal(model, numofparameters);

        // Devo essere capace di intercettare se l'utente inserisce un numero diverso da 3 parametri
        numberOfParametersIncorrect = new IncorrectParameters(model, numofparameters);
    }

    public ParametersFromTerminal insertedUserInfo() {
        return insertedUserInfo;
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

            if(view.getPrecedentCallingState() instanceof WelcomeScreen) {
                model.setNickname(model.getFromTerminal().get(0));
                model.setIp(model.getFromTerminal().get(1));
                model.setPort(model.getFromTerminal().get(2));
            }
            if(view.getPrecedentCallingState() instanceof CreateOrConnectDecision){
                model.setGameCodeNumber(Integer.parseInt(model.getFromTerminal().get(0)));
            }
            //if(view.getPrecedentCallingState() instanceof )
        }
    }

}
