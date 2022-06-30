package it.polimi.ingsw.client.view.common;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;

public class SetConnection {
    public static void setConnection(String nickname, String ip, String port, ClientModel clientModel) {
        clientModel.setNickname(nickname);
        clientModel.setIp(ip);
        clientModel.setPort(port);
        Network.setupClient(ip, port);
        clientModel.setMyIp(Network.getMyIp());
    }
}
