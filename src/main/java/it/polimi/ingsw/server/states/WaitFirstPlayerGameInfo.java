package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitFirstPlayerGameInfo extends State {
    private ConnectionInfo connectionInfo;
    private ClientModel clientModel = null;
    private Gson json;
    private Controller controller;
    private ConnectionModel connectionModel;

    private ParametersFromNetwork message;
    public WaitFirstPlayerGameInfo(ConnectionInfo connectionInfo, Controller controller, ConnectionModel connectionModel) {
        super("[Il server è in attesa di gamemode e numero di giocatori]");
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        this.connectionInfo = connectionInfo;
        this.controller = controller;
        this.connectionModel= connectionModel;
    }

    public ParametersFromNetwork getMessage() {
        return message;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        System.out.println("[Non ho ancora ricevuto niente]");
        message.enable();
        while(!message.parametersReceived() ){
            // non ho ricevuto ancora il messaggio
        }
        System.out.println("[Il primo player ha inviato il gameMode e il numOfPlayers]");
        if (message.parametersReceived()){

            //converto il messaggio arrivato in un oggetto clientModel
            System.out.println(message.getParameter(0));
            clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

            // rimemorizzo le info nel mio database locale
            connectionModel.getClientsInfo().set(0,clientModel);

            System.out.println("E sono " + clientModel.getGamemode() + " " + clientModel.getGameCodeNumber() );
            System.out.println("Ora il server crasherà perché non ci sono altri stati che sono stati scritti" );

            // Invio il solito ack
            Network.send(json.toJson(clientModel));
            System.out.println("[Inviato ack]");

            //scateno l'evento ed esco dallo stato
            message.fireStateEvent();
            message.disable();
        }
        return super.entryAction(cause);
    }
}

