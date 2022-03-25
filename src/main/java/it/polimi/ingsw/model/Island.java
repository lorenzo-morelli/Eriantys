package it.polimi.ingsw.model;

public class Island {
    int serialNumber;  // può andare da 0 ad 11 (aritmentica modulare modulo 12)
    boolean linkedWithPrev;
    boolean linkedWithNext;
    boolean hasMotherNature;
    StudentSet inhabitants;
    Color towerColor;   // se towerColor = Color.NULLCOLOR allora non ci sono torri su quell'isola
                        // di default un'isola appena costruita non può avere torri né avere madre natura

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

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
    public Island(){
        this.inhabitants = new StudentSet();
        this.hasMotherNature = false;
        this.towerColor=Color.nullcolor;
    }

}
