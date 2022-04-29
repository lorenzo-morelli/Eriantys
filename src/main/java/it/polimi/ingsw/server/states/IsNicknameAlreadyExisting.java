package it.polimi.ingsw.server.states;

import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.server.model.IpAndNickame;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class IsNicknameAlreadyExisting extends State {
    private ConnectionInfo connectionInfo;

    private Event nicknameExist;
    private Event nicknameNotExist;
    public IsNicknameAlreadyExisting(ConnectionInfo connectionInfo) {
        super("[Check if the nickname is already present in the database]");
        this.connectionInfo = connectionInfo;
        nicknameExist = new Event("Il nickname già esiste");
        nicknameNotExist = new Event("Il nickname non esiste");
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        boolean nicknameAlreadyExists = false;
        for (IpAndNickame i : connectionInfo.getIpAndNickames()){
            if(connectionInfo.getNewNickname().equals(i.getNickname())){
                    nicknameAlreadyExists = true;
                    Network.send(connectionInfo.getNewNickname()+" NICKNAME_ALREADY_EXIST");
                    nicknameExist.fireStateEvent();
            }
        }
        if(!nicknameAlreadyExists){

            Network.send(connectionInfo.getNewNickname() + " CREATION_SUCCESSFUL");
            while(true){

            }
            //nicknameNotExist.fireStateEvent();
        }

        return super.entryAction(cause);
    }
}
