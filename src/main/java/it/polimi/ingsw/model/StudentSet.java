package it.polimi.ingsw.model;

public class StudentSet {
    private StudentColorSet redColorSet;
    private StudentColorSet yellowColorSet;
    private StudentColorSet blueColorSet;
    private StudentColorSet pinkColorSet;
    private StudentColorSet greenColorSet;

    public StudentSet(){
        this.redColorSet = new StudentColorSet();
        this.redColorSet.setColorName(Color.red);

        this.yellowColorSet = new StudentColorSet();
        this.yellowColorSet.setColorName(Color.yellow);

        this.blueColorSet = new StudentColorSet();
        this.blueColorSet.setColorName(Color.blue);

        this.pinkColorSet = new StudentColorSet();
        this.pinkColorSet.setColorName(Color.pink);

        this.greenColorSet = new StudentColorSet();
        this.greenColorSet.setColorName(Color.green);
    }

    public StudentColorSet getBlueColorSet() {
        return blueColorSet;
    }

    public void setBlueColorSet(StudentColorSet blueColorSet) {
        this.blueColorSet = blueColorSet;
    }

    public StudentColorSet getGreenColorSet() {
        return greenColorSet;
    }

    public void setGreenColorSet(StudentColorSet greenColorSet) {
        this.greenColorSet = greenColorSet;
    }

    public StudentColorSet getPinkColorSet() {
        return pinkColorSet;
    }

    public void setPinkColorSet(StudentColorSet pinkColorSet) {
        this.pinkColorSet = pinkColorSet;
    }

    public StudentColorSet getRedColorSet() {
        return redColorSet;
    }

    public void setRedColorSet(StudentColorSet redColorSet) {
        this.redColorSet = redColorSet;
    }

    public StudentColorSet getYellowColorSet() {
        return yellowColorSet;
    }

    public void setYellowColorSet(StudentColorSet yellowColorSet) {
        this.yellowColorSet = yellowColorSet;
    }

    public static void main(String[] args){
        StudentSet myStudentSet = new StudentSet();
        myStudentSet.redColorSet.setCardinality(7);
        System.out.println("Il colore è " + myStudentSet.redColorSet.getColorName().toString());
        System.out.println("Il numero di studenti di questo colore è " + myStudentSet.redColorSet.getCardinality());
        // incremento cardinalità degli studenti rossi
        myStudentSet.redColorSet.setCardinality(myStudentSet.redColorSet.getCardinality() + 1);
        System.out.println("Il numero di studenti di questo colore è " + myStudentSet.redColorSet.getCardinality());
    }
}
