package it.polimi.ingsw.model;

public class Island {
    //private int serialNumber;  // può andare da 0 ad 11 (aritmentica modulare modulo 12)
    private boolean linkedWithPrev;
    private boolean linkedWithNext;
    private boolean hasMotherNature;
    private StudentSet inhabitants;
    private Color towerColor;   // se towerColor = Color.NULLCOLOR allora non ci sono torri su quell'isola
                                // di default un'isola appena costruita non può avere torri né avere madre natura

    public Island(){
        this.inhabitants = new StudentSet();
        // inizializzazione
        this.hasMotherNature = false;
        this.towerColor=Color.nullcolor;
        this.inhabitants.setAllPopulationToZero();
        this.linkedWithPrev = false;
        this.linkedWithNext = false;
    }

    public int getInhabitants(Color color){
        return this.inhabitants.getCardinality(color);
    }

    public void setInhabitants(int n, Color color){
        this.inhabitants.setCardinality(n,color);
    }

    public Color getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(Color towerColor) {
        this.towerColor = towerColor;
    }
/*
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }*/

    public boolean isLinkedWithNext() {
        return linkedWithNext;
    }
    public void setLinkedWithNext(boolean linked) {
        this.linkedWithNext = linked;
    }

    public boolean isLinkedWithPrev() {
        return linkedWithPrev;
    }
    public void setLinkedWithPrev(boolean linked) {
        this.linkedWithPrev = linked;
    }
    public boolean HasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean mn) {
        this.hasMotherNature = mn;
    }


}
