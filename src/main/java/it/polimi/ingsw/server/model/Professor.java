package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.Optional;

public class Professor {
    private PeopleColor color;
    private Player heldBy;

    public Professor(PeopleColor color){
        this.color = color;
        heldBy=null;
    }

    public Player getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(Player heldBy) {
        this.heldBy = heldBy;
    }

    public PeopleColor getColor() {
        return color;
    }

    public void setColor(PeopleColor color) {
        this.color = color;
    }
}
