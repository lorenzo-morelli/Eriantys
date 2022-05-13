package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

public class Professor {
    private final PeopleColor color;
    private Player heldBy;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

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
        switch (color){
            case RED: return  ANSI_RED + "      " +color +
                " - " + (heldBy==null ? "Nobody" : heldBy.getNickname())  + " "+ANSI_RESET;
            case BLUE: return  ANSI_BLUE + color +
                    " - " + (heldBy==null ? "Nobody" : heldBy.getNickname())  + " "+ANSI_RESET;
            case PINK:return  ANSI_PURPLE + color +
                    " - " + (heldBy==null ? "Nobody" : heldBy.getNickname())  + " "+ANSI_RESET;
            case GREEN:return  ANSI_GREEN + color +
                    " - " + (heldBy==null ? "Nobody" : heldBy.getNickname())  + " "+ANSI_RESET;
            case YELLOW:return  ANSI_YELLOW + color +
                    " - " + (heldBy==null ? "Nobody" : heldBy.getNickname())  + " "+ANSI_RESET;
    }
    return null;
    }

}
