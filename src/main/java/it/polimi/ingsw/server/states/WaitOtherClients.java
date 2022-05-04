package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitOtherClients extends State {
    private ConnectionModel connectionModel;
    private int numOfPlayersToWait;
    private ClientModel clientModel = null;
    private Gson json;
    private Controller controller;
    private ParametersFromNetwork message;

    public WaitOtherClients(ConnectionModel connectionModel, Controller controller) {
        super("[Aspettando gli altri giocatori]");
        this.connectionModel = connectionModel;
        this.controller = controller;
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);

    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        // Quanti clients devo ancora aspettare?
        numOfPlayersToWait = connectionModel.getClientsInfo().get(0).getNumofplayer() - 1;

        // Bene, aspettiamoli
        while(numOfPlayersToWait != 0){
            System.out.println("[aspettando altri "+ numOfPlayersToWait+ " clients]");

            message.enable();

            while(!message.parametersReceived() ){
                // non ho ricevuto ancora un messaggio
            }

            if (message.parametersReceived()){
                // Converti il messaggio stringa json in un oggetto clientModel
                clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                System.out.println("Ricevuto " + clientModel.getNickname() +" " +clientModel.getMyIp());
                // Appendi alla lista di ClientModel il modello appena ricevuto così da salvarlo per usi futuri
                connectionModel.getClientsInfo().add(clientModel);
                // Compila il campo "sei primo" e invia la risposta al client
                clientModel.setAmIfirst(false);
                Network.send(json.toJson(clientModel));
                System.out.println("[Inviato ack al primo player]");

                //scateno l'evento ed esco dallo stato
                message.fireStateEvent();
                message.disable();
                numOfPlayersToWait --;
            }
        }


        return super.entryAction(cause);
    }
}
