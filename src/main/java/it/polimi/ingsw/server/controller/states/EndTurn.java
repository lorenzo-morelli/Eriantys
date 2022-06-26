package it.polimi.ingsw.server.controller.states;

import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

/**
 * This state handles the things that the server has to do
 * at the end of the shift (for example reload the cloud cards)
 */
public class EndTurn extends State {
    private final Event goToAssistantCardPhase;

    private final ServerController serverController;

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public Event goToAssistantCardPhase() {
        return goToAssistantCardPhase;
    }

    public EndTurn(ServerController serverController) {
        super("[End Turn]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        goToAssistantCardPhase = new Event("end turn");
        goToAssistantCardPhase.setStateEventListener(controller);
    }

    /**
     * Refills the clouds, check if the game is ended because the bag is empty,
     * else fire the event to go to the assistant card phase
     * @param cause the event that caused the transition in this state
     * @return null event
     * @throws Exception input output errors or network related ones
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        for(int i = 0; i< model.getTable().getClouds().size(); i++){
            if(model.getTable().getClouds().get(i).getStudentsAccumulator().size()==0) {
                boolean check = model.getTable().getClouds().get(i).charge(model.getTable().getBag());
                if (!check) {
                    model.setLastTurn();
                    break;
                }
            }
        }
        model.nextPlayer();
        goToAssistantCardPhase().fireStateEvent();
        return super.entryAction(cause);
    }
}