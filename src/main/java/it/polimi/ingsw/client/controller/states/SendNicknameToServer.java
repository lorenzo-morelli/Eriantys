package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class SendNicknameToServer extends State {
    private Gson json;
    private ClientModel clientModel;
    private Controller controller;
    private ParametersFromNetwork ack;
    private Event nickAlreadyExistent;
    private Event nickUnique;

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
            // Attendi la ricezione dell'ack
            checkAck();
        }catch (InterruptedException e){e.printStackTrace();}
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException{

    }


    public void checkAck() throws Exception {

        ack = new ParametersFromNetwork(1);
        ack.setStateEventListener(controller);
        ack.enable();
        //System.out.println("[Conferma del nickname non ancora ricevuta]");
        while (!ack.parametersReceived()){
            // non ho ancora ricevuto l'ack
        }

        //System.out.println("[Conferma del nickname ricevuta]");
        ClientModel fromNetwork = json.fromJson(ack.getParameter(0),ClientModel.class);
        System.out.println(ack.getParameter(0));

        if(fromNetwork.getAmIfirst() == null){
            // ancora una volta l'utente ha inserito un nickname già esistente
            nickAlreadyExistent.fireStateEvent();
        }
        else {
            // l'utente ha finalmente inserito un nickname valido


            System.out.println("In attesa che gli altri giocatori si colleghino...");
            nickUnique.fireStateEvent();
        }

    }
}
