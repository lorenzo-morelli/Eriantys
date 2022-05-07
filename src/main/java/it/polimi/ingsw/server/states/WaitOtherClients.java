package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.server.ServerController;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
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

    private Event allClientsConnected;

    public WaitOtherClients(ServerController serverController) {
        super("[Aspettando gli altri giocatori]");
        this.connectionModel = serverController.getConnectionModel();
        this.controller = serverController.getFsm();
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        allClientsConnected = new Event("Tutti i clients sono collegati e pronti a giocare");
        allClientsConnected.setStateEventListener(controller);
    }

    public Event allClientsConnected() {
        return allClientsConnected;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        // Quanti clients devo ancora aspettare?
        numOfPlayersToWait = connectionModel.getClientsInfo().get(0).getNumofplayer() - 1;

        // Bene, aspettiamoli
        while(numOfPlayersToWait > 0){
            System.out.println("[aspettando altri "+ numOfPlayersToWait+ " clients]");

            message.enable();
            while(!message.parametersReceived() ){
                // non ho ricevuto ancora un messaggio
            }
                // Converti il messaggio stringa json in un oggetto clientModel
                clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                // Ma se il nickname è già esistente? Gestiscimi questa casistic
                isNicknameAlreadyExistent();

                System.out.println("Ricevuto " + clientModel.getNickname() +" " +clientModel.getMyIp());

                // Adesso ho la garanzia di avere un nickname unico
                System.out.println("[Nickname unico]");
                // Compila il campo "non sei primo" e invia la risposta al client
                clientModel.setAmIfirst(false);
                Network.send(json.toJson(clientModel));
                System.out.println("[Client notificato per aver inserito nickname valido]");

                // Appendi alla lista di ClientModel il modello appena ricevuto così da salvarlo per usi futuri
                connectionModel.getClientsInfo().add(clientModel);

                //scateno l'evento ed esco dallo stato
                message.fireStateEvent();
                message.disable();
                numOfPlayersToWait --;
        }
        allClientsConnected.fireStateEvent();
        return super.entryAction(cause);
    }

    public void isNicknameAlreadyExistent(){
        for(ClientModel c : connectionModel.getClientsInfo()){
            if (clientModel.getNickname().equals(c.getNickname())){
                    // ahia, il nickname esiste già
                    // notifico il client di questo fatto
                    Network.send(json.toJson(clientModel));
                    System.out.println("[Client notificato per aver inserito nickname già presente]");

                    // attendo un nickname unico
                    ParametersFromNetwork nickname = new ParametersFromNetwork(1);;

                    boolean messageReceived = false;
                    while(!messageReceived) {
                        nickname = new ParametersFromNetwork(1);
                        nickname.enable();
                        while (!nickname.parametersReceived()) {
                            // non ho ricevuto ancora un messaggio
                        }
                        if (json.fromJson(nickname.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()){
                            messageReceived = true;
                        }
                    }
                    clientModel = json.fromJson(nickname.getParameter(0), ClientModel.class);
                    isNicknameAlreadyExistent();
            }
        }
    }
}
