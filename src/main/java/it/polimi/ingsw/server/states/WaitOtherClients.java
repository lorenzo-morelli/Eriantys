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
        while(numOfPlayersToWait > 0){
            System.out.println("[aspettando altri "+ numOfPlayersToWait+ " clients]");

            message.enable();
            while(!message.parametersReceived() ){
                // non ho ricevuto ancora un messaggio
            }
                // Converti il messaggio stringa json in un oggetto clientModel
                clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                System.out.println("Ricevuto " + clientModel.getNickname() +" " +clientModel.getMyIp());

                // Ma se il nickname è già esistente??
                isNicknameAlreadyExistent();

                // Appendi alla lista di ClientModel il modello appena ricevuto così da salvarlo per usi futuri
                connectionModel.getClientsInfo().add(clientModel);

                //scateno l'evento ed esco dallo stato
                message.fireStateEvent();
                message.disable();
                numOfPlayersToWait --;
        }
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
                    ParametersFromNetwork nickname = new ParametersFromNetwork(1);
                    nickname.enable();
                    while(!nickname.parametersReceived() ){
                       // non ho ricevuto ancora un messaggio
                    }

                    clientModel = json.fromJson(nickname.getParameter(0), ClientModel.class);
                    isNicknameAlreadyExistent();
            }
        }
        System.out.println("[Nickname unico]");
        // Compila il campo "non sei primo" e invia la risposta al client
        clientModel.setAmIfirst(false);
        Network.send(json.toJson(clientModel));
        System.out.println("[Client notificato per aver inserito nickname valido]");
    }
}
