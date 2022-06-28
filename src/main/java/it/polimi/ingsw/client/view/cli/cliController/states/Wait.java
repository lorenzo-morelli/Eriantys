package it.polimi.ingsw.client.view.cli.cliController.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.Objects;

/**
 * The wait state should be interpreted as a "command waiting" state from the server.
 * The client is therefore at rest and only 3 types of messages will be delivered from server:
 * requestToMe, a request for direct client interaction (for example: insert the assistant card),
 * requestToOthers, i.e. the server is requesting interaction with another client ,
 * and response, that is, another client is responding to the server.
 */
public class Wait extends State {
    private final Gson json;
    private final ClientModel myClientModel;
    private ClientModel receivedClientModel;
    private final View view;

    private final Event messaggioGestito;
    private final Event reset;

    private boolean isToReset;

    public Wait(ClientModel clientModel, View view, Controller controller) {
        super("[Stato di attesa di comandi (aggiornamento vista o comandi di inserimento da terminale)]");
        json = new Gson();
        myClientModel = clientModel;
        this.view = view;
        messaggioGestito = new Event(" messaggio gestito");
        messaggioGestito.setStateEventListener(controller);
        reset = new Event("reset");
        reset.setStateEventListener(controller);
        isToReset = false;
    }

    public Event messaggioGestito() {
        return messaggioGestito;
    }

    public Event Reset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        ParametersFromNetwork message = new ParametersFromNetwork(1);
        message.enable();

        long start = System.currentTimeMillis();
        long end = start + 70 * 1000L;
        boolean check=true;

        while (check || System.currentTimeMillis()>=end){
            check=message.waitParametersReceived(5);
        }

        if(System.currentTimeMillis()>=end){
            System.out.println("Server non piu raggiungibile");
            reset.fireStateEvent();
            return super.entryAction(cause);
        }
        Thread t = new Thread(){
            public synchronized void run() {
                ClientModel tryReceivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                if(Objects.equals(tryReceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")){
                    return;
                }

                receivedClientModel= tryReceivedClientModel;

                if (Network.disconnectedClient()) {
                    Network.disconnect();
                    System.out.println("Il gioco e' terminato a causa della disconnessione di un client");
                    isToReset = true;
                }

                if (receivedClientModel.isGameStarted().equals(true) && receivedClientModel.isNotKicked()) {
                    if (receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null) {
                        if (receivedClientModel.getClientIdentity() == myClientModel.getClientIdentity()) {
                            if (receivedClientModel.isPingMessage()) {
                                view.requestPing();
                            } else {
                                try {
                                    view.setClientModel(receivedClientModel);
                                    view.requestToMe();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            if (    receivedClientModel.getTypeOfRequest() != null                                                &&
                                    !receivedClientModel.isPingMessage()                                                           &&
                                    !receivedClientModel.getTypeOfRequest().equals( "TRYTORECONNECT")                              &&
                                    !receivedClientModel.getTypeOfRequest().equals("DISCONNECTION")                                   ) {
                                view.setClientModel(receivedClientModel);
                                view.requestToOthers();
                            }
                        }
                    }
                    else if (receivedClientModel.isResponse().equals(true) && receivedClientModel.getTypeOfRequest() != null) {
                        view.setClientModel(receivedClientModel);
                        view.response();
                    }
                }
            }
        };
        t.start();

        if (isToReset) {
            reset.fireStateEvent();
        } else {
            messaggioGestito.fireStateEvent();
        }
        return super.entryAction(cause);
    }
}
