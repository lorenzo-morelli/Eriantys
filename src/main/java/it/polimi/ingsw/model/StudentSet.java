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
