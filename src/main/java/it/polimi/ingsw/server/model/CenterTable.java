package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class CenterTable {
    private ArrayList<Cloud> clouds;
    private ArrayList<Island> islands;
    private int motherNaturePosition;
    private StudentSet bag;
    private ArrayList<Professor> professors;
    private ArrayList<Character> characterCards; //TODO

    public CenterTable(int numcloud){
        int i;

        clouds =new ArrayList<>();
        for(i=0;i<numcloud;i++){
            clouds.add(new Cloud());
        }

        islands=new ArrayList<>(islands);
        for(i=0;i<12;i++){
            islands.add(new Island());
        }

        motherNaturePosition=0;

        bag=new StudentSet(25,25,25,25,25);

        professors=new ArrayList<>(5);
        professors.add(new Professor(PeopleColor.RED));
        professors.add(new Professor(PeopleColor.YELLOW));
        professors.add(new Professor(PeopleColor.BLUE));
        professors.add(new Professor(PeopleColor.GREEN));
        professors.add(new Professor(PeopleColor.PINK));

    }
}