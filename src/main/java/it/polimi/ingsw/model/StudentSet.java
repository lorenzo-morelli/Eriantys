package it.polimi.ingsw.model;

public class StudentSet {
    private StudentColorSet redColorSet;
    private StudentColorSet yellowColorSet;
    private StudentColorSet blueColorSet;
    private StudentColorSet pinkColorSet;
    private StudentColorSet greenColorSet;

    public StudentSet(){
        this.redColorSet = new StudentColorSet();
        this.yellowColorSet = new StudentColorSet();
        this.blueColorSet = new StudentColorSet();
        this.pinkColorSet = new StudentColorSet();
        this.greenColorSet = new StudentColorSet();
    }

    public int getCardinality(Color color){
        if (color == Color.red){
            return this.redColorSet.getCardinality();
        }
        if (color == Color.yellow){
            return this.yellowColorSet.getCardinality();
        }
        if (color == Color.blue){
            return this.blueColorSet.getCardinality();
        }
        if (color == Color.pink){
            return this.pinkColorSet.getCardinality();
        }
        if (color == Color.green){
            return this.greenColorSet.getCardinality();
        }
        //ToDo implementare come una eccezione
        return -1;
    }

    public void setCardinality(int n,Color color){
        if (color == Color.red){
            this.redColorSet.setCardinality(n);
        }
        if (color == Color.yellow){
            this.yellowColorSet.setCardinality(n);
        }
        if (color == Color.blue){
            this.blueColorSet.setCardinality(n);
        }
        if (color == Color.pink){
            this.pinkColorSet.setCardinality(n);
        }
        if (color == Color.green){
            this.greenColorSet.setCardinality(n);
        }
    }

    public void setAllPopulationToZero(){
        this.redColorSet.setCardinality(0);
        this.greenColorSet.setCardinality(0);
        this.pinkColorSet.setCardinality(0);
        this.blueColorSet.setCardinality(0);
        this.yellowColorSet.setCardinality(0);
    }
    // for testing purpose only
    public static void main(String[] args){
        StudentSet myStudentSet = new StudentSet();
        myStudentSet.setCardinality(1,Color.red);
        System.out.println("Il numero di studenti rossi è " + myStudentSet.getCardinality(Color.red));
        myStudentSet.setAllPopulationToZero();
        System.out.println("Il numero di studenti rossi è " + myStudentSet.getCardinality(Color.red));
    }
}
