package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.Objects;

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
        long end = start + 15 * 1000L;
        boolean check=true;

        while (check || System.currentTimeMillis()>=end){
            //System.out.println("loop");
            check=message.waitParametersReceived(5);
        }

        if(System.currentTimeMillis()>=end){
            System.out.println("Server non piu raggiungibile");
            reset.fireStateEvent();
            return super.entryAction(cause);
        }
        Thread t = new Thread(){
            public synchronized void run() {
                ClientModel TryreceivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                if(Objects.equals(TryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")){
                    return;
                }

                receivedClientModel=TryreceivedClientModel;

                if (Network.disconnectedClient()) {
                    Network.disconnect();
                    System.out.println("Il gioco è terminato a causa della disconnessione di un client");
                    isToReset = true;
                }

                if (receivedClientModel.isGameStarted().equals(true) && receivedClientModel.NotisKicked()) {


                    // Il messaggio è o una richiesta o una risposta

                    // se il messaggio non è una risposta di un client al server vuol dire che
                    if (receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null) {
                        // il messaggio è una richiesta del server alla view di un client

                        // se il messaggio è rivolto a me devo essere io a compiere l'azione
                        if (receivedClientModel.getClientIdentity() == myClientModel.getClientIdentity()) {
                            // il messaggio è rivolto a me
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
                            // altrimenti devo limitarmi a segnalare che l'altro giocatore sta facendo qualcosa
                            if (    receivedClientModel.getTypeOfRequest() == null                                                 ||
                                    receivedClientModel.isPingMessage()                                                            ||
                                    receivedClientModel.getTypeOfRequest().equals( "TRYTORECONNECT")                               ||
                                    receivedClientModel.getTypeOfRequest().equals("DISCONNECTION")                                   ) {
                                return;
                            }
                            else{
                                try {
                                    view.setClientModel(receivedClientModel);
                                    view.requestToOthers();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    // altrimenti il messaggio è una risposta di un altro client ad un server
                    else if (receivedClientModel.isResponse().equals(true) && receivedClientModel.getTypeOfRequest() != null) {
                        try {
                            view.setClientModel(receivedClientModel);
                            view.response();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
