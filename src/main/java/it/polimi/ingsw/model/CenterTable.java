package it.polimi.ingsw.model;
import java.util.*;

public class CenterTable {
    private Vector<Cloud> clouds;
    private Vector<Island> islands;

    public CenterTable(){
        this.clouds = new Vector<Cloud>(4);
        this.islands = new Vector<Island>(12);
        };

    // Voglio poter caricare su un isola i-esima una data quantità di studenti di un certo colore
     public void PushOnCloud(int index, int n, Color color){
         this.clouds.get(index).set(n,color);
    }

    //Voglio poter leggere il contenuto di una certa etnia di colore di una certa isola
    public int ReadCloud(int index, Color color){
        return this.clouds.get(index).get(color);
    }

    //Voglio poter fare la stessa cosa per un'isola
    public int PushOnIsland(int index, Color color){
         return this.islands.get(index).getInhabitants(color);
    }

    public void ReadIsland(int index, int n, Color color){
        this.islands.get(index).setInhabitants(n,color);
    }

    //Voglio poter unire due isole adiacenti
    public void MergeIslands(int i, int j){
         if(i-j == 1 || j-i == 1 || i == 11 && j == 0 || i == 0 && j == 11){   //se le isole sono adiacenti
             if(i<j){
                 this.islands.get(i).setLinkedWithNext(true);
                 this.islands.get(j).setLinkedWithPrev(true);
             }
             else{
                 this.islands.get(j).setLinkedWithNext(true);
                 this.islands.get(i).setLinkedWithPrev(true);
             }
         }
    }

    //voglio poter settare Madre natura di un isola a caso

    public void setMotherNature(int index, boolean bool){
         this.islands.get(index).setHasMotherNature(bool);
    }


    public int whereIsMotherNature(){
         for(int i=0;i<=11;i++){
             if (this.islands.get(i).HasMotherNature()==true){
                 return i;
             }
         }
         //ToDo: implementare con una eccezione
        return -1;
    }

    //voglio poter muovere madre natura di n passi
    public void MoveMotherNature(int n){
         int oldPosition = whereIsMotherNature();
         this.islands.get(oldPosition).setHasMotherNature(false);
         this.islands.get((oldPosition + n) % 12).setHasMotherNature(true);
    }
}
