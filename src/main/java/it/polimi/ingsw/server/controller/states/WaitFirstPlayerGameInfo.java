package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

public class WaitFirstPlayerGameInfo extends State {
    private final Gson json;
    private Controller controller;
    private final ConnectionModel connectionModel;

    private final ParametersFromNetwork message;
    private final Event reset = new ClientDisconnection();

    public WaitFirstPlayerGameInfo(ServerController serverController) {
        super("[Il server è in attesa di gamemode e numero di giocatori]");
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        this.controller = ServerController.getFsm();
        reset.setStateEventListener(controller);
        this.connectionModel = serverController.getConnectionModel();
    }
    public Event getReset() {
        return reset;
    }

    public ParametersFromNetwork gotNumOfPlayersAndGamemode() {
        return message;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        boolean messageReceived = false;
        System.out.println("[Non ho ancora ricevuto niente]");

        while (!messageReceived) {
            message.enable();
            while (!message.parametersReceived()) {
                if(Network.disconnectedClient()){
                    reset.fireStateEvent();
                    return super.entryAction(cause);
                }
            }
            // controllo se il messaggio è arrivato proprio dal primo client
            if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == connectionModel.getClientsInfo().get(0).getClientIdentity()) {
                messageReceived = true;
            }

        }
        System.out.println("[Il primo player ha inviato il gameMode e il numOfPlayers]");
        if (message.parametersReceived()) {

            //converto il messaggio arrivato in un oggetto clientModel
            System.out.println(message.getParameter(0));
            ClientModel clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

            // rimemorizzo le info nel mio database locale
            connectionModel.getClientsInfo().set(0, clientModel);

            System.out.println("E sono " + clientModel.getGameMode() + " " + clientModel.getNumofplayer());

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

