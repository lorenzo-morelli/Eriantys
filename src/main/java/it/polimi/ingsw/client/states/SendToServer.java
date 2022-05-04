package it.polimi.ingsw.client.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class SendToServer extends State{
    private Gson json;
    private ClientModel clientModel;
    private ParametersFromNetwork ack;
    public SendToServer(ClientModel clientModel, Controller controller) {
        super("[Invio dati al server ed attesa di un ack]");
        this.clientModel = clientModel;
        ack = new ParametersFromNetwork(1);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        clientModel.setNumofplayer(Integer.parseInt(clientModel.getFromTerminal().get(0)));
        clientModel.setGamemode(clientModel.getFromTerminal().get(1));
        Network.send(json.toJson(clientModel));
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException{
        try {
            // Attendi la ricezione dell'ack
            checkAck();
        }catch (InterruptedException e){e.printStackTrace();}
    }

    public ParametersFromNetwork getAck() {
        return ack;
    }

    public void checkAck() throws IOException, InterruptedException {

        ack.enable();
        while (!ack.parametersReceived()){
            // non ho ancora ricevuto l'ack
        }
        ack.disable();

        // Se ho ricevuto l'ack controllo che effettivamente è un ack inviato a me
        // if messaggioArrivato.getNickname == mio nickname then ho ricevuto ack
        // else attendi l'ack
        // (con il metodo .equals perché sono Stringhe)
        if(!json.fromJson(ack.getParameter(0),ClientModel.class).getNickname().equals(clientModel.getNickname())){
            ack = new ParametersFromNetwork(1);
            checkAck();
        }
        else {
            ack.fireStateEvent();
        }

    }
}
