package it.polimi.ingsw.server.model.characters;


public class CharacterCard {
    private final String Name;
    private int cost;
    public static final String ANSI_CYAN = "\033[0;36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public CharacterCard(int cost, String name){
        this.Name= name;
        this.cost=cost;
    }

    public int getCost() {
        return cost;
    }

    public void improveCost() {
        this.cost ++;
    }

    @Override
    public String toString() {
        return ANSI_CYAN + "    "+getName()+ "   COST: " + cost +ANSI_RESET ;
    }

    public String getName() {
        return Name;
    }
}
