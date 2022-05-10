package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

public class Professor {
    private final PeopleColor color;
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

    @Override
    public String toString() {
        return "Professor{" +
                "color=" + color.toString() +
                ", heldBy=" + (heldBy==null ? "null" : heldBy.getNickname())  +
                '}';
    }
}
