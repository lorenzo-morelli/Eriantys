package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class CreateOrConnect extends Decision{
    public CreateOrConnect(View view, ClientModel clientModel) throws IOException {
        super(view, clientModel, "CREATE", "CONNECT");
    }
}
