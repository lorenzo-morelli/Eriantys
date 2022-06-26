package it.polimi.ingsw.client.view.cli.cliController.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

/**
 * Send the client nickname to the server, and handle the case in which the server reports the existence of another
 * client with the same nickname, by forwarding to the client view asking to choose another nickname.
 */
public class SendNicknameToServer extends State {
    private final Gson json;
    private final ClientModel clientModel;
    private final Controller controller;
    private ParametersFromNetwork ack;
    private final Event nickAlreadyExistent;
    private final Event nickUnique;

    public SendNicknameToServer(ClientModel clientModel, Controller controller) {
        super("[Invio dati al server ed attesa di un ack]");
        this.clientModel = clientModel;
        this.controller = controller;
        ack = new ParametersFromNetwork(1);
        json = new Gson();
        nickAlreadyExistent = new Event("Nickname already existent");
        nickUnique = new Event("Nick unique");
        nickAlreadyExistent.setStateEventListener(controller);
        nickUnique.setStateEventListener(controller);
    }

    public Event nickAlreadyExistent() {
        return nickAlreadyExistent;
    }

    public Event nickUnique() {
        return nickUnique;
    }


    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        System.out.println("[Inviato nuovo nickname al server]");
        //System.out.println(json.toJson(clientModel));
        Network.send(json.toJson(clientModel));
        try {
            checkAck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.entryAction(cause);
    }


    public void checkAck() throws Exception {
        ack = new ParametersFromNetwork(1);
        ack.setStateEventListener(controller);
        ack.enable();
        //System.out.println("[Conferma del nickname non ancora ricevuta]");
        ack.waitParametersReceived();

        //System.out.println("[Conferma del nickname ricevuta]");
        ClientModel fromNetwork = json.fromJson(ack.getParameter(0), ClientModel.class);
        System.out.println(ack.getParameter(0));

        if (fromNetwork.getAmIfirst() == null) {
            // ancora una volta l'utente ha inserito un nickname già esistente
            nickAlreadyExistent.fireStateEvent();
        } else {
            // l'utente ha finalmente inserito un nickname valido


            System.out.println("In attesa che gli altri giocatori si colleghino...");
            nickUnique.fireStateEvent();
        }

    }
}
