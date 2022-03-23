package it.polimi.ingsw.model;

public class IslandSet {
    private Color test= Color.Yellow;

    public void test(){
        if (this.test.equals(Color.Yellow)){
            System.out.println("Sono Giallo");
        }
    }

    public static void main(String args[]){
        IslandSet Arcipelago = new IslandSet();
        Arcipelago.test();
    }
}
