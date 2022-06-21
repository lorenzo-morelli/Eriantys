package it.polimi.ingsw.utils.other;

import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

/**
 * This class make the users able to store the message and the result of its waiting (boolean value) into a single object
 */
public class DoubleObject{
    public ParametersFromNetwork getParam() {
        return param;
    }
    public boolean isResp() {
        return resp;
    }
    private final ParametersFromNetwork param;
    boolean resp;
    public DoubleObject(ParametersFromNetwork param, boolean resp){
        this.param = param;
        this.resp = resp;
    }
}