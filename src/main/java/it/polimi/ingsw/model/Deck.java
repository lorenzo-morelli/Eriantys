package it.polimi.ingsw.model;
import java.util.ArrayList;
public class Deck {
    private ArrayList<AssistantCard> cards;

    public Deck(){
        this.cards= new ArrayList<>();
        // adesso c'è da inizializzare il deck appena costruito
        this.cards.add(new AssistantCard(1,1));
        this.cards.add(new AssistantCard(2,1));
        this.cards.add(new AssistantCard(3,2));
        this.cards.add(new AssistantCard(4,2));
        this.cards.add(new AssistantCard(5,3));
        this.cards.add(new AssistantCard(6,3));
        this.cards.add(new AssistantCard(7,4));
        this.cards.add(new AssistantCard(8,4));
        this.cards.add(new AssistantCard(9,5));
        this.cards.add(new AssistantCard(10,5));
    }

    // Serve qualche metodo per elencare all'utente le carte disponibili
    // senza passargli direttamente una reference alla lista
    // sicuramente questo metodo non soddisfa quanto richiesto, ma per il momento può
    // andar bene
    public void listCards(){
        for(int i=0; i<this.cards.size();i++){
            System.out.println( "("+ i + ") " + "["+ this.cards.get(i).getValues() + ","+this.cards.get(i).getMoves() +"]");
        }
    }

    //Una volta scelta, la carta deve poter essere rimossa
    public void remove(int index){
        this.cards.remove(index);
    }



    public static void main(String args[]){
        Deck mioDeck = new Deck();
        mioDeck.listCards();
        //putacaso l'utente scegliesse la carta 2
        mioDeck.remove(2);
        mioDeck.listCards();
    }


}
