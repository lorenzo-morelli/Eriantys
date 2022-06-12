package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.ArrayList;

public class WaitFirstPlayer extends State {
    private final Gson json;
    private Controller controller;
    private final ConnectionModel connectionModel;
    private final ParametersFromNetwork firstMessage;
    private final Event reset = new ClientDisconnection();

    public WaitFirstPlayer(ServerController serverController) {
        super("[Il server è in attesa del primo giocatore]");
        json = new Gson();
        firstMessage = new ParametersFromNetwork(1);
        firstMessage.setStateEventListener(controller);
        this.controller = ServerController.getFsm();
        reset.setStateEventListener(controller);
        this.connectionModel = serverController.getConnectionModel();
    }

    public Event getReset() {
        return reset;
    }

    public ParametersFromNetwork gotFirstMessage() {
        return firstMessage;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        if (Network.disconnectedClient()){
            Network.disconnect();
        }
        System.out.println("[Listening on port " + CommandPrompt.gotFromTerminal()+ " ]");
        Network.setupServer(CommandPrompt.gotFromTerminal());
        Network.setDisconnectedClient(false);
        connectionModel.setClientsInfo(new ArrayList<>());


        firstMessage.enable();

        while (!firstMessage.parametersReceived() ) {
            //System.out.println("loop");
            firstMessage.waitParametersReceived(10);
            if(Network.disconnectedClient()){
                reset.fireStateEvent();
            }
        }

        System.out.println("[Il primo player si è connesso]");
        if (firstMessage.parametersReceived()) {
            // Converti il messaggio stringa json in un oggetto clientModel
            ClientModel clientModel = json.fromJson(firstMessage.getParameter(0), ClientModel.class);

            System.out.println("Ricevuto " + clientModel.getNickname() + " " + clientModel.getMyIp());
            // Compila il campo "sei primo" e invia la risposta al client
            connectionModel.getClientsInfo().add(0, clientModel);
            clientModel.setAmIfirst(true);
            Network.send(json.toJson(clientModel));
            System.out.println("[Inviato ack al primo player]");

            //scateno l'evento ed esco dallo stato
            firstMessage.fireStateEvent();
            firstMessage.disable();
        }
        return super.entryAction(cause);
    }
}
