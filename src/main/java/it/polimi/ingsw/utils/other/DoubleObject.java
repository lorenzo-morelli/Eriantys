package it.polimi.ingsw.utils.other;

import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

public class DoubleObject{
    public ParametersFromNetwork getParame() {
        return parame;
    }
    public boolean isRespo() {
        return respo;
    }
    private final ParametersFromNetwork parame;
    boolean respo;
    public DoubleObject(ParametersFromNetwork param, boolean resp){
        parame= param;
        respo= resp;
    }
};