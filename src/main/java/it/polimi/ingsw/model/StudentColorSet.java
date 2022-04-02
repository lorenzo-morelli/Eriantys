package it.polimi.ingsw.model;

public class StudentColorSet {
    private int cardinality;

    //A java bean has empty constructor, is a Struct !
    public StudentColorSet(){}


    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }
}
