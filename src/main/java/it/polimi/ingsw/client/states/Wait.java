package it.polimi.ingsw.client.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class Wait extends State {
    private ParametersFromNetwork message;
    private Gson json;
    private ClientModel myClientModel;
    private ClientModel receivedClientModel;
    private View view;
    public Wait(ClientModel clientModel, View view, Controller controller) {
        super("[Stato di attesa di comandi (aggiornamento vista o comandi di inserimento da terminale)]");
        json = new Gson();
        myClientModel = clientModel;
        this.view = view;
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
    }

    public ParametersFromNetwork messaggioGestito() {
        return message;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {


           message.enable();
           while(!message.parametersReceived()){
               // non ho ricevuto ancora nessun messaggio
           }
            receivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);


           if(receivedClientModel.isGameStarded().equals(true)) {
               Gson json = new Gson();
               System.out.println(message.getParameter(0));

               System.out.println(receivedClientModel.getNickname());
                view.setClientModel(receivedClientModel);



                // Il messaggio è o una richiesta o una risposta

               // se il messaggio non è una risposta di un client al server vuol dire che
               if (receivedClientModel.isResponse().equals(false)) {
                   // il messaggio è una richiesta del server alla view di un client

                   // se il messaggio è rivolto a me devo essere io a compiere l'azione
                   if (receivedClientModel.getNickname().equals(myClientModel.getNickname())) {
                       // il messaggio è rivolto a me
                       view.requestToMe();
                   } else {
                       // altrimenti devo limitarmi a segnalare che l'altro giocatore sta facendo qualcosa
                       view.requestToOthers(receivedClientModel.getNickname(), receivedClientModel.getRequestDescription());
                   }
               }
               // altrimenti il messaggio è una risposta di un client ad un server
               else if (receivedClientModel.isResponse().equals(true)) {
                   view.response(receivedClientModel.getNickname(), receivedClientModel.getRequestDescription());
               }

           }
        message.fireStateEvent();
        return super.entryAction(cause);
    }
}
