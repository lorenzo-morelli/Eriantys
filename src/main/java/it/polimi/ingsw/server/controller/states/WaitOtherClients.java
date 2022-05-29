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


public class WaitOtherClients extends State {
    private final ConnectionModel connectionModel;
    private ClientModel clientModel = null;
    private final Gson json;
    private ParametersFromNetwork message;

    private final Event twoOrThreeClientsConnected;
    private final Event reset = new ClientDisconnection();
    private final Event fourClientsConnected;

    public WaitOtherClients(ServerController serverController) {
        super("[Aspettando gli altri giocatori]");
        this.connectionModel = serverController.getConnectionModel();
        Controller controller = ServerController.getFsm();
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        fourClientsConnected= new Event("4 clients sono collegati e pronti a giocare");
        fourClientsConnected.setStateEventListener(controller);
        twoOrThreeClientsConnected= new Event("2 o 3 clients sono collegati e pronti a giocare");
        twoOrThreeClientsConnected.setStateEventListener(controller);
        reset.setStateEventListener(controller);
    }
    public Event getReset() {
        return reset;
    }

    public Event twoOrThreeClientsConnected() {
        return twoOrThreeClientsConnected;
    }

    public Event fourClientsConnected() {
        return fourClientsConnected;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        // Quanti clients devo ancora aspettare?
        int numOfPlayersToWait = connectionModel.getClientsInfo().get(0).getNumofplayer() - 1;

        // Bene, aspettiamoli
        while (numOfPlayersToWait > 0) {
            System.out.println("[aspettando altri " + numOfPlayersToWait + " clients]");

            message=new ParametersFromNetwork(1);
            message.enable();
            while (!message.parametersReceived()) {
                message.waitParametersReceived(10);
                if(Network.disconnectedClient()){
                    reset.fireStateEvent();
                    return super.entryAction(cause);
                }
            }
            // Converti il messaggio stringa json in un oggetto clientModel
            clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

            // Ma se il nickname è già esistente? Gestiscimi questa casistic
            isNicknameAlreadyExistent();

            System.out.println("Ricevuto " + clientModel.getNickname() + " " + clientModel.getMyIp());

            // Adesso ho la garanzia di avere un nickname unico
            System.out.println("[Nickname unico]");
            // Compila il campo "non sei primo" e invia la risposta al client
            clientModel.setAmIfirst(false);
            Network.send(json.toJson(clientModel));
            System.out.println("[Client notificato per aver inserito nickname valido]");

            // Appendi alla lista di ClientModel il modello appena ricevuto così da salvarlo per usi futuri
            connectionModel.getClientsInfo().add(clientModel);

            //scateno l'evento ed esco dallo stato
            numOfPlayersToWait--;

        }
        if (connectionModel.getClientsInfo().size() == 4){
            fourClientsConnected.fireStateEvent();
        }
        else if(connectionModel.getClientsInfo().size() == 3 || connectionModel.getClientsInfo().size()==2){
            twoOrThreeClientsConnected.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    public void isNicknameAlreadyExistent() throws InterruptedException {
        for (ClientModel c : connectionModel.getClientsInfo()) {
            if (clientModel.getNickname().equals(c.getNickname())) {
                // ahia, il nickname esiste già
                // notifico il client di questo fatto
                Network.send(json.toJson(clientModel));
                System.out.println("[Client notificato per aver inserito nickname già presente]");

                // attendo un nickname unico
                ParametersFromNetwork nickname = new ParametersFromNetwork(1);


                boolean messageReceived = false;
                while (!messageReceived) {
                    nickname = new ParametersFromNetwork(1);
                    nickname.enable();
                    nickname.waitParametersReceived();
                    if (json.fromJson(nickname.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                        messageReceived = true;
                    }
                }
                clientModel = json.fromJson(nickname.getParameter(0), ClientModel.class);
                isNicknameAlreadyExistent();
            }
        }
    }
}
