package it.polimi.ingsw.server;

import it.polimi.ingsw.server.states.Idle;
import it.polimi.ingsw.server.states.SpecifyPort;
import it.polimi.ingsw.server.states.Wait;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

public class ServerController{
    private Network network;

    private final Idle idle;    // modo elegante di far partire il controllore
    private final SpecifyPort specifyPort;

    private final Wait wait;
    private final Event start;

    public ServerController() throws IOException, InterruptedException {
        CommandPrompt.setDebug();
        this.network = new Network();

        idle = new Idle();
        start = new Event("[Controller Started]");

        Controller fsm = new Controller("Controllore del Server",idle);

        //Costruzione degli stati necessari
        specifyPort = new SpecifyPort(network);
        wait = new Wait();
        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, specifyPort);
        fsm.addTransition(specifyPort, specifyPort.portSpecified(), wait);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

    public static void main(String args[]) {
        try {
            new ServerController();
        } catch (InterruptedException | IOException e){}
    }
}
