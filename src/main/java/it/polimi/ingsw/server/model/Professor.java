package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

/**
 * This class represents the data model of the professor for each color:
 * professor represent that the players has a greater number of students of
 * the specified color compared to all others players.
 *
 * @author Ignazio Neto Dell'Acqua
 */
public class Professor {
    private final PeopleColor color;
    private Player heldBy;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";

    public Professor(PeopleColor color){
        this.color = color;
        heldBy=null;
    }

    /**
     * Return who has the professor
     * @return player who has the professor
     */
    public Player getHeldBy() {
        return heldBy;
    }

    /**
     * set who has the professor
     * @param heldBy player to set
     */
    public void setHeldBy(Player heldBy) {
        this.heldBy = heldBy;
    }

    /**
     * get the color of the professor
     * @return color from PeopleColor
     */
    public PeopleColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        if(heldBy==null){
            result.append("Nobody ");
        }
        else{
            switch (heldBy.getSchoolBoard().getTowerColor()){
                case WHITE: result.append(ANSI_WHITE).append(heldBy.getNickname()).append(ANSI_RESET);
                    break;
                case GREY:  result.append(ANSI_GRAY).append(heldBy.getNickname()).append(ANSI_RESET);
                    break;
                case BLACK: result.append(ANSI_BLACK).append(heldBy.getNickname()).append(ANSI_RESET);
                    break;
            }
        }
        switch (color){
            case RED: return  ANSI_RED + "      " +color + ANSI_RESET+
                " - " + result;
            case BLUE: return  ANSI_BLUE + color +ANSI_RESET+
                    " - " + result;
            case PINK:return  ANSI_PURPLE + color +ANSI_RESET+
                    " - " + result;
            case GREEN:return  ANSI_GREEN + color +ANSI_RESET+
                    " - " + result;
            case YELLOW:return  ANSI_YELLOW + color +ANSI_RESET+
                    " - " + result;
    }
    return null;
    }

}
