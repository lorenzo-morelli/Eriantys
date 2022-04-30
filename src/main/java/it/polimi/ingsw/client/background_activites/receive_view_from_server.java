package it.polimi.ingsw.client.background_activites;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;

public class receive_view_from_server implements Runnable{
    private View view;
    public receive_view_from_server(View view){
        this.view= view;
    }
    @Override
    public void run() {
        while (Network.isConnected()) { //o almeno fino a che è connesso
            //todo: ricevi e printa la vista (cioè ogni messaggio di aggiornamento che fa il server,
            //                                                        che viene mandato broadcasta a tutti)
        }
    }
}
