package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

public class Wait extends State {
    private Gson json;
    private ClientModel myClientModel;
    private ClientModel receivedClientModel;
    private Controller controller;
    private View view;

    private Event messaggioGestito;
    public Wait(ClientModel clientModel, View view, Controller controller) {
        super("[Stato di attesa di comandi (aggiornamento vista o comandi di inserimento da terminale)]");
        json = new Gson();
        myClientModel = clientModel;
        this.view = view;
        this.controller = controller;
        messaggioGestito = new Event(" messaggio gestito");
        messaggioGestito.setStateEventListener(controller);
    }

    public Event messaggioGestito() {
        return messaggioGestito;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
            ParametersFromNetwork message = new ParametersFromNetwork(1);

            message.enable();
            while (!message.parametersReceived()) {
                // non ho ricevuto ancora nessun messaggio
            }
            receivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);



            if (receivedClientModel.isGameStarted().equals(true)) {
                Gson json = new Gson();

                view.setClientModel(receivedClientModel);


                // Il messaggio è o una richiesta o una risposta

                // se il messaggio non è una risposta di un client al server vuol dire che
                if (receivedClientModel.isResponse().equals(false)) {
                    // il messaggio è una richiesta del server alla view di un client

                    // se il messaggio è rivolto a me devo essere io a compiere l'azione
                    if (receivedClientModel.getClientIdentity() == myClientModel.getClientIdentity()) {
                        // il messaggio è rivolto a me
                        view.requestToMe();
                    } else {
                        // altrimenti devo limitarmi a segnalare che l'altro giocatore sta facendo qualcosa
                        view.requestToOthers();
                    }
                }
                // altrimenti il messaggio è una risposta di un altro client ad un server
                else if (receivedClientModel.isResponse().equals(true)) {
                    view.response();
                }

            }
        messaggioGestito.fireStateEvent();
        return super.entryAction(cause);
    }
}
