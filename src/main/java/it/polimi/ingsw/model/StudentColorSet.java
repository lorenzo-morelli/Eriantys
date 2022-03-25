package it.polimi.ingsw.model;

public class StudentColorSet {
    private Color colorName;
    private int cardinality;

    //A java bean has empty constructor, is a Struct !
    public StudentColorSet(){}

    public Color getColorName() {
        return colorName;
    }

    public void setColorName(Color colorName) {
        this.colorName = colorName;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }
}
