package it.polimi.ingsw.model;

public class Cloud {
    StudentSet studentAccumulator;  // accumulatore per la carica e scarica di studenti

    public Cloud(){
        this.studentAccumulator = new StudentSet();
    }
}
