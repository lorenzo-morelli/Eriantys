package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class Read_from_terminal extends State {
    Model model;
    View view;
    String type;
    int parameter;
    ParametersFromTerminal insertedParameters;
    IncorrectParameters numberOfParametersIncorrect;

    public Read_from_terminal(View view, Model model, int numofparameters, String type) throws IOException {
        super("[STATO di lettura di " + numofparameters + " parametri da terminale interpretati come :"+ type+ "]");
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
            case ("USERINFO"):
                view.askConnectionInfo();
                break;
            case ("GAMECODE"):
                view.askGameCode();
                break;
            case ("GAMEINFO"):
                view.askGameInfo();
                break;
            case("WICHCARD"):
                view.ask_carta_assistente();
                break;
            case("WICHSTUDENT"):
                view.ask_witch_student();
                break;
            case ("WICHISLAND"):
                view.ask_witch_island();
        }
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            switch (this.type) {
                case ("USERINFO"): //NICKNAME / IP / PORTA
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
                case("WICHCARD"): //CARD POSITION
                    model.setCardChoosed(Integer.parseInt(model.getFromTerminal().get(0)));
                case("WICHSTUDENT"): //STUDENT POSITION IN ENTRANCE
                    model.setStudent_in_entrance_Choosed(Integer.parseInt(model.getFromTerminal().get(0)));
                    break;
                case("WICHISLAND"): //ISLAND INDEX
                    model.setIsland_Choosed(Integer.parseInt(model.getFromTerminal().get(0)));
                    break;
            }
        }
    }
}
