package it.polimi.ingsw.model;

public class Player {
    String nickname;
    int userId;
    Deck cards;
    int choosenCard;
    SchoolBoard mySchoolBoard;
    public Player(){
        cards = new Deck();
        mySchoolBoard = new SchoolBoard();
        //roba di default
        this.nickname = "Steve";
        this.choosenCard = 0;
    }

    public void setTowerColor(Color color){
        this.mySchoolBoard.setTowerColor(color);
    }

    public void setTowers(int quantity){
        this.mySchoolBoard.setTowerQuantity(quantity);
    }

    // Attenzione: non è possibile e non ha senso voler spostare una torre su un isola:
    // la classe player non è il giocatore, è solo una struttura dati !
    // è il modello che saprà quanti giocatori ci sono, ed avrà istanziate tutte le strutture dati
    // per farsi aggiornare dal controllore !

    public Color getTowerColor(){
        return this.mySchoolBoard.getTowerColor();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    //todo: selezione e rimozione carte


}
