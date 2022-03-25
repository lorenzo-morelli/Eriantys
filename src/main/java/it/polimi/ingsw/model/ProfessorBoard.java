package it.polimi.ingsw.model;

public class ProfessorBoard {
    private boolean hasRedProfessor;
    private boolean hasYellowProfessor;
    private boolean hasBlueProfessor;
    private boolean hasPinkProfessor;
    private boolean hasGreenProfessor;

    // A java bean has an empty constructor
    public ProfessorBoard(){}

    public boolean HasBlueProfessor() {
        return hasBlueProfessor;
    }

    public boolean HasGreenProfessor() {
        return hasGreenProfessor;
    }

    public boolean HasPinkProfessor() {
        return hasPinkProfessor;
    }

    public boolean HasRedProfessor() {
        return hasRedProfessor;
    }

    public boolean HasYellowProfessor() {
        return hasYellowProfessor;
    }

    public void setHasBlueProfessor(boolean hasBlueProfessor) {
        this.hasBlueProfessor = hasBlueProfessor;
    }

    public void setHasGreenProfessor(boolean hasGreenProfessor) {
        this.hasGreenProfessor = hasGreenProfessor;
    }

    public void setHasPinkProfessor(boolean hasPinkProfessor) {
        this.hasPinkProfessor = hasPinkProfessor;
    }

    public void setHasRedProfessor(boolean hasRedProfessor) {
        this.hasRedProfessor = hasRedProfessor;
    }

    public void setHasYellowProfessor(boolean hasYellowProfessor) {
        this.hasYellowProfessor = hasYellowProfessor;
    }

}
